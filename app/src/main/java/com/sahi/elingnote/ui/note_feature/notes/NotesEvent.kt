package com.sahi.elingnote.ui.note_feature.notes

import com.sahi.elingnote.data.model.NoteEntity

sealed class NotesEvent {
    data class DeleteNote(val note:NoteEntity): NotesEvent()
    object RestoreNote: NotesEvent()
}
