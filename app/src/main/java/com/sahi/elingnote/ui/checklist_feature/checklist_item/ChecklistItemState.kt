package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.sahi.elingnote.data.model.ChecklistItem

class ChecklistItemState(hint: String, item: ChecklistItem) {

    var hint by mutableStateOf(hint)
    val isHintVisible: Boolean
        get() = label.isBlank()

    var label by mutableStateOf(item.label)
    fun updateLabel(newLabel: String) {
        label = newLabel
    }

    var checked by mutableStateOf(item.checked)
    fun updateChecked(isChecked: Boolean) {
        checked = isChecked
    }

    var checklistId: Int = 0

    companion object {
        val Saver: Saver<ChecklistItemState, *> = listSaver(
            save = { listOf(it.hint, it.label, it.checked, it.checklistId) },
            restore = {
                ChecklistItemState(
                    hint = it[0] as String,
                    item = ChecklistItem(
                        label = it[1] as String,
                        checked = it[2] as Boolean,
                        checklistId = it[3] as Int
                    )
                )
            }
        )
    }
}

@Composable
fun rememberChecklistItemState(hint: String, item: ChecklistItem) : ChecklistItemState =
    rememberSaveable(hint, item, saver = ChecklistItemState.Saver) {
        ChecklistItemState(hint, item)
    }


