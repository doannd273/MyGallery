package com.example.mygallery.ui.gallery

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.mygallery.R
import com.example.mygallery.model.Image
import com.example.mygallery.model.NO_POSITION
import com.example.mygallery.model.getPositionText
import com.example.mygallery.ui.theme.MyGalleryTheme

@Composable
fun GalleryRoute(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    GalleryScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        state = state,
        onContinueClick = {},
        onRemoveImage = {}
    )
}

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    state: GalleryState,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onRemoveImage: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                TopAppBar(
                    onBackClick = onBackClick
                )

                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(
                    items = state.images,
                    key = { it.id },
                ) { item ->
                    GalleryItem(image = item, onItemClick = {

                    })
                }
            }
        }
    }

    ActionBottomSheet(
        imageSelectedList = state.selectedImages,
        onContinueClick = onContinueClick,
        onRemoveImage = onRemoveImage,
    )
}

@Composable
fun GalleryItem(
    image: Image,
    onItemClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(
                width = 3.dp,
                color = if (image.isSelected) Color.Blue else Color.Transparent
            )
            .clickable {
                onItemClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = image.uri,
            contentDescription = "",
            placeholder = painterResource(R.drawable.ic_background_3),
            error = painterResource(R.drawable.ic_background_3),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        if (image.isSelected && image.positionSelected != NO_POSITION) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .align(Alignment.TopEnd),
            ) {
                Text(
                    text = image.getPositionText(),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                )
            }
        } else {
            Icon(
                modifier = Modifier
                    .padding(all = 4.dp)
                    .size(30.dp)
                    .align(Alignment.TopEnd),
                painter = painterResource(id = R.drawable.ic_gallery_selection_circle_24),
                contentDescription = "",
                tint = Color.White,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBottomSheet(
    imageSelectedList: List<Image>,
    onContinueClick: () -> Unit,
    onRemoveImage: () -> Unit,
) {
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .statusBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            }
        ) {
            ActionBottomSheetContent(
                imageSelectedList = imageSelectedList,
                onContinueClick = onContinueClick,
                onRemoveImage = onRemoveImage,
            )
        }
    }
}

@Composable
private fun ActionBottomSheetContent(
    imageSelectedList: List<Image>,
    onContinueClick: () -> Unit,
    onRemoveImage: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = imageSelectedList,
                key = { it.id },
            ) { item ->
                ImageSelectedItem(
                    imageSelected = item,
                    onRemoveImage = onRemoveImage,
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        TextButton(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(5.dp)
                )
                .background(Color.Blue).padding(horizontal = 8.dp),
            onClick = {
                onContinueClick()
            },
        ) {
            Text(
                text = stringResource(R.string.gallery_action_next),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun ImageSelectedItem(
    imageSelected: Image,
    onRemoveImage: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(5.dp)
            )
            .size(80.dp),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = imageSelected.uri,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_gallery_close_24),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
                .align(
                    Alignment.TopEnd
                )
                .clickable {
                    onRemoveImage()
                },
            tint = Color.White
        )
    }
}

@Composable
fun TopAppBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {
                onBackClick()
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gallery_close_24),
                contentDescription = "",
                modifier = Modifier.size(30.dp),
            )
        }

        Text(
            text = stringResource(R.string.gallery_title),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )

        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {

            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gallery_settings_24),
                contentDescription = "",
                modifier = Modifier.size(30.dp),
            )
        }

        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {

            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gallery_camera_24),
                contentDescription = "",
                modifier = Modifier.size(30.dp),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GalleryScreenPreview() {
    MyGalleryTheme {
        GalleryScreen(
            onBackClick = {},
            onContinueClick = {},
            onRemoveImage = {},
            state = galleryScreenPreviewState(),
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 120)
@Composable
private fun ActionBottomSheetPreview() {
    MyGalleryTheme {
        ActionBottomSheetContent(
            imageSelectedList = galleryScreenPreviewState().selectedImages,
            onContinueClick = {},
            onRemoveImage = {},
        )
    }
}

@Composable
private fun galleryScreenPreviewState(): GalleryState {
    val packageName = LocalContext.current.packageName
    val selectedPositions = mapOf(
        0 to 1,
        1 to 2,
        5 to 3,
    )
    val previewDrawables = listOf(
        R.drawable.ic_background,
        R.drawable.ic_background_2,
        R.drawable.ic_background_3,
    )

    val images = List(18) { index ->
        val selectedPosition = selectedPositions[index]
        val drawableRes = previewDrawables[index % previewDrawables.size]

        Image(
            id = "preview-$index",
            name = "Preview image $index",
            uri = Uri.parse("android.resource://$packageName/$drawableRes"),
            isSelected = selectedPosition != null,
            positionSelected = selectedPosition ?: NO_POSITION,
        )
    }

    return GalleryState(
        images = images,
        selectedImages = images.filter { image ->
            image.isSelected && image.positionSelected != NO_POSITION
        },
    )
}

