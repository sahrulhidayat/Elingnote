package com.sahi.feature.trash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val trashNavigationRoute = "trash_navigation"

fun NavController.navigateToTrashScreen(navOptions: NavOptions? = null) {
    this.navigate(trashNavigationRoute, navOptions)
}

fun NavGraphBuilder.trashScreen(onBack: () -> Unit) {
    composable(route = trashNavigationRoute) {
        TrashRoute(onBack = onBack)
    }
}