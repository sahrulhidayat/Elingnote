package com.sahi.feature.checklist.checklists

import android.os.Build
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sahi.core.model.Entity.Checklist
import com.sahi.core.ui.components.ChecklistCard
import com.sahi.core.ui.components.ElingNoteTopAppBar
import com.sahi.core.ui.components.EmptyStateAnimation
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.util.Collections

@Composable
fun ChecklistsRoute(
    onClickItem: (Checklist) -> Unit,
    onClickFab: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistsViewModel = koinViewModel(),
) {
    val checklistsState by viewModel.checklistsState.collectAsStateWithLifecycle()
    val selectedIndexes = viewModel.selectedIndexes
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    val snackBar = snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = SnackbarDuration.Short
                    )
                    when (snackBar) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onEvent(ChecklistsEvent.RestoreChecklist)
                        }

                        SnackbarResult.Dismissed -> {
                            viewModel.recentlyDeletedChecklists.clear()
                        }
                    }
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
        onClickFab = onClickFab,
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
    onClickFab: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isSelectMode by rememberSaveable {
        mutableStateOf(false)
    }

    fun resetSelected() {
        isSelectMode = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            selectedIndexes.replaceAll { false }
        } else {
            Collections.replaceAll(selectedIndexes, true, false)
        }
    }

    val topBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = topBarColor
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickFab,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                content = {
                    Icon(
                        imageVector = Icons.Default.AddTask,
                        contentDescription = "New checklist"
                    )
                }
            )
        },
        topBar = {
            ElingNoteTopAppBar(
                title = "My Checklists",
                actions = {
                    if (isSelectMode) {
                        IconButton(
                            onClick = {
                                val checklists =
                                    mutableListOf<Checklist>()
                                selectedIndexes.forEachIndexed { index, item ->
                                    if (item) {
                                        checklists.add(checklistsState.checklists[index].checklist)
                                    }
                                }
                                onEvent(ChecklistsEvent.DeleteChecklists(checklists))
                                resetSelected()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                },
                isSelectMode = isSelectMode,
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
                            if (isSelectMode)
                                selectedIndexes[index] = !selectedIndexes[index]
                            else
                                onClickItem(checklistWithItems.checklist)
                        },
                        onLongClick = {
                            isSelectMode = true
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
