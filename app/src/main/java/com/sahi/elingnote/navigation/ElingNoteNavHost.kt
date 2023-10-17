package com.sahi.elingnote.navigation

import androidx.compose.material3.DrawerState
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
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    startDestination: String = notesNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        notesScreen(
            drawerState = drawerState,
            onClickItem = { note ->
                navController.navigate(
                    "$editNoteNavigationRoute?noteId=${note.id}&noteColor=${note.color}"
                )
            },
            onClickFab = {
                navController.navigateToEditNote()
            }
        )
        editNoteScreen(onBack = { navController.popBackStack() })
        checklistsScreen(
            drawerState = drawerState,
            onClickItem = { checklist ->
                navController.navigate(
                    "$editChecklistNavigationRoute?checklistId=${checklist.id}&checklistColor=${checklist.color}"
                )
            },
            onClickFab = {
                navController.navigateToEditChecklist()
            }
        )
        editChecklistScreen(onBack = { navController.popBackStack() })
        trashScreen(onBack = { navController.popBackStack()})
    }
}