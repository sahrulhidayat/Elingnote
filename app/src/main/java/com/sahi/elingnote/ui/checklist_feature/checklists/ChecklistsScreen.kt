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
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemState
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ItemChecklist

@Composable
fun ChecklistsRoute(
    onClickItem: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistsViewModel = hiltViewModel(),
) {

    val checklistsState by viewModel.checklistsState.collectAsState()

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
                checklistWithItems = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                onClick = { onClickItem(it.checklist.id) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistCard(
    checklistWithItems: ChecklistWithItems,
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
            Text(
                text = checklistWithItems.checklist.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (checklistWithItems.checklistItems.isNotEmpty()) {
                checklistWithItems.checklistItems.forEach { item ->
                    ItemChecklist(
                        state = ChecklistItemState(
                            checklistId = item.checklistId,
                            label = item.label,
                            checked = item.checked
                        )
                    )
                }
            }
        }
    }
}