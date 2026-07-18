package com.example.mygallery.di

import com.example.mygallery.repository.GalleryRepository
import com.example.mygallery.repository.GalleryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindGalleryRepository(impl: GalleryRepositoryImpl): GalleryRepository
}
