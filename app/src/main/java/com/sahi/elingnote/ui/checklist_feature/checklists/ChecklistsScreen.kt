package com.sahi.elingnote.ui.checklist_feature.checklists

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemState
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ItemChecklist
import java.util.Collections

@Composable
fun ChecklistsRoute(
    onClickItem: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistsViewModel = hiltViewModel(),
) {

    val checklistsState by viewModel.checklistsState.collectAsState()
    val selectedIndexes = viewModel.selectedIndexes

    ChecklistsScreen(
        checklistsState = checklistsState,
        selectedIndexes = selectedIndexes,
        onEvent = viewModel::onEvent,
        onClickItem = onClickItem,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistsScreen(
    checklistsState: ChecklistsState,
    selectedIndexes: SnapshotStateList<Boolean>,
    onEvent: (ChecklistsEvent) -> Unit,
    onClickItem: (checklistId: Int?) -> Unit,
    modifier: Modifier = Modifier
) {

    var enterSelectMode by rememberSaveable {
        mutableStateOf(false)
    }

    fun resetSelected() {
        enterSelectMode = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            selectedIndexes.replaceAll { false }
        } else {
            Collections.replaceAll(selectedIndexes, true, false)
        }
    }

    BackHandler(enabled = enterSelectMode) {
        resetSelected()
    }

    if (!selectedIndexes.contains(true)) {
        resetSelected()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (enterSelectMode)
                        IconButton(
                            onClick = { resetSelected() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = "Cancel select"
                            )
                        }
                },
                title = {
                    val selected = selectedIndexes.filter { value -> value }
                    if (enterSelectMode)
                        Text(text = "${selected.size} Selected")
                    else
                        Text("Your Checklists")
                },
                actions = {
                    if (enterSelectMode) {
                        IconButton(
                            onClick = {
                                selectedIndexes.withIndex().forEach { item ->
                                    if (item.value) {
                                        val selected = checklistsState.checklists[item.index]
                                        onEvent(ChecklistsEvent.DeleteChecklist(selected))
                                    }
                                }
                                resetSelected()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding)
        ) {
            item { 
                Spacer(modifier = Modifier.height(4.dp))
            }
            items(checklistsState.checklists) {
                val index = checklistsState.checklists.indexOf(it)
                if (selectedIndexes.size <= checklistsState.checklists.size) {
                    selectedIndexes.add(false)
                } else {
                    selectedIndexes.removeLast()
                }

                ChecklistCard(
                    checklistWithItems = it,
                    isSelected = selectedIndexes[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    onClick = {
                        if (enterSelectMode)
                            selectedIndexes[index] = !selectedIndexes[index]
                        else
                            onClickItem(it.checklist.id)
                    },
                    onLongClick = {
                        enterSelectMode = true
                        selectedIndexes[index] = !selectedIndexes[index]
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChecklistCard(
    checklistWithItems: ChecklistWithItems,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) {
    var itemCount by remember { mutableIntStateOf(0) }
    Card(
        modifier = modifier
            .heightIn(max = 210.dp)
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(10.dp),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = checklistWithItems.checklist.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (checklistWithItems.checklistItems.isNotEmpty()) {
                ChecklistItems(
                    onPlacementComplete = { itemCount = it }
                ) {
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

                val overflowItems: Int = checklistWithItems.checklistItems.size - itemCount

                if (overflowItems > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+ $overflowItems items",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun ChecklistItems(
    modifier: Modifier = Modifier,
    onPlacementComplete: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }

        data class Item(val placeable: Placeable, val yPosition: Int)

        val items = mutableListOf<Item>()
        var yPosition = 0
        for (placeable in placeables) {
            if (yPosition + placeable.height > constraints.maxHeight) break
            items.add(Item(placeable, yPosition))
            yPosition += placeable.height
        }

        layout(
            width = items.maxOf { it.placeable.width },
            height = items.last().let { it.yPosition + it.placeable.height },
        ) {
            items.forEach {
                it.placeable.place(0, it.yPosition)
            }
            onPlacementComplete(items.count())
        }
    }
}
