package com.example.mygallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    NavHost(
        navController = navController,
        startDestination = HomeDestination
    ) {
        composable<HomeDestination> {
            HomeRoute(
                goToGallery = {
                    navController.navigate(GalleryDestination)
                }
            )
        }

        composable<GalleryDestination> {
            GalleryRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
