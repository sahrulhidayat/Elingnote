package com.sahi.feature.note.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sahi.elingnote.data.model.Note

const val notesNavigationRoute = "notes_route"

fun NavController.navigateToNotes(navOptions: NavOptions? = null) {
    this.navigate(com.sahi.feature.note.notes.notesNavigationRoute, navOptions)
}

fun NavGraphBuilder.notesScreen(
    onClickItem: (Note) -> Unit,
    onClickFab: () -> Unit
) {
    composable(route = com.sahi.feature.note.notes.notesNavigationRoute) {
        NotesRoute(
            onClickItem = onClickItem,
            onClickFab = onClickFab
        )
    }
}