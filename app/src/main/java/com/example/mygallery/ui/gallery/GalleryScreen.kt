package com.example.mygallery.ui.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mygallery.R
import com.example.mygallery.ui.theme.MyGalleryTheme

@Composable
fun GalleryRoute(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    GalleryScreen(
        onBackClick = onBackClick
    )
}

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
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
            Text(
                text = "Gallery",
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
fun TopAppBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp),
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
            onBackClick = {}
        )
    }
}