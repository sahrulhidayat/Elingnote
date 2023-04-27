package com.sahi.elingnote.ui.checklist_feature.checklists

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val checklistsNavigationRoute = "checklists_route"

fun NavController.navigateToChecklists(navOptions: NavOptions? = null) {
    this.navigate(checklistsNavigationRoute, navOptions)
}

fun NavGraphBuilder.checklistsScreen(onClickItem: (Int?) -> Unit) {
    composable(route = checklistsNavigationRoute) {
        ChecklistsRoute(onClickItem)
    }
}