package com.sahi.elingnote.ui.trash_feature

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val trashNavigationRoute = "trash_navigation"

fun NavController.navigateToTrashScreen(navOptions: NavOptions? = null) {
    this.navigate(trashNavigationRoute, navOptions)
}

fun NavGraphBuilder.trashScreen() {
    composable(route = trashNavigationRoute) {
        TrashRoute()
    }
}