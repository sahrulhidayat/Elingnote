package com.sahi.elingnote.ui.note_feature.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val notesNavigationRoute = "notes_route"

fun NavController.navigateToNotes(navOptions: NavOptions? = null) {
    this.navigate(notesNavigationRoute, navOptions)
}

fun NavGraphBuilder.notesScreen(onClickItem: (Int?) -> Unit) {
    composable(route = notesNavigationRoute) {
        NotesRoute(onClickItem)
    }
}