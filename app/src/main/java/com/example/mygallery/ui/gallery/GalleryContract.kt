package com.example.mygallery.ui.gallery

import com.example.mygallery.model.Image

data class GalleryState(
    val images: List<Image> = listOf(),
    val selectedImages: List<Image> = listOf(),
)