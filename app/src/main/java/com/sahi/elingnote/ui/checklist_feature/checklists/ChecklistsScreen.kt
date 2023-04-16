package com.sahi.elingnote.ui.checklist_feature.checklists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.data.model.ChecklistEntity

@Composable
fun ChecklistsScreen(
    viewModel: ChecklistsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(modifier = modifier) {
        items(state.checklists) { checklist ->
            ChecklistCard(
                checklist = checklist,
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO */ },
            )
        }
    }
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
) {
    Row(modifier = modifier) {
        Checkbox(
            checked = checked,
            onCheckedChange = { /*TODO*/ }
        )
        Text(text = checklistItem)
    }
}