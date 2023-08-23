package com.sahi.feature.note.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val notesNavigationRoute = "notes_route"

fun NavController.navigateToNotes(navOptions: NavOptions? = null) {
    this.navigate(notesNavigationRoute, navOptions)
}

fun NavGraphBuilder.notesScreen(
    onClickItem: (com.sahi.core.model.Entity.Note) -> Unit,
    onClickFab: () -> Unit
) {
    composable(route = notesNavigationRoute) {
        NotesRoute(
            onClickItem = onClickItem,
            onClickFab = onClickFab
        )
    }
}