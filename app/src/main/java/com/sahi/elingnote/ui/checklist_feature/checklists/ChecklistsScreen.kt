package com.sahi.elingnote.ui.checklist_feature.checklists

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemState
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ItemChecklist
import com.sahi.elingnote.ui.components.ElingNoteTopAppBar
import com.sahi.elingnote.ui.components.EmptyStateAnimation
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.util.Collections

@Composable
fun ChecklistsRoute(
    onClickItem: (Checklist) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistsViewModel = koinViewModel(),
) {
    val checklistsState by viewModel.checklistsState.collectAsState()
    val selectedIndexes = viewModel.selectedIndexes
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    ChecklistsScreen(
        checklistsState = checklistsState,
        snackBarHostState = snackBarHostState,
        selectedIndexes = selectedIndexes,
        onEvent = viewModel::onEvent,
        onClickItem = onClickItem,
        modifier = modifier,
    )
}

@Composable
fun ChecklistsScreen(
    checklistsState: ChecklistsState,
    snackBarHostState: SnackbarHostState,
    selectedIndexes: SnapshotStateList<Boolean>,
    onEvent: (ChecklistsEvent) -> Unit,
    onClickItem: (Checklist) -> Unit,
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

    val topBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = topBarColor,
            darkIcons = useDarkIcons
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            ElingNoteTopAppBar(
                title = "My Checklists",
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
                enterSelectMode = enterSelectMode,
                selectedIndexes = selectedIndexes,
                onResetSelect = { resetSelected() }
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            itemsIndexed(checklistsState.checklists) { index, checklistWithItems ->
                if (!checklistWithItems.checklist.isTrash) {
                    ChecklistCard(
                        checklistWithItems = checklistWithItems,
                        isSelected = selectedIndexes[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        onClick = {
                            if (enterSelectMode)
                                selectedIndexes[index] = !selectedIndexes[index]
                            else
                                onClickItem(checklistWithItems.checklist)
                        },
                        onLongClick = {
                            enterSelectMode = true
                            selectedIndexes[index] = !selectedIndexes[index]
                        }
                    )
                }
            }
        }
        val items = checklistsState.checklists.filter { !it.checklist.isTrash }
        if (items.isEmpty()) {
            EmptyStateAnimation(modifier = Modifier.fillMaxSize())
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
    val checklistColor = checklistWithItems.checklist.color
    var containerModifier = modifier
        .background(
            color = Color(checklistColor),
            shape = RoundedCornerShape(10.dp)
        )
        .heightIn(max = 210.dp)
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )

    if (isSelected)
        containerModifier = containerModifier.then(
            Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
        )

    Box(
        modifier = containerModifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val title = checklistWithItems.checklist.title
            if (title.isNotBlank()) {
                Text(
                    text = checklistWithItems.checklist.title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
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
