package com.sahi.elingnote.ui.note_feature.notes

import androidx.compose.runtime.Composable
import com.sahi.elingnote.data.model.NoteEntity

data class NotesState(
    val notes: List<NoteEntity> = emptyList()
)