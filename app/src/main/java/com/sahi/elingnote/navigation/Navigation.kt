package com.sahi.elingnote.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

const val noteRoute = "note_route"

const val checklistRoute = "checklist_route"

fun NavController.navigateToNote(navOptions: NavOptions? = null) {
    this.navigate(noteRoute, navOptions)
}

fun NavController.navigateToChecklist(navOptions: NavOptions? = null) {
    this.navigate(checklistRoute, navOptions)
}