package com.sahi.feature.checklist.edit_checklist

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val editChecklistNavigationRoute = "edit_checklist_route"

fun NavController.navigateToEditChecklist(navOptions: NavOptions? = null) {
    this.navigate(editChecklistNavigationRoute, navOptions)
}

fun NavGraphBuilder.editChecklistScreen(onBack: () -> Unit) {
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
        route = "$editChecklistNavigationRoute?checklistId={checklistId}",
        arguments = listOf(
            navArgument(name = "checklistId") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        EditChecklistRoute(onBack = onBack)
    }
}