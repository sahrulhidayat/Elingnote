package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemEvent
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemState
import com.sahi.elingnote.ui.checklist_feature.checklist_item.EditItemChecklist
import com.sahi.elingnote.ui.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditChecklistRoute(
    onSaveChecklist: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditChecklistViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val titleState by viewModel.checklistTitle
    val itemsState by viewModel.itemsFlow.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.SaveChecklist -> onSaveChecklist()
            }
        }
    }

    EditChecklistScreen(
        titleState = titleState,
        itemsState = itemsState,
        onEvent = viewModel::onEvent,
        itemEvent = viewModel::itemEvent,
        modifier = modifier
    )
}

@Composable
fun EditChecklistScreen(
    titleState: EditChecklistState,
    itemsState: List<ChecklistItemState>,
    onEvent: (EditChecklistEvent) -> Unit,
    itemEvent: (ChecklistItemEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(enabled = true) {
        onEvent(EditChecklistEvent.SaveChecklist)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(44.dp),
                actions = {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { onEvent(EditChecklistEvent.SaveChecklist) }) {
                        Icon(Icons.Outlined.Save, contentDescription = "Save checklist")
                    }
                },
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding)
        ) {
            item {
                TransparentHintTextField(
                    text = titleState.title,
                    hint = titleState.hint,
                    onValueChange = {
                        onEvent(EditChecklistEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        onEvent(EditChecklistEvent.ChangeTitleFocus(it))
                    },
                    isHintVisible = titleState.isHintVisible,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                )
            }
            itemsIndexed(itemsState) { index, item ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    EditItemChecklist(state = item, index = index, itemEvent = itemEvent)
                }
            }
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    if (
                        itemsState.lastOrNull()?.label?.isNotEmpty() == true
                        || itemsState.isEmpty()
                    ) {
                        IconButton(
                            onClick = {
                                itemEvent(ChecklistItemEvent.AddItem)
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add checklist item")
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}