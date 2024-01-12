package com.sahi.feature.note.edit_note

import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pointlessapps.rt_editor.model.RichTextValue
import com.pointlessapps.rt_editor.model.Style
import com.pointlessapps.rt_editor.ui.RichTextEditor
import com.pointlessapps.rt_editor.ui.defaultRichTextFieldStyle
import com.sahi.core.notifications.ui.EditDeleteAlarmDialog
import com.sahi.core.notifications.ui.SetAlarmDialog
import com.sahi.core.ui.components.EditModeTopAppBar
import com.sahi.core.ui.components.LifecycleObserver
import com.sahi.core.ui.components.TransparentHintTextField
import com.sahi.core.ui.theme.itemColors
import com.sahi.utils.darkenColor
import com.sahi.utils.simpleDateTimeFormat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditNoteRoute(
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

    val titleState by viewModel.noteTitle
    val contentState by viewModel.noteContent
    val reminderTime by viewModel.reminderTime
    val noteColor by viewModel.noteColor

    EditNoteScreen(
        titleState = titleState,
        contentState = contentState,
        noteColor = noteColor,
        reminderTime = reminderTime,
        onEvent = viewModel::onEvent,
        modifier = modifier,
        onBack = onBack
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    titleState: EditNoteState,
    contentState: RichTextValue,
    noteColor: Int,
    reminderTime: Long,
    onEvent: (EditNoteEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    var backgroundColor = Color(noteColor)
    if (isSystemInDarkTheme()) {
        backgroundColor = Color(darkenColor(noteColor))
    }
    val noteColorAnimatable = remember { Animatable(backgroundColor) }
    SideEffect {
        scope.launch {
            noteColorAnimatable.animateTo(
                targetValue = backgroundColor,
                animationSpec = tween(
                    durationMillis = 100,
                    easing = FastOutLinearInEasing
                )
            )
        }
    }

    var showColorSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val showSetAlarmDialog = rememberSaveable { mutableStateOf(false) }
    val showEditDeleteAlarmDialog = rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val isCurrentSelectionBold = contentState.currentStyles.contains(Style.Bold)
    val isCurrentSelectionItalic = contentState.currentStyles.contains(Style.Italic)
    val isCurrentSelectionUnderlined = contentState.currentStyles.contains(Style.Underline)

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
                        onClick = { showColorSheet = true }
                    ) {
                        Icon(
                            Icons.Default.FormatColorFill,
                            contentDescription = "Background color"
                        )
                    }
                    VerticalDivider()

                    @Composable
                    fun setIconColors(isActive: Boolean) = IconButtonDefaults.iconButtonColors(
                        contentColor = if (isActive) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                    IconButton(
                        onClick = { onEvent(EditNoteEvent.InsertStyle(Style.Bold)) },
                        colors = setIconColors(isActive = isCurrentSelectionBold),
                    ) { Icon(Icons.Default.FormatBold, contentDescription = "Format Bold") }
                    IconButton(
                        onClick = { onEvent(EditNoteEvent.InsertStyle(Style.Italic)) },
                        colors = setIconColors(isActive = isCurrentSelectionItalic),
                    ) { Icon(Icons.Default.FormatItalic, contentDescription = "Format Italic") }
                    IconButton(
                        onClick = { onEvent(EditNoteEvent.InsertStyle(Style.Underline)) },
                        colors = setIconColors(isActive = isCurrentSelectionUnderlined),
                    ) {
                        Icon(
                            Icons.Default.FormatUnderlined,
                            contentDescription = "Format Underlined"
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    if (titleState.timestamp != 0L) {
                        Text(
                            modifier = Modifier.padding(end = 12.dp),
                            text = "Last edited:\n${titleState.timestamp.simpleDateTimeFormat()}",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.labelMedium
                        )
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
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions {
                            focusManager.moveFocus(
                                FocusDirection.Next
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RichTextEditor(
                        modifier = Modifier.background(noteColorAnimatable.value),
                        value = contentState,
                        onValueChange = {
                            onEvent(EditNoteEvent.EnteredContent(it))
                        },
                        textFieldStyle = defaultRichTextFieldStyle().copy(
                            textColor = MaterialTheme.colorScheme.onBackground,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            placeholder = "Note content",
                            placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            cursorColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
                Spacer(modifier = Modifier.height(250.dp))
            }
        }
        SetAlarmDialog(
            showDialog = showSetAlarmDialog,
            onSetAlarm = { alarmDateTime ->
                onEvent(
                    EditNoteEvent.SetReminder(time = alarmDateTime)
                )
            }
        )
        EditDeleteAlarmDialog(
            reminderTime = reminderTime,
            showDialog = showEditDeleteAlarmDialog,
            onEditAlarm = {
                showEditDeleteAlarmDialog.value = false
                showSetAlarmDialog.value = true
            },
            onDeleteAlarm = {
                showEditDeleteAlarmDialog.value = false
                onEvent(EditNoteEvent.DeleteReminder)
            }
        )
        if (showColorSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showColorSheet = false
                },
                sheetState = sheetState,
                shape = CutCornerShape(0.dp),
                scrimColor = Color.Transparent,
                windowInsets = WindowInsets(0, 0, 0, 0),
                dragHandle = {}
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                        .fillMaxWidth()
                ) {
                    LazyRow(
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        items(itemColors) {
                            var color = it
                            if (isSystemInDarkTheme()) {
                                color = Color(darkenColor(it.toArgb()))
                            }

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
                                        onEvent(EditNoteEvent.ChangeColor(it.toArgb()))
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