package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sahi.elingnote.ui.components.TransparentHintTextField

@Composable
fun ItemChecklist(
    modifier: Modifier = Modifier,
    state: ChecklistItemState,
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier,
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(2.dp),
            colors = if (state.checked) {
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            } else {
                CardDefaults.cardColors(containerColor = Color.Transparent)
            },
            border = if (state.checked) {
                BorderStroke(1.3.dp, color = MaterialTheme.colorScheme.primary)
            } else {
                BorderStroke(1.3.dp, color = Color.Black)
            }
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.checked)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = modifier) {
            Text(
                text = state.label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                ),
                textDecoration = if (state.checked) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 1,
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
    val focusRequester = remember { FocusRequester() }

    SideEffect {
        if (state.isFocused)
            focusRequester.requestFocus()
    }

    Row(
        modifier = modifier,
    ) {
        Checkbox(
            checked = state.checked,
            colors = CheckboxDefaults.colors(
                uncheckedColor = Color.Black
            ),
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
                textStyle =
                if (state.checked)
                    MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.LineThrough
                    )
                else
                    MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.None
                    ),
                isHintVisible = state.isHintVisible,
                onValueChange = {
                    itemEvent(ChecklistItemEvent.EnteredLabel(index, it))
                },
                onFocusChange = {
                    itemEvent(ChecklistItemEvent.ChangeLabelFocus(index, it))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions { itemEvent(ChecklistItemEvent.AddItem) },
                modifier = modifier
                    .focusRequester(focusRequester)
            )
        }
        if (state.isFocused) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black),
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
