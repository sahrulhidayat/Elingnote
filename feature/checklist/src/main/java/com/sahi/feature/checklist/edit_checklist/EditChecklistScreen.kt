package com.sahi.feature.checklist.edit_checklist

import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.collectAsState
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
import com.sahi.core.notifications.ui.SetAlarmDialog
import com.sahi.core.ui.components.EditChecklistItem
import com.sahi.core.ui.components.EditModeTopAppBar
import com.sahi.core.ui.components.LifecycleObserver
import com.sahi.core.ui.components.TransparentHintTextField
import com.sahi.core.ui.theme.itemColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditChecklistRoute(
    checklistColor: Int,
    modifier: Modifier = Modifier,
    viewModel: EditChecklistViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val checklistId = viewModel.currentChecklistId
    val titleState by viewModel.checklistTitle
    val itemsState by viewModel.itemsFlow.collectAsState()
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

    EditChecklistScreen(
        titleState = titleState,
        itemsState = itemsState,
        checklistColor = checklistColor,
        onEvent = viewModel::onEvent,
        itemEvent = viewModel::itemEvent,
        modifier = modifier,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditChecklistScreen(
    titleState: EditChecklistState,
    itemsState: List<ChecklistItemState>,
    checklistColor: Int,
    onEvent: (EditChecklistEvent) -> Unit,
    itemEvent: (ChecklistItemEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val checklistColorAnimatable = remember { Animatable(Color(checklistColor)) }
    val scope = rememberCoroutineScope()
    var showColorSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val showSetAlarmDialog = rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = checklistColorAnimatable.value,
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
                onBack = onBack
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = {
                            showColorSheet = true
                        }
                    ) {
                        Icon(Icons.Default.FormatColorFill, contentDescription = "Background color")
                    }
                },
                contentPadding = PaddingValues(4.dp)
            )
        }
    ) { padding ->
        val listState = rememberLazyListState()
        if (listState.isScrollInProgress) focusManager.clearFocus()
        LazyColumn(
            state = listState,
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                TransparentHintTextField(
                    modifier = Modifier.padding(16.dp),
                    text = titleState.title,
                    hint = titleState.hint,
                    onValueChange = {
                        onEvent(EditChecklistEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        onEvent(EditChecklistEvent.ChangeTitleFocus(it))
                    },
                    isHintVisible = titleState.isHintVisible,
                    textStyle = MaterialTheme.typography.titleMedium,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) },
                )
            }
            itemsIndexed(itemsState) { index, item ->
                EditChecklistItem(
                    modifier = Modifier.padding(end = 16.dp),
                    checked = item.checked,
                    isHintVisible = item.isHintVisible,
                    isFocused = item.isFocused,
                    label = item.label,
                    hint = item.hint,
                    onCheckedChange = {
                        itemEvent(
                            ChecklistItemEvent.ChangeChecked(
                                index,
                                it
                            )
                        )
                    },
                    onValueChange = { itemEvent(ChecklistItemEvent.EnteredLabel(index, it)) },
                    onFocusChange = {
                        itemEvent(
                            ChecklistItemEvent.ChangeLabelFocus(
                                index,
                                it
                            )
                        )
                    },
                    onAdd = { itemEvent(ChecklistItemEvent.AddItem) },
                    onDelete = { itemEvent(ChecklistItemEvent.DeleteItem(index)) }
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
                    if (
                        itemsState.lastOrNull()?.label?.isNotEmpty() == true
                        || itemsState.isEmpty()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add checklist item")
                        Text(
                            text = "New item",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(250.dp))
            }
        }

        val itemLabels = itemsState.map { it.label }
        val labelsString: String = itemLabels.joinToString("\n")
        SetAlarmDialog(
            showDialog = showSetAlarmDialog,
            onSetAlarm = { onEvent(EditChecklistEvent.SetAlarm) }
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
                                            checklistColorAnimatable.animateTo(
                                                targetValue = color,
                                                animationSpec = tween(
                                                    durationMillis = 300,
                                                    easing = FastOutLinearInEasing
                                                )
                                            )
                                        }
                                        onEvent(EditChecklistEvent.ChangeColor(color.toArgb()))
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