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
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItem

@Composable
fun ChecklistsRoute(
    onClickItem: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistsViewModel = hiltViewModel(),
) {

    val checklistsState by viewModel.state.collectAsState()

    ChecklistsScreen(
        checklistsState = checklistsState,
        onEvent = viewModel::onEvent,
        onClickItem = onClickItem,
        modifier = modifier,
    )
}

@Composable
fun ChecklistsScreen(
    checklistsState: ChecklistsState,
    onEvent: (ChecklistsEvent) -> Unit,
    onClickItem: (checklistId: Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(checklistsState.checklists) {
            ChecklistCard(
                checklist = it,
                onCheckedItem = { item, checked ->
                    onEvent(
                        ChecklistsEvent.ChangeItemChecked(
                            it.checklistItem[item.checklistId].itemId,
                            checked
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClickItem(it.checklist.id) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistCard(
    checklist: ChecklistWithItem,
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
            Text(text = checklist.checklist.title)
            Spacer(modifier = Modifier.height(8.dp))
            checklist.checklistItem.onEach { item ->
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