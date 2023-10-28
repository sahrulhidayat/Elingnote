package com.sahi.feature.note.notes

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val notesNavigationRoute = "notes_route"

fun NavController.navigateToNotes(navOptions: NavOptions? = null) {
    this.navigate(notesNavigationRoute, navOptions)
}

fun NavGraphBuilder.notesScreen(
    drawerState: DrawerState,
    snackBarHostState: SnackbarHostState,
    onClickItem: (id: Int) -> Unit
) {
    composable(route = notesNavigationRoute) {
        NotesRoute(
            drawerState = drawerState,
            snackBarHostState = snackBarHostState,
            onClickItem = onClickItem,
        )
    }
}