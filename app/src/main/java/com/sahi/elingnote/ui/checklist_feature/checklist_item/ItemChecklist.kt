package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.ui.components.TransparentHintTextField

@Composable
fun ItemChecklist(
    modifier: Modifier = Modifier,
    item: ChecklistItem
) {
    val state = rememberChecklistItemState(hint = "New item", item)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.checked,
            onCheckedChange = {

            }
        )
        Text(
            text = state.label,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EditItemChecklist(
    modifier: Modifier = Modifier,
    index: Int,
    item: ChecklistItem
) {

    val state = rememberChecklistItemState(hint = "New item", item)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.checked,
            onCheckedChange = {
                state.updateChecked(it, index)
            }
        )
        TransparentHintTextField(
            text = state.label,
            hint = state.hint,
            onValueChange = {
                state.updateLabel(it, index)
            },
            onFocusChange = {
                state.onFocusChange(it, index)
            },
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                state.onDeleteItem(state.item.value)
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Delete item"
            )
        }
    }
}