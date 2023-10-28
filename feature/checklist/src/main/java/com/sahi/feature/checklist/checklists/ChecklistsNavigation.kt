package com.sahi.feature.checklist.checklists

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val checklistsNavigationRoute = "checklists_route"

fun NavController.navigateToChecklists(navOptions: NavOptions? = null) {
    this.navigate(checklistsNavigationRoute, navOptions)
}

fun NavGraphBuilder.checklistsScreen(
    drawerState: DrawerState,
    snackBarHostState: SnackbarHostState,
    onClickItem: (id: Int) -> Unit
) {
    composable(route = checklistsNavigationRoute) {
        ChecklistsRoute(
            drawerState = drawerState,
            snackBarHostState = snackBarHostState,
            onClickItem = onClickItem,
        )
    }
}