package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sahi.elingnote.ui.components.TransparentHintTextField

@Composable
fun ItemChecklist(
    modifier: Modifier = Modifier,
    state: ChecklistItemState,
) {
    Row(
        modifier = modifier,
    ) {
        Checkbox(
            checked = state.checked,
            onCheckedChange = {},
        )
        Box(modifier = Modifier.padding(top = 14.dp)) {
            Text(
                text = state.label,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EditItemChecklist(
    modifier: Modifier = Modifier,
    index: Int,
    state: ChecklistItemState,
    itemEvent: (ChecklistItemEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier,
    ) {
        Checkbox(
            checked = state.checked,
            onCheckedChange = {
                itemEvent(ChecklistItemEvent.ChangeChecked(index, it))
            }
        )
        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .weight(1f)
        ) {
            TransparentHintTextField(
                text = state.label,
                hint = state.hint,
                isHintVisible = state.isHintVisible,
                onValueChange = {
                    itemEvent(ChecklistItemEvent.EnteredLabel(index, it))
                },
                onFocusChange = {
                    itemEvent(ChecklistItemEvent.ChangeLabelFocus(index, it))
                },
            )
        }
        if (state.isFocused) {
            IconButton(
                onClick = {
                    focusManager.moveFocus(FocusDirection.Up)
                    itemEvent(ChecklistItemEvent.DeleteItem(index))
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Delete item"
                )
            }
        }
    }
}