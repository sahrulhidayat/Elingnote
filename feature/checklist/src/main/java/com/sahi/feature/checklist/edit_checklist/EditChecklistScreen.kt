package com.sahi.feature.checklist.edit_checklist

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.Alignment
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sahi.core.notifications.ui.EditDeleteAlarmDialog
import com.sahi.core.notifications.ui.SetAlarmDialog
import com.sahi.core.ui.components.EditChecklistItem
import com.sahi.core.ui.components.EditModeTopAppBar
import com.sahi.core.ui.components.LifecycleObserver
import com.sahi.core.ui.components.ReminderLabel
import com.sahi.core.ui.components.TransparentHintTextField
import com.sahi.core.ui.theme.itemColors
import com.sahi.utils.darkenColor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditChecklistRoute(
    modifier: Modifier = Modifier,
    viewModel: EditChecklistViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current

    LifecycleObserver(
        onStart = { },
        onStop = { viewModel.onEvent(EditChecklistEvent.SaveChecklist) }
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

    val checklistState by viewModel.checklist
    val itemsState by viewModel.itemsFlow.collectAsStateWithLifecycle()
    val reminderTime by viewModel.reminderTime
    val checklistColor by viewModel.checklistColor

    EditChecklistScreen(
        checklistState = checklistState,
        itemsState = itemsState,
        checklistColor = checklistColor,
        reminderTime = reminderTime,
        onEvent = viewModel::onEvent,
        itemEvent = viewModel::itemEvent,
        modifier = modifier,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditChecklistScreen(
    checklistState: EditChecklistState,
    itemsState: List<ChecklistItemState>,
    checklistColor: Int,
    reminderTime: Long,
    onEvent: (EditChecklistEvent) -> Unit,
    itemEvent: (ChecklistItemEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    var backgroundColor = Color(checklistColor)
    if (isSystemInDarkTheme()) {
        backgroundColor = Color(darkenColor(checklistColor))
    }
    val checklistColorAnimatable = remember { Animatable(backgroundColor) }
    SideEffect {
        scope.launch {
            checklistColorAnimatable.animateTo(
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = checklistColorAnimatable.value,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            EditModeTopAppBar(
                showSetAlarmDialog = showSetAlarmDialog,
                scrollBehavior = scrollBehavior,
                colors = if (checklistColorAnimatable.value != Color.White) {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    )
                } else {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    )
                },
                timeStamp = checklistState.timestamp,
                onBack = onBack
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(
                    if (!WindowInsets.isImeVisible) 48.dp else 0.dp
                )
            ) {}
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = modifier.weight(1f)
            ) {
                item {
                    TransparentHintTextField(
                        modifier = Modifier.padding(16.dp),
                        text = checklistState.title,
                        hint = checklistState.hint,
                        onValueChange = {
                            onEvent(EditChecklistEvent.EnteredTitle(it))
                        },
                        onFocusChange = {
                            onEvent(EditChecklistEvent.ChangeTitleFocus(it))
                        },
                        isHintVisible = checklistState.isHintVisible,
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) },
                    )
                }
                itemsIndexed(itemsState) { index, item ->
                    EditChecklistItem(
                        modifier = Modifier.padding(end = 16.dp),
                        checked = item.checked,
                        isFocused = item.isFocused,
                        label = item.label,
                        onCheckedChange = {
                            itemEvent(ChecklistItemEvent.ChangeChecked(index, it))
                        },
                        onValueChange = { itemEvent(ChecklistItemEvent.EnteredLabel(index, it)) },
                        onFocusChange = {
                            itemEvent(ChecklistItemEvent.ChangeLabelFocus(index, it))
                        },
                        onDelete = { itemEvent(ChecklistItemEvent.DeleteItem(index)) },
                        onAdd = { itemEvent(ChecklistItemEvent.AddItem) },
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                itemEvent(ChecklistItemEvent.AddItem)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val lastItemLabel = itemsState.lastOrNull()?.label
                        if (
                            lastItemLabel?.isNotEmpty() == true
                            || itemsState.isEmpty()
                        ) {
                            Icon(
                                Icons.Default.Add,
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = "Add checklist item"
                            )
                            Text(
                                text = "New item",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }
                }
                item {
                    if (reminderTime != 0L) {
                        ReminderLabel(
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    showEditDeleteAlarmDialog.value = true
                                },
                            reminderTime = reminderTime
                        )
                    }
                    Spacer(modifier = Modifier.height(250.dp))
                }
            }
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        showColorSheet = true
                    }
                ) {
                    Icon(
                        Icons.Default.FormatColorFill,
                        contentDescription = "Background color",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }
        SetAlarmDialog(
            showDialog = showSetAlarmDialog,
            onSetAlarm = { alarmDateTime ->
                onEvent(
                    EditChecklistEvent.SetReminder(time = alarmDateTime)
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
                onEvent(EditChecklistEvent.DeleteReminder)
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
                                            checklistColorAnimatable.animateTo(
                                                targetValue = color,
                                                animationSpec = tween(
                                                    durationMillis = 300,
                                                    easing = FastOutLinearInEasing
                                                )
                                            )
                                        }
                                        onEvent(EditChecklistEvent.ChangeColor(it.toArgb()))
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