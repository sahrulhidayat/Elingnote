package com.sahi.feature.note.notes

import android.os.Build
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sahi.core.model.Entity.Note
import com.sahi.core.ui.components.MainTopAppBar
import com.sahi.core.ui.components.EmptyStateAnimation
import com.sahi.core.ui.components.NoteCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Collections

@Composable
fun NotesRoute(
    drawerState: DrawerState,
    onClickItem: (Note) -> Unit,
    onClickFab: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = koinViewModel(),
) {
    val notesState by viewModel.state.collectAsStateWithLifecycle()
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
                            viewModel.onEvent(NotesEvent.RestoreNotes)
                        }

                        SnackbarResult.Dismissed -> {
                            viewModel.recentlyDeletedNotes.clear()
                        }
                    }
                }
            }
        }
    }

    NotesScreen(
        notesState = notesState,
        drawerState = drawerState,
        snackBarHostState = snackBarHostState,
        selectedIndexes = selectedIndexes,
        onEvent = viewModel::onEvent,
        onClickItem = onClickItem,
        onClickFab = onClickFab,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notesState: NotesState,
    drawerState: DrawerState,
    snackBarHostState: SnackbarHostState,
    selectedIndexes: SnapshotStateList<Boolean>,
    onEvent: (NotesEvent) -> Unit,
    onClickItem: (Note) -> Unit,
    onClickFab: () -> Unit,
    modifier: Modifier = Modifier,
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

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                        imageVector = Icons.Default.NoteAdd,
                        contentDescription = "New note"
                    )
                }
            )
        },
        topBar = {
            MainTopAppBar(
                title = "My Notes",
                actions = {
                    if (isSelectMode) {
                        IconButton(
                            onClick = {
                                val notes = mutableListOf<Note>()
                                selectedIndexes.forEachIndexed { index, item ->
                                    if (item) {
                                        notes.add(notesState.notes[index])
                                    }
                                }
                                onEvent(NotesEvent.DeleteNotes(notes))
                                resetSelected()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                isSelectMode = isSelectMode,
                selectedIndexes = selectedIndexes,
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
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
                            if (isSelectMode)
                                selectedIndexes[index] = !selectedIndexes[index]
                            else
                                onClickItem(note)
                        },
                        onLongClick = {
                            isSelectMode = true
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