package com.sahi.elingnote.ui.note_feature.notes

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.ui.components.ElingNoteTopAppBar
import com.sahi.elingnote.ui.components.EmptyStateAnimation
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    note: Note,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) {
    val isWhiteBackground = Color(note.color) == Color.White
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(note.color)),
        border = when {
            isSelected -> BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary)
            isWhiteBackground -> BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            else -> null
        }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .heightIn(max = 200.dp)
        ) {
            if (note.title.isNotBlank())
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            Spacer(modifier = Modifier.height(4.dp))
            if (note.content.isNotBlank())
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )
        }
    }
}