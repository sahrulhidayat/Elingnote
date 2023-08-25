package com.sahi.feature.checklist.edit_checklist

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

fun NavGraphBuilder.editChecklistScreen() {
    composable(
        route = "$editChecklistNavigationRoute?checklistId={checklistId}&checklistColor={checklistColor}",
        arguments = listOf(
            navArgument(name = "checklistId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument(name = "checklistColor") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        val color = it.arguments?.getInt("checklistColor") ?: -1
        EditChecklistRoute(color)
    }
}