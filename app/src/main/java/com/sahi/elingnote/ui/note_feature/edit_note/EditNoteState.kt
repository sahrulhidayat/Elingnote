package com.sahi.elingnote.ui.note_feature.edit_note

data class EditNoteState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)