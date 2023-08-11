package com.sahi.elingnote.ui.note_feature.notes

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.ui.components.ElingNoteTopAppBar
import com.sahi.elingnote.ui.components.EmptyStateAnimation
import com.sahi.elingnote.ui.components.NoteCard
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.util.Collections

@Composable
fun NotesRoute(
    onClickItem: (Note) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = koinViewModel(),
) {
    val notesState by viewModel.state.collectAsState()
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

    NotesScreen(
        notesState = notesState,
        snackBarHostState = snackBarHostState,
        selectedIndexes = selectedIndexes,
        onEvent = viewModel::onEvent,
        onClickItem = onClickItem,
        modifier = modifier
    )
}

@Composable
fun NotesScreen(
    notesState: NotesState,
    snackBarHostState: SnackbarHostState,
    selectedIndexes: SnapshotStateList<Boolean>,
    onEvent: (NotesEvent) -> Unit,
    onClickItem: (Note) -> Unit,
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
                title = "My Notes",
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
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
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
            itemsIndexed(notesState.notes) { index, note ->
                if (!note.isTrash) {
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
                                onClickItem(note)
                        },
                        onLongClick = {
                            enterSelectMode = true
                            selectedIndexes[index] = !selectedIndexes[index]
                        }
                    )
                }
            }
        }
        val items = notesState.notes.filter { !it.isTrash }
        if (items.isEmpty()) {
            EmptyStateAnimation(modifier = Modifier.fillMaxSize())
        }
    }
}