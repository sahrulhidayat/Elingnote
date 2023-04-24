package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
                is EditNoteViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is EditNoteViewModel.UiEvent.SaveNote -> onSaveNote()
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
    Column(
        modifier = modifier
    ) {
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
            textStyle = MaterialTheme.typography.titleMedium
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
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxHeight()
        )
    }
}