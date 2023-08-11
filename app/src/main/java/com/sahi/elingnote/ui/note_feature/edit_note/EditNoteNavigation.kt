package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val editNoteNavigationRoute = "edit_note_route"

fun NavController.navigateToEditNote(navOptions: NavOptions? = null) {
    this.navigate(editNoteNavigationRoute, navOptions)
}

fun NavGraphBuilder.editNoteScreen(onSaveNote: () -> Unit) {
    composable(
        route = "$editNoteNavigationRoute?noteId={noteId}&noteColor={noteColor}",
        arguments = listOf(
            navArgument(name = "noteId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument(name = "noteColor") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        val color = it.arguments?.getInt("noteColor") ?: -1
        EditNoteRoute(onSaveNote, color)
    }
}