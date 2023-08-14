package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.ui.components.EditChecklistItem
import com.sahi.elingnote.ui.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditChecklistRoute(
    onSaveChecklist: () -> Unit,
    checklistColor: Int,
    modifier: Modifier = Modifier,
    viewModel: EditChecklistViewModel = koinViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val titleState by viewModel.checklistTitle
    val itemsState by viewModel.itemsFlow.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.SaveChecklist -> onSaveChecklist()
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
    BackHandler(enabled = true) {
        onEvent(EditChecklistEvent.SaveChecklist)
    }
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
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { onEvent(EditChecklistEvent.SaveChecklist) }) {
                        Icon(Icons.Outlined.Save, contentDescription = "Save checklist")
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
                    TransparentHintTextField(
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
                    EditChecklistItem(state = item, index = index, itemEvent = itemEvent)
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
                                        checklistColorAnimatable.animateTo(
                                            targetValue = Color(buttonColor),
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = FastOutLinearInEasing
                                            )
                                        )
                                    }
                                    onEvent(EditChecklistEvent.ChangeColor(buttonColor))
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