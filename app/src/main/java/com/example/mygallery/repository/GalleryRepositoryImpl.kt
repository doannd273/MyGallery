package com.example.mygallery.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.example.mygallery.model.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : GalleryRepository {

    override suspend fun getImages(): List<Image> {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.MIME_TYPE,
            )

            val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val images = mutableListOf<Image>()

            contentResolver.query(
                collectionUri,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idColumnIndex =
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Images.Media._ID
                    )

                val displayNameColumnIndex =
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DISPLAY_NAME
                    )

                val sizeColumnIndex =
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Images.Media.SIZE
                    )

                val mimeTypeColumnIndex =
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Images.Media.MIME_TYPE
                    )

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumnIndex)

                    val imageUri = ContentUris.withAppendedId(
                        collectionUri, id
                    )
                    val displayName = cursor.getString(displayNameColumnIndex)
                    val size = cursor.getLong(sizeColumnIndex)
                    val mimeType = cursor.getString(mimeTypeColumnIndex)

                    images += Image(
                        id = id,
                        displayName = displayName,
                        uri = imageUri,
                        sizeBytes = size,
                        mimeType = mimeType,
                    )
                }

            }

            images
        }
    }
}
