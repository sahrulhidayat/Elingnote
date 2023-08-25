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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sahi.core.ui.components.EditChecklistItem
import com.sahi.core.ui.theme.itemColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditChecklistRoute(
    checklistColor: Int,
    modifier: Modifier = Modifier,
    viewModel: EditChecklistViewModel = koinViewModel(),
) {
    val titleState by viewModel.checklistTitle
    val itemsState by viewModel.itemsFlow.collectAsState()
    val context = LocalContext.current

    LifecycleStartEffect(Unit) {
        onStopOrDispose {
            viewModel.onEvent(EditChecklistEvent.SaveChecklist)
        }
    }
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
        modifier = modifier
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
) {
    val focusManager = LocalFocusManager.current

    val checklistColorAnimatable = remember { Animatable(Color(checklistColor)) }
    val scope = rememberCoroutineScope()
    var showColorSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(checklistColorAnimatable.value) {
        systemUiController.setStatusBarColor(
            color = checklistColorAnimatable.value
        )
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(48.dp),
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
                                    color = checklistColorAnimatable.value,
                                    shape = CircleShape
                                )
                                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                        )
                    }
                },
                contentPadding = PaddingValues(4.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(checklistColorAnimatable.value)
        ) {
            item {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    com.sahi.core.ui.components.TransparentHintTextField(
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
            }
            itemsIndexed(itemsState) { index, item ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    EditChecklistItem(
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
            }
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    if (
                        itemsState.lastOrNull()?.label?.isNotEmpty() == true
                        || itemsState.isEmpty()
                    ) {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black),
                            onClick = {
                                itemEvent(ChecklistItemEvent.AddItem)
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add checklist item")
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
        if (showColorSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                shape = CutCornerShape(0.dp),
                containerColor = checklistColorAnimatable.value,
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
                    items(itemColors) { color ->
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
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