package com.sahi.feature.checklist.checklists

import androidx.compose.material3.DrawerState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sahi.core.model.entity.Checklist

const val checklistsNavigationRoute = "checklists_route"

fun NavController.navigateToChecklists(navOptions: NavOptions? = null) {
    this.navigate(checklistsNavigationRoute, navOptions)
}

fun NavGraphBuilder.checklistsScreen(
    drawerState: DrawerState,
    onClickItem: (Checklist) -> Unit,
    onClickFab: () -> Unit
) {
    composable(route = checklistsNavigationRoute) {
        ChecklistsRoute(
            drawerState = drawerState,
            onClickItem = onClickItem,
            onClickFab = onClickFab
        )
    }
}