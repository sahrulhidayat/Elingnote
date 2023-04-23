package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.ui.component.TransparentHintTextField

@Composable
fun EditChecklistScreen(
    viewModel: EditChecklistViewModel = hiltViewModel(),
    onSaveChecklist: () -> Unit
) {
    val titleState = viewModel.checklistTitle.value
    val contentState = viewModel.checklistContent.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TransparentHintTextField(
            text = titleState.text,
            hint = titleState.hint,
            onValueChange = {
                viewModel.onEvent(EditChecklistEvent.EnteredTitle(it))
            },
            onFocusChange = {
                viewModel.onEvent(EditChecklistEvent.ChangeTitleFocus(it))
            },
            isHintVisible = true,
            textStyle = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (contentState.content.isNullOrEmpty()) {
            Text(
                text = "Add item",
                color = Color.DarkGray
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn() {
                    items(contentState.content.toList()) { checklist ->
                        ChecklistItem(
                            checked = checklist.checked,
                            onCheckedChange = { !checklist.checked },
                            onDeleteClick = { viewModel.onEvent(EditChecklistEvent.DeleteChecklistItem(checklist)) },
                            checklistItem = checklist
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ChecklistItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    checklistItem: ChecklistItem
) {
    Row(modifier = modifier) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = checklistItem::label.name)
        IconButton(
            onClick = onDeleteClick,
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete item"
            )
        }
    }
}