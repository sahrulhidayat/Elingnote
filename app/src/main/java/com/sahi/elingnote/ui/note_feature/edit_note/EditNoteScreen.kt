package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahi.elingnote.ui.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditNoteRoute(
    onSaveNote: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditNoteViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.SaveNote -> onSaveNote()
            }
        }
    }

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    EditNoteScreen(
        titleState = titleState,
        contentState = contentState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )

}

@Composable
fun EditNoteScreen(
    titleState: EditNoteState,
    contentState: EditNoteState,
    onEvent: (EditNoteEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    BackHandler(enabled = true) {
        onEvent(EditNoteEvent.SaveNote)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(44.dp),
                actions = {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { onEvent(EditNoteEvent.SaveNote) }) {
                        Icon(Icons.Outlined.Save, contentDescription = "Save note")
                    }
                },
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding)
        ) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    TransparentHintTextField(
                        text = titleState.text,
                        hint = titleState.hint,
                        onValueChange = {
                            onEvent(EditNoteEvent.EnteredTitle(it))
                        },
                        onFocusChange = {
                            onEvent(EditNoteEvent.ChangeTitleFocus(it))
                        },
                        isHintVisible = titleState.isHintVisible,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TransparentHintTextField(
                        text = contentState.text,
                        hint = contentState.hint,
                        onValueChange = {
                            onEvent(EditNoteEvent.EnteredContent(it))
                        },
                        onFocusChange = {
                            onEvent(EditNoteEvent.ChangeContentFocus(it))
                        },
                        isHintVisible = contentState.isHintVisible,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        }
    }
}