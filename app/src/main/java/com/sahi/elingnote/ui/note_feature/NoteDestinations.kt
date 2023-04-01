package com.sahi.elingnote.ui.note_feature

sealed class NoteDestinations(val route: String) {
    object NotesScreen: NoteDestinations(route = "notes_screen")
    object EditNoteScreen: NoteDestinations(route = "edit_note_screen")
}
