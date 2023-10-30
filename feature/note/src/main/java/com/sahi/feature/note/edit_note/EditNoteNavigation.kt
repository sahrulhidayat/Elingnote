package com.sahi.feature.note.edit_note

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val editNoteNavigationRoute = "edit_note_route"

fun NavController.navigateToEditNote(navOptions: NavOptions? = null) {
    this.navigate(editNoteNavigationRoute, navOptions)
}

fun NavGraphBuilder.editNoteScreen(onBack: () -> Unit) {
    composable(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            )
        },
        route = "$editNoteNavigationRoute?noteId={noteId}",
        arguments = listOf(
            navArgument(name = "noteId") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        EditNoteRoute(onBack = onBack)
    }
}