package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.LifecycleStartEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.ui.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditNoteRoute(
    noteColor: Int,
    modifier: Modifier = Modifier,
    viewModel: EditNoteViewModel = koinViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LifecycleStartEffect(Unit) {
        onStopOrDispose {
            viewModel.onEvent(EditNoteEvent.SaveNote)
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.SaveNote -> { }
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
    val focusManager = LocalFocusManager.current

    val noteColorAnimatable = remember { Animatable(Color(noteColor)) }
    val scope = rememberCoroutineScope()
    var showColorSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(noteColorAnimatable.value) {
        systemUiController.setStatusBarColor(
            color = noteColorAnimatable.value
        )
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(48.dp),
                actions = {
                    IconButton(
                        onClick = {
                            showColorSheet = true
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
                                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
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
                contentPadding = PaddingValues(4.dp),
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(noteColorAnimatable.value),
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
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions {
                            focusManager.moveFocus(
                                FocusDirection.Next
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,
                            lineHeight = 1.2.em,
                        ),
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        }
        if (showColorSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                shape = CutCornerShape(0.dp),
                containerColor = noteColorAnimatable.value,
                onDismissRequest = {
                    showColorSheet = false
                }
            ) {
                LazyRow(
                    modifier = Modifier.padding(bottom = 56.dp)
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
                                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                                .clickable {
                                    scope.launch {
                                        noteColorAnimatable.animateTo(
                                            targetValue = Color(buttonColor),
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = FastOutLinearInEasing
                                            )
                                        )
                                    }
                                    onEvent(EditNoteEvent.ChangeColor(buttonColor))
                                }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}