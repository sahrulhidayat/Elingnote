package com.sahi.elingnote.ui.note_feature.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.data.model.NoteEntity
import com.sahi.elingnote.ui.theme.ElingNoteTheme
import com.sahi.elingnote.util.noteDummy

@Composable
fun NotesRoute(
    onClickItem: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel(),
) {

    val notesState by viewModel.state.collectAsState()

    NoteScreen(
        notesState = notesState,
        onClickItem = onClickItem,
        modifier = modifier
    )
}

@Composable
fun NoteScreen(
    notesState: NotesState,
    onClickItem: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(notesState.notes) { note ->
            NoteCard(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                onClick = { onClickItem(note.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: NoteEntity,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
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
        NoteScreen(
            notesState = NotesState(notes = listOf(noteDummy, noteDummy, noteDummy)),
            onClickItem = {})
    }
}

@Preview(name = "NoteCard preview", widthDp = 360)
@Composable
fun PreviewDarkTheme() {

    ElingNoteTheme(darkTheme = true) {
        NoteScreen(
            notesState = NotesState(notes = listOf(noteDummy, noteDummy, noteDummy)),
            onClickItem = {})
    }
}