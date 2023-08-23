package com.sahi.feature.trash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sahi.core.ui.components.ChecklistCard
import com.sahi.core.ui.components.ElingNoteTopAppBar
import com.sahi.core.ui.components.NoteCard
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrashRoute(
    modifier: Modifier = Modifier,
    viewModel: TrashViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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

    TrashScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun TrashScreen(
    state: TrashState,
    snackBarHostState: SnackbarHostState,
    onEvent: (TrashEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            title = { Text(text = "Confirm delete") },
            text = { Text(text = "Are you sure want to delete all items in the trash?") },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(TrashEvent.DeleteAll)
                    showDeleteDialog = false
                }) {
                    Text(text = "Delete All")
                }
            },
            onDismissRequest = { showDeleteDialog = false },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            com.sahi.core.ui.components.ElingNoteTopAppBar(
                title = "Trash",
                actions = {
                    if (state.trashNotes.isNotEmpty() || state.trashChecklist.isNotEmpty())
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = "Delete all items"
                            )
                        }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Notes :",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (state.trashNotes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "- Empty -",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            items(state.trashNotes) { note ->
                com.sahi.core.ui.components.NoteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    note = note,
                    onRestore = {
                        onEvent(
                            TrashEvent.RestoreNote(note = note)
                        )
                    }
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Checklists :",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (state.trashChecklist.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "- Empty -",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            items(state.trashChecklist) { checklistWithItems ->
                com.sahi.core.ui.components.ChecklistCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    checklistWithItems = checklistWithItems,
                    onRestore = {
                        onEvent(
                            TrashEvent.RestoreChecklist(checklist = checklistWithItems.checklist)
                        )
                    }
                )
            }
        }
    }
}