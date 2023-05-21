package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.ui.focus.FocusState

sealed class ChecklistItemEvent {
    data class EnteredLabel(val index: Int, val label: String) : ChecklistItemEvent()
    data class ChangeLabelFocus(val index: Int, val focusState: FocusState) : ChecklistItemEvent()
    data class ChangeChecked(val index: Int, val checked: Boolean) : ChecklistItemEvent()
    data class DeleteItem(val index: Int) : ChecklistItemEvent()
    object AddItem : ChecklistItemEvent()
}