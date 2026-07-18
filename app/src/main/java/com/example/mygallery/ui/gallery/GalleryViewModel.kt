package com.example.mygallery.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygallery.model.Image
import com.example.mygallery.model.NO_POSITION
import com.example.mygallery.permission.PhotoAccess
import com.example.mygallery.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryState())
    val uiState: StateFlow<GalleryState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GalleryEffect>()
    val effect: SharedFlow<GalleryEffect> = _effect.asSharedFlow()

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.ItemClick -> {
                toggleImageSelection(event.id)
            }

            is GalleryEvent.RemoveImage -> {
                removeImageSelection(event.id)
            }
        }
    }

    fun onPhotoAccessChanged(access: PhotoAccess) {
        if (access == PhotoAccess.Denied) {
            _uiState.update { state ->
                state.copy(
                    access = access,
                    isLoading = false,
                    hasLoadError = false,
                    isActionBottomSheetVisible = false,
                    images = emptyList(),
                    selectedImages = emptyList(),
                )
            }
            return
        }

        _uiState.update { state ->
            state.copy(access = access)
        }
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    hasLoadError = false,
                )
            }

            runCatching {
                galleryRepository.getImages()
            }.onSuccess { images ->
                _uiState.update { state ->
                    val selectedIds = state.selectedImages.map { image -> image.id }
                    val updatedImages = images.withSelection(selectedIds)
                    val selectedImages = updatedImages.selectedBy(selectedIds)

                    state.copy(
                        isLoading = false,
                        hasLoadError = false,
                        images = updatedImages,
                        selectedImages = selectedImages,
                        isActionBottomSheetVisible =
                            state.isActionBottomSheetVisible && selectedImages.isNotEmpty(),
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        hasLoadError = true,
                        isActionBottomSheetVisible = false,
                        images = emptyList(),
                        selectedImages = emptyList(),
                    )
                }
            }
        }
    }

    private fun toggleImageSelection(id: Long) {
        val currentState = _uiState.value
        val currentSelectedIds = currentState.selectedImages.map { image -> image.id }

        if (id !in currentSelectedIds && currentSelectedIds.size >= MAX_SELECTED_IMAGES) {
            viewModelScope.launch {
                _effect.emit(GalleryEffect.ShowMaxSelectionReached)
            }
            return
        }

        _uiState.update { state ->
            val currentSelectedIds = state.selectedImages.map { image -> image.id }
            val nextSelectedIds = if (id in currentSelectedIds) {
                currentSelectedIds.filterNot { selectedId -> selectedId == id }
            } else {
                currentSelectedIds + id
            }
            val updatedImages = state.images.withSelection(nextSelectedIds)
            val selectedImages = updatedImages.selectedBy(nextSelectedIds)

            state.copy(
                images = updatedImages,
                selectedImages = selectedImages,
                isActionBottomSheetVisible = selectedImages.isNotEmpty(),
            )
        }
    }

    private fun removeImageSelection(id: Long) {
        _uiState.update { state ->
            val nextSelectedIds = state.selectedImages
                .map { image -> image.id }
                .filterNot { selectedId -> selectedId == id }
            val updatedImages = state.images.withSelection(nextSelectedIds)
            val selectedImages = updatedImages.selectedBy(nextSelectedIds)

            state.copy(
                images = updatedImages,
                selectedImages = selectedImages,
                isActionBottomSheetVisible = selectedImages.isNotEmpty(),
            )
        }
    }

    private fun List<Image>.withSelection(selectedIds: List<Long>): List<Image> {
        val selectedPositions = selectedIds
            .withIndex()
            .associate { (index, id) -> id to index + 1 }

        return map { image ->
            val position = selectedPositions[image.id]

            image.copy(
                isSelected = position != null,
                positionSelected = position ?: NO_POSITION,
            )
        }
    }

    private fun List<Image>.selectedBy(selectedIds: List<Long>): List<Image> {
        return selectedIds.mapNotNull { selectedId ->
            firstOrNull { image -> image.id == selectedId && image.isSelected }
        }
    }
}
