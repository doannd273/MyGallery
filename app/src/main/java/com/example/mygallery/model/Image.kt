package com.example.mygallery.model

import android.net.Uri

const val NO_POSITION = -1
const val DEFAULT_POSITION = 1

data class Image(
    val id: String,
    val name: String,
    val uri: Uri,
    val isSelected: Boolean = false,
    val positionSelected: Int = NO_POSITION,
)

fun Image.getPositionText(): String {
    return if (this.positionSelected == NO_POSITION) DEFAULT_POSITION.toString() else this.positionSelected.toString()
}