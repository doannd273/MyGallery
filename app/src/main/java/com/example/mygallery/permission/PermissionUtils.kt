package com.example.mygallery.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

enum class PhotoAccess {
    Full,
    Partial,
    Denied,
}

fun Context.getPhotoAccess(): PhotoAccess {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES,
                ) == PackageManager.PERMISSION_GRANTED -> {
            PhotoAccess.Full
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                ) == PackageManager.PERMISSION_GRANTED -> {
            PhotoAccess.Partial
        }

        Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2 &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ) == PackageManager.PERMISSION_GRANTED -> {
            PhotoAccess.Full
        }

        else -> PhotoAccess.Denied
    }
}

fun getRequiredPhotoPermissions(): Array<String> {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            )
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
            )
        }

        else -> {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    }
}
