package com.sahi.feature.note.edit_note

import android.widget.Toast
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
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.sahi.core.notifications.ui.SetAlarmDialog
import com.sahi.core.ui.components.EditModeTopAppBar
import com.sahi.core.ui.components.LifecycleObserver
import com.sahi.core.ui.components.TransparentHintTextField
import com.sahi.core.ui.theme.itemColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditNoteRoute(
    noteColor: Int,
    modifier: Modifier = Modifier,
    viewModel: EditNoteViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current

    LifecycleObserver(
        onStart = { },
        onStop = { viewModel.onEvent(EditNoteEvent.SaveNote) }
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val noteId = viewModel.currentNoteId
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    EditNoteScreen(
        titleState = titleState,
        contentState = contentState,
        noteId = noteId,
        noteColor = noteColor,
        onEvent = viewModel::onEvent,
        modifier = modifier,
        onBack = onBack
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    titleState: EditNoteState,
    contentState: EditNoteState,
    noteId: Int,
    noteColor: Int,
    onEvent: (EditNoteEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val noteColorAnimatable = remember { Animatable(Color(noteColor)) }
    val scope = rememberCoroutineScope()
    var showColorSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val showSetAlarmDialog = rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = noteColorAnimatable.value,
        topBar = {
            EditModeTopAppBar(
                showSetAlarmDialog = showSetAlarmDialog,
                scrollBehavior = scrollBehavior,
                colors = if (noteColorAnimatable.value != Color.White) {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    )
                } else {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    )
                },
                onBack = onBack
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier,
                actions = {
                    IconButton(
                        onClick = {
                            showColorSheet = true
                        }
                    ) {
                        Icon(Icons.Default.FormatColorFill, contentDescription = "Background color")
                    }
                },
                contentPadding = PaddingValues(4.dp),
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize(),
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
                Spacer(modifier = Modifier.height(250.dp))
            }
        }
        SetAlarmDialog(
            title = titleState.text,
            content = contentState.text,
            requestCode = "1$noteId".toInt(),
            showDialog = showSetAlarmDialog,
            onSetAlarm = { onEvent(EditNoteEvent.SetAlarm) }
        )
        if (showColorSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                shape = CutCornerShape(0.dp),
                scrimColor = Color.Transparent,
                windowInsets = WindowInsets(0, 0, 0, 0),
                dragHandle = {},
                onDismissRequest = {
                    showColorSheet = false
                }
            ) {
                Box(modifier = Modifier.padding(bottom = 50.dp)) {
                    LazyRow(
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        items(itemColors) { color ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.onBackground,
                                        CircleShape
                                    )
                                    .clickable {
                                        scope.launch {
                                            noteColorAnimatable.animateTo(
                                                targetValue = Color(color.toArgb()),
                                                animationSpec = tween(
                                                    durationMillis = 300,
                                                    easing = FastOutLinearInEasing
                                                )
                                            )
                                        }
                                        onEvent(EditNoteEvent.ChangeColor(color.toArgb()))
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
}