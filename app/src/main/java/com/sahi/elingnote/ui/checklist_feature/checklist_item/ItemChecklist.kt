package com.sahi.elingnote.ui.checklist_feature.checklist_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
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
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.5.dp, color = MaterialTheme.colorScheme.primary),
        ) {
            Box(
                modifier = Modifier
                    .background(
                        if (state.checked)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                    .size(18.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.checked)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = modifier) {
            Text(
                text = state.label,
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = if (state.checked) TextDecoration.LineThrough else TextDecoration.None,
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
                textStyle = if (state.checked)
                                TextStyle(textDecoration = TextDecoration.LineThrough)
                            else
                                TextStyle(textDecoration = TextDecoration.None),
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