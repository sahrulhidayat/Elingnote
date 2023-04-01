package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.compose.ui.focus.FocusState

sealed class EditNoteEvent {
    data class EnteredTitle(val value: String): EditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): EditNoteEvent()
    data class EnteredContent(val value: String): EditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): EditNoteEvent()
    object SaveNote: EditNoteEvent()
}
