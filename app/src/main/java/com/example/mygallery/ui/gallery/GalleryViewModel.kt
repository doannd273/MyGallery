package com.example.mygallery.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygallery.permission.PhotoAccess
import com.example.mygallery.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.ItemClick -> {
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
                    state.copy(
                        isLoading = false,
                        hasLoadError = false,
                        images = images,
                        selectedImages = state.selectedImages.filter { selectedImage ->
                            images.any { image -> image.id == selectedImage.id }
                        },
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        hasLoadError = true,
                        images = emptyList(),
                        selectedImages = emptyList(),
                    )
                }
            }
        }
    }
}
