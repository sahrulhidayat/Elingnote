package com.sahi.elingnote.ui.note_feature.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.data.model.NoteEntity
import com.sahi.elingnote.ui.theme.ElingNoteTheme
import com.sahi.elingnote.util.noteDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NotesViewModel = hiltViewModel(),
    onClickItem: (noteId: Int?) -> Unit
) {

    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(state.notes) { note ->
                NoteCard(
                    note = note,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onClickItem(note.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: NoteEntity,
    onClick:() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
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

@Preview(name = "NoteCard preview", widthDp = 360)
@Composable
fun PreviewLightTheme() {

    ElingNoteTheme(darkTheme = false) {
        NoteCard(note = noteDummy) { }
    }
}

@Preview(name = "NoteCard preview", widthDp = 360)
@Composable
fun PreviewDarkTheme() {

    ElingNoteTheme(darkTheme = true) {
        NoteCard(note = noteDummy) { }
    }
}