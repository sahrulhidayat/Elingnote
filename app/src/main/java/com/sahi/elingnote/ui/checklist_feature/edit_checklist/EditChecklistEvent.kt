package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.compose.ui.focus.FocusState
import com.sahi.elingnote.data.model.ChecklistItem

sealed class EditChecklistEvent {
    data class EnteredTitle(val value: String) : EditChecklistEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : EditChecklistEvent()
    object SaveChecklist : EditChecklistEvent()
}