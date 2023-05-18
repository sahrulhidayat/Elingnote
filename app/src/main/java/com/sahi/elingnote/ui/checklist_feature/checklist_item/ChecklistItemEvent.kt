package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.ui.focus.FocusState
import com.sahi.elingnote.data.model.ChecklistItem

sealed class ChecklistItemEvent {
    data class EnteredLabel(val label: String) : ChecklistItemEvent()
    data class ChangeLabelFocus(val focusState: FocusState) : ChecklistItemEvent()
    data class ChangeChecked(val checked: Boolean) : ChecklistItemEvent()
    data class DeleteItem(val item: ChecklistItem) : ChecklistItemEvent()
    object AddItem : ChecklistItemEvent()
}