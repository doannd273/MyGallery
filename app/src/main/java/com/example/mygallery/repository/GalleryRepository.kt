package com.example.mygallery.repository

import com.example.mygallery.model.Image

interface GalleryRepository {
    suspend fun getImages(): List<Image>
}