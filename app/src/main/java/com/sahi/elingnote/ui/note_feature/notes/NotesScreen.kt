package com.sahi.elingnote.ui.note_feature.notes

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.data.model.NoteEntity
import java.util.Collections

@Composable
fun NotesRoute(
    onClickItem: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel(),
) {

    val notesState by viewModel.state.collectAsState()
    val selectedIndexes = viewModel.selectedIndexes

    NotesScreen(
        notesState = notesState,
        selectedIndexes = selectedIndexes,
        onEvent = viewModel::onEvent,
        onClickItem = onClickItem,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notesState: NotesState,
    selectedIndexes: SnapshotStateList<Boolean>,
    onEvent: (NotesEvent) -> Unit,
    onClickItem: (Int?) -> Unit,
    modifier: Modifier = Modifier,
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
                title = {
                    val selected = selectedIndexes.filter { value -> value }
                    if (enterSelectMode)
                        Text(text = "${selected.size} Selected")
                    else
                        Text("Your Notes")
                },
                actions = {
                    if (enterSelectMode) {
                        IconButton(
                            onClick = {
                                selectedIndexes.withIndex().forEach { item ->
                                    if (item.value) {
                                        val selected = notesState.notes[item.index]
                                        onEvent(NotesEvent.DeleteNote(selected))
                                    }
                                }
                                resetSelected()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding),
        ) {
            items(notesState.notes) { note ->
                val index = notesState.notes.indexOf(note)
                if (selectedIndexes.size < notesState.notes.size) {
                    selectedIndexes.add(false)
                }
                NoteCard(
                    note = note,
                    isSelected = selectedIndexes[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    onClick = {
                        if (enterSelectMode)
                            selectedIndexes[index] = !selectedIndexes[index]
                        else
                            onClickItem(note.id)
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
fun NoteCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    note: NoteEntity,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = MaterialTheme.shapes.medium,
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}