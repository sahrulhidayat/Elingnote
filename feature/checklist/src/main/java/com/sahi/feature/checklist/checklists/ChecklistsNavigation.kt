package com.sahi.feature.checklist.checklists

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sahi.core.database.model.Checklist

const val checklistsNavigationRoute = "checklists_route"

fun NavController.navigateToChecklists(navOptions: NavOptions? = null) {
    this.navigate(checklistsNavigationRoute, navOptions)
}

fun NavGraphBuilder.checklistsScreen(
    onClickItem: (com.sahi.core.database.model.Checklist) -> Unit,
    onClickFab: () -> Unit
) {
    composable(route = checklistsNavigationRoute) {
        ChecklistsRoute(
            onClickItem = onClickItem,
            onClickFab = onClickFab
        )
    }
}