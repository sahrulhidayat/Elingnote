package com.sahi.elingnote.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sahi.elingnote.ui.checklist_feature.checklists.checklistsScreen
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.editChecklistNavigationRoute
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.editChecklistScreen
import com.sahi.elingnote.ui.note_feature.edit_note.editNoteNavigationRoute
import com.sahi.elingnote.ui.note_feature.edit_note.editNoteScreen
import com.sahi.elingnote.ui.note_feature.notes.notesNavigationRoute
import com.sahi.elingnote.ui.note_feature.notes.notesScreen
import com.sahi.elingnote.ui.trash_feature.trashScreen

@Composable
fun ElingNoteNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = notesNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        notesScreen { noteId ->
            navController.navigate(
                "$editNoteNavigationRoute?noteId=${noteId}"
            )
        }
        editNoteScreen(onSaveNote = { navController.navigateUp() })
        checklistsScreen { checklistId ->
            navController.navigate(
                "$editChecklistNavigationRoute?checklistId=${checklistId}"
            )
        }
        editChecklistScreen(onSaveChecklist = { navController.navigateUp() })
        trashScreen()
    }
}