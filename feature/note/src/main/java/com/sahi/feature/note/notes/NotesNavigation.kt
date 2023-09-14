package com.sahi.feature.note.notes

import androidx.compose.material3.DrawerState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sahi.core.model.Entity.Note

const val notesNavigationRoute = "notes_route"

fun NavController.navigateToNotes(navOptions: NavOptions? = null) {
    this.navigate(notesNavigationRoute, navOptions)
}

fun NavGraphBuilder.notesScreen(
    drawerState: DrawerState,
    onClickItem: (Note) -> Unit,
    onClickFab: () -> Unit
) {
    composable(route = notesNavigationRoute) {
        NotesRoute(
            drawerState = drawerState,
            onClickItem = onClickItem,
            onClickFab = onClickFab
        )
    }
}