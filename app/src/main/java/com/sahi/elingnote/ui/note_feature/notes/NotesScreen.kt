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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sahi.elingnote.ui.theme.ElingNoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(state.notes) { note ->
                // TODO: Implement note list 
            }
        }
    }
}

@Composable
fun NoteCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column() {
            Text(text = "Note title")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Note content")
        }
    }
}

@Preview
@Composable
fun PreviewLightTheme() {

    val navController = rememberNavController()

    ElingNoteTheme(darkTheme = false) {
        NoteScreen(navController)
    }
}

@Preview
@Composable
fun PreviewDarkTheme() {

    val navController = rememberNavController()

    ElingNoteTheme(darkTheme = true) {
        NoteScreen(navController)
    }
}