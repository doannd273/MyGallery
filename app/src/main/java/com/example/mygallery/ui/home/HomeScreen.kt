package com.example.mygallery.ui.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mygallery.R
import com.example.mygallery.model.Image
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mygallery.ui.theme.MyGalleryTheme

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    selectedImages: List<Image>,
    goToGallery: () -> Unit,
) {
    HomeScreen(
        modifier = modifier,
        selectedImages = selectedImages,
        goToGallery = goToGallery,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedImages: List<Image>,
    goToGallery: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.home_title),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Button(
                        onClick = goToGallery,
                    ) {
                        Text(text = stringResource(R.string.home_action_open_gallery))
                    }
                }

                HorizontalDivider()
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            if (selectedImages.isEmpty()) {
                Text(
                    text = stringResource(R.string.home_selected_images_empty),
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = selectedImages,
                        key = { image -> image.id },
                    ) { image ->
                        AsyncImage(
                            model = image.uri,
                            contentDescription = image.displayName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    MyGalleryTheme {
        HomeScreen(
            selectedImages = homePreviewImages(),
            goToGallery = {},
        )
    }
}

@Composable
private fun homePreviewImages(): List<Image> {
    val packageName = LocalContext.current.packageName
    val previewDrawables = listOf(
        R.drawable.ic_background,
        R.drawable.ic_background_2,
        R.drawable.ic_background_3,
    )

    return previewDrawables.mapIndexed { index, drawableRes ->
        Image(
            id = index.toLong(),
            displayName = "Selected image $index",
            uri = Uri.parse("android.resource://$packageName/$drawableRes"),
            sizeBytes = 256_000L + index * 32_000L,
            mimeType = "image/jpeg",
        )
    }
}
