package com.sahi.elingnote.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sahi.elingnote.ui.checklist_feature.checklists.checklistsScreen
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.editChecklistNavigationRoute
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.editChecklistScreen
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.navigateToEditChecklist
import com.sahi.elingnote.ui.note_feature.edit_note.editNoteNavigationRoute
import com.sahi.elingnote.ui.note_feature.edit_note.editNoteScreen
import com.sahi.elingnote.ui.note_feature.edit_note.navigateToEditNote
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
        notesScreen(
            onClickItem = { note ->
                navController.navigate(
                    "$editNoteNavigationRoute?noteId=${note.id}&noteColor=${note.color}"
                )
            },
            onClickFab = {
                navController.navigateToEditNote()
            }
        )
        editNoteScreen(onSaveNote = { navController.navigateUp() })
        checklistsScreen(
            onClickItem = { checklist ->
                navController.navigate(
                    "$editChecklistNavigationRoute?checklistId=${checklist.id}&checklistColor=${checklist.color}"
                )
            },
            onClickFab = {
                navController.navigateToEditChecklist()
            }
        )
        editChecklistScreen(onSaveChecklist = { navController.navigateUp() })
        trashScreen()
    }
}