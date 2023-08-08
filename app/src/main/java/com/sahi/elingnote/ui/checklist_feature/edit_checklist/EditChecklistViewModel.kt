package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.repository.ChecklistRepository
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemEvent
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class EditChecklistState(
    val title: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

class EditChecklistViewModel(
    private val checklistRepository: ChecklistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var checklistTitle = mutableStateOf(EditChecklistState(hint = "Checklist title"))
        private set

    private val items = mutableStateListOf<ChecklistItemState>()
    var itemsFlow = MutableStateFlow(items)
        private set
    private var checklistColor = mutableIntStateOf(Checklist.checklistColors[0].toArgb())

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var currentChecklistId: Int? = null

    init {
        savedStateHandle.get<Int>("checklistId")?.let { checklistId ->
            if (checklistId != -1) {
                viewModelScope.launch {
                    checklistRepository.getChecklistWithItems(checklistId).also {
                        currentChecklistId = checklistId
                        checklistTitle.value = checklistTitle.value.copy(
                            title = it.checklist.title,
                            isHintVisible = false
                        )

                        it.checklistItems.forEach { item ->
                            items.add(
                                ChecklistItemState(
                                    itemId = item.itemId ?: 0,
                                    label = item.label,
                                    checked = item.checked,
                                    checklistId = item.checklistId
                                )
                            )
                        }
                    }
                }
            } else {
                viewModelScope.launch {
                    checklistRepository.addChecklist(
                        Checklist(
                            title = checklistTitle.value.title.ifBlank { "<New checklist>" },
                            timestamp = System.currentTimeMillis(),
                            color = checklistColor.intValue
                        )
                    ).also { checklistId ->
                        currentChecklistId = checklistId.toInt()
                    }
                }
            }
        }
    }

    fun onEvent(event: EditChecklistEvent) {
        when (event) {
            is EditChecklistEvent.EnteredTitle -> {
                checklistTitle.value = checklistTitle.value.copy(
                    title = event.value
                )
            }

            is EditChecklistEvent.ChangeTitleFocus -> {
                checklistTitle.value = checklistTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            checklistTitle.value.title.isBlank()
                )
            }

            is EditChecklistEvent.SaveChecklist -> {
                viewModelScope.launch {
                    checklistRepository.addChecklist(
                        Checklist(
                            id = currentChecklistId,
                            title = checklistTitle.value.title.ifBlank { "<New checklist>" },
                            timestamp = System.currentTimeMillis(),
                            color = checklistColor.intValue
                        )
                    )

                    itemsFlow.collectLatest { items ->
                        if (items.isNotEmpty()) {
                            items.map {
                                ChecklistItem(
                                    itemId = it.itemId,
                                    checklistId = it.checklistId,
                                    label = it.label,
                                    checked = it.checked
                                )
                            }.forEach {
                                checklistRepository.updateChecklistItem(it)
                            }
                        }
                        eventFlow.emit(UiEvent.SaveChecklist)
                    }
                }
            }

            is EditChecklistEvent.ChangeColor -> {
                checklistColor.intValue = event.color
            }
        }
    }

    fun itemEvent(event: ChecklistItemEvent) {
        when (event) {
            is ChecklistItemEvent.ChangeChecked -> {
                items[event.index] = items[event.index].copy(checked = event.checked)
            }

            is ChecklistItemEvent.ChangeLabelFocus -> {
                items[event.index] = items[event.index]
                    .copy(
                        isHintVisible = !event.focusState.isFocused &&
                                items[event.index].label.isBlank(),
                        isFocused = event.focusState.isFocused
                    )
            }

            is ChecklistItemEvent.EnteredLabel -> {
                items[event.index] = items[event.index].copy(label = event.label)
            }

            is ChecklistItemEvent.DeleteItem -> {
                items.removeAt(event.index).also {
                    viewModelScope.launch {
                        checklistRepository.deleteChecklistItem(
                            ChecklistItem(
                                itemId = it.itemId,
                                checklistId = it.checklistId,
                                label = it.label,
                                checked = it.checked
                            )
                        )
                    }
                }

            }

            is ChecklistItemEvent.AddItem -> {
                viewModelScope.launch {
                    checklistRepository.addChecklistItem(
                        ChecklistItem(
                            checklistId = currentChecklistId ?: 0,
                            label = "",
                            checked = false
                        )
                    ).also { itemId ->
                        items.add(
                            ChecklistItemState(
                                itemId = itemId.toInt(),
                                checklistId = currentChecklistId ?: 0,
                                label = "",
                                checked = false,
                                isFocused = true,
                            )
                        )
                    }
                }

            }
        }
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
    object SaveChecklist : UiEvent()
}

sealed class EditChecklistEvent {
    data class EnteredTitle(val value: String) : EditChecklistEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : EditChecklistEvent()
    data class ChangeColor(val color: Int) : EditChecklistEvent()
    object SaveChecklist : EditChecklistEvent()
}
