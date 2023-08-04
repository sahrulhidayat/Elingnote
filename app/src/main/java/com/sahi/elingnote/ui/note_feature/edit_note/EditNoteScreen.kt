package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.ui.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditNoteRoute(
    onSaveNote: () -> Unit,
    noteColor: Int,
    modifier: Modifier = Modifier,
    viewModel: EditNoteViewModel = koinViewModel(),
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
        noteColor = noteColor,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    titleState: EditNoteState,
    contentState: EditNoteState,
    noteColor: Int,
    onEvent: (EditNoteEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(true) {
        onEvent(EditNoteEvent.SaveNote)
    }
    val focusManager = LocalFocusManager.current
    val noteColorAnimatable = remember { Animatable(Color(noteColor)) }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    items(Note.noteColors) { color ->
                        val buttonColor = color.toArgb()
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(2.dp, Color.White, CircleShape)
                                .clickable {
                                    scope.launch {
                                        noteColorAnimatable.animateTo(
                                            targetValue = Color(buttonColor)
                                        )
                                    }
                                    onEvent(EditNoteEvent.ChangeColor(buttonColor))
                                }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        },
        sheetShape = CutCornerShape(0.dp),
        sheetPeekHeight = 0.dp,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(noteColorAnimatable.value)
        ) {
            LazyColumn(
                modifier = modifier
                    .padding(padding)
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
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                color = Color.Black
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(
                                    FocusDirection.Next
                                )
                            },
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
                                color = Color.Black,
                                lineHeight = 1.2.em,
                            ),
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
            }
            BottomAppBar(
                modifier = Modifier
                    .height(48.dp)
                    .align(Alignment.BottomCenter),
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (!sheetState.isVisible)
                                    sheetState.expand()
                                else
                                    sheetState.hide()
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .background(
                                    color = noteColorAnimatable.value,
                                    shape = CircleShape
                                )
                                .border(2.dp, Color.Black, CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            onEvent(EditNoteEvent.SaveNote)
                        }
                    ) {
                        Icon(Icons.Outlined.Save, contentDescription = "Save note")
                    }
                },
                contentPadding = PaddingValues(4.dp)
            )
        }
    }
}