package com.sahi.elingnote.ui.checklist_feature.edit_checklist

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

fun NavGraphBuilder.editChecklistScreen(onSaveChecklist: () -> Unit) {
    composable(
        route = "$editChecklistNavigationRoute?checklistId={checklistId}",
        arguments = listOf(
            navArgument(name = "checklistId") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        EditChecklistRoute(onSaveChecklist)
    }
}