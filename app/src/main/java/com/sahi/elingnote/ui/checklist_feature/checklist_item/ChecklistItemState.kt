package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.EditChecklistViewModel

class ChecklistItemState(
    hint: String,
    item: ChecklistItem,
    private val editChecklistViewModel: EditChecklistViewModel
) {

    var hint by mutableStateOf(hint)
    val isHintVisible: Boolean
        @Composable get() = label.isBlank()

    var label by mutableStateOf(item.label)
    var checked by mutableStateOf(item.checked)
    private var checklistId by mutableStateOf(item.checklistId)

    fun updateLabel(newLabel: String, index: Int) {
        label = newLabel
        editChecklistViewModel.itemEvent(ChecklistItemEvent.EnteredLabel(index, newLabel))
    }

    fun updateChecked(checkedValue: Boolean, index: Int) {
        checked = checkedValue
        editChecklistViewModel.itemEvent(ChecklistItemEvent.ChangeChecked(index, checkedValue))
    }

    fun onFocusChange(focusState: FocusState, index: Int) {
        editChecklistViewModel.itemEvent(ChecklistItemEvent.ChangeLabelFocus(index, focusState))
    }

    fun onDeleteItem(item: ChecklistItem) {
        editChecklistViewModel.itemEvent(ChecklistItemEvent.DeleteItem(item))
    }

    val item = mutableStateOf(
        ChecklistItem(
            checklistId = checklistId,
            label = label,
            checked = checked
        )
    )

}

@Composable
fun rememberChecklistItemState(
    hint: String,
    item: ChecklistItem,
    editChecklistViewModel: EditChecklistViewModel = hiltViewModel()
): ChecklistItemState =
    remember {
        ChecklistItemState(
            hint,
            item,
            editChecklistViewModel = editChecklistViewModel
        )
    }


