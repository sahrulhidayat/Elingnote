package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val editNoteNavigationRoute = "edit_note_route"

fun NavGraphBuilder.editNoteScreen(onSaveNote: () -> Unit) {
    composable(
        route = "$editNoteNavigationRoute?noteId={noteId}",
        arguments = listOf(
            navArgument(name = "noteId") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        EditNoteRoute(onSaveNote)
    }
}