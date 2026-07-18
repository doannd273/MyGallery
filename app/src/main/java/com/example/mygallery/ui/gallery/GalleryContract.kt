package com.example.mygallery.ui.gallery

import com.example.mygallery.model.Image
import com.example.mygallery.permission.PhotoAccess

const val MAX_SELECTED_IMAGES = 5

data class GalleryState(
    val access: PhotoAccess = PhotoAccess.Denied,
    val isLoading: Boolean = false,
    val hasLoadError: Boolean = false,
    val isActionBottomSheetVisible: Boolean = false,
    val images: List<Image> = emptyList(),
    val selectedImages: List<Image> = emptyList(),
)

sealed class GalleryEvent {
    data class ItemClick(val id: Long) : GalleryEvent()
    data class RemoveImage(val id: Long) : GalleryEvent()
}

sealed class GalleryEffect {
    data object ShowMaxSelectionReached : GalleryEffect()
}
