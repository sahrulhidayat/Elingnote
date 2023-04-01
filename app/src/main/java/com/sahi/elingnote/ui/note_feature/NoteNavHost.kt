package com.sahi.elingnote.ui.note_feature

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sahi.elingnote.ui.note_feature.edit_note.EditNoteScreen
import com.sahi.elingnote.ui.note_feature.notes.NoteScreen

@Composable
fun NoteNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NoteDestinations.NotesScreen.route
    ) {
        composable(route = NoteDestinations.NotesScreen.route) {
            NoteScreen(
                onClickItem = { noteId ->
                    navController.navigate(
                        NoteDestinations.EditNoteScreen.route + "?noteId=${noteId}"
                    )
                }
            )
        }
        composable(
            route = NoteDestinations.EditNoteScreen.route
                    + "?noteId={noteId}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            EditNoteScreen(
                onSaveNote = {
                    navController.navigateUp()
                }
            )
        }
    }
}