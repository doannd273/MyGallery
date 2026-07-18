package com.example.mygallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mygallery.model.Image
import com.example.mygallery.ui.gallery.GalleryRoute
import com.example.mygallery.ui.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

@Serializable
data object GalleryDestination

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var selectedImages by remember {
        mutableStateOf<List<Image>>(emptyList())
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeDestination
    ) {
        composable<HomeDestination> {
            HomeRoute(
                selectedImages = selectedImages,
                goToGallery = {
                    navController.navigate(GalleryDestination)
                }
            )
        }

        composable<GalleryDestination> {
            GalleryRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onContinueClick = { images ->
                    selectedImages = images
                    navController.popBackStack()
                }
            )
        }
    }
}
