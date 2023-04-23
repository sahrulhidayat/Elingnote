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
import com.sahi.elingnote.data.model.ChecklistItem

@Composable
fun ChecklistsScreen(
    modifier: Modifier = Modifier,
    viewModel: ChecklistsViewModel = hiltViewModel(),
    onClickItem: (checklistId: Int?) -> Unit
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(modifier = modifier) {
        items(state.checklists) { checklist ->
            ChecklistCard(
                checklist = checklist,
                onCheckedItem = { item, checked ->
                    viewModel.onEvent(
                        ChecklistsEvent.ChangeItemChecked(
                            checklist.id ?: return@ChecklistCard,
                            item,
                            checked
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClickItem(checklist.id) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistCard(
    checklist: ChecklistEntity,
    onCheckedItem: (ChecklistItem, Boolean) -> Unit,
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
            checklist.content?.onEach { item ->
                ChecklistItem(
                    checked = item.checked,
                    checklistItem = item,
                    onCheckedChange = { checked -> onCheckedItem(item, checked) }
                )
            }
        }
    }
}

@Composable
fun ChecklistItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    checklistItem: ChecklistItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(modifier = modifier) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = checklistItem::label.name)
    }
}