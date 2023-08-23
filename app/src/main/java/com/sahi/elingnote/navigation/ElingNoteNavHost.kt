package com.sahi.elingnote.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sahi.feature.checklist.checklists.checklistsScreen
import com.sahi.feature.checklist.edit_checklist.editChecklistNavigationRoute
import com.sahi.feature.checklist.edit_checklist.editChecklistScreen
import com.sahi.feature.checklist.edit_checklist.navigateToEditChecklist
import com.sahi.feature.note.edit_note.editNoteNavigationRoute
import com.sahi.feature.note.edit_note.editNoteScreen
import com.sahi.feature.note.edit_note.navigateToEditNote
import com.sahi.feature.note.notes.notesNavigationRoute
import com.sahi.feature.note.notes.notesScreen
import com.sahi.feature.trash.trashScreen

@Composable
fun ElingNoteNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = com.sahi.feature.note.notes.notesNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        notesScreen(
            onClickItem = { note ->
                navController.navigate(
                    "${com.sahi.feature.note.edit_note.editNoteNavigationRoute}?noteId=${note.id}&noteColor=${note.color}"
                )
            },
            onClickFab = {
                navController.navigateToEditNote()
            }
        )
        editNoteScreen()
        checklistsScreen(
            onClickItem = { checklist ->
                navController.navigate(
                    "${com.sahi.feature.checklist.edit_checklist.editChecklistNavigationRoute}?checklistId=${checklist.id}&checklistColor=${checklist.color}"
                )
            },
            onClickFab = {
                navController.navigateToEditChecklist()
            }
        )
        editChecklistScreen()
        trashScreen()
    }
}