package com.example.mygallery.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mygallery.ui.theme.MyGalleryTheme

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    goToGallery: () -> Unit,
) {
    HomeScreen(goToGallery = goToGallery)
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    goToGallery: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            Button(
                onClick = {
                    goToGallery()
                },
                modifier = Modifier,
            ) {
                Text(text = "Go To Gallery")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    MyGalleryTheme {
        HomeScreen(goToGallery = {})
    }
}