package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EditChecklistScreen(
    onSaveChecklist: () -> Unit
) {

}

@Composable
fun ChecklistItem(
    checked: Boolean,
    checklistItem: String,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    Row(modifier = modifier) {
        Checkbox(
            checked = checked,
            onCheckedChange = { /*TODO*/ }
        )
        Text(text = checklistItem)
        IconButton(
            onClick = {
                // TODO: onDeleteItem click
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete item"
            )
        }
    }
}