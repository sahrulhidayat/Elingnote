package com.sahi.elingnote.ui.checklist_feature

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sahi.elingnote.data.model.ChecklistEntity

@Composable
fun ChecklistsScreen(

) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistCard(
    checklist: ChecklistEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = checklist.title)
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
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
    }
}