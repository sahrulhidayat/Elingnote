package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.repository.ChecklistRepository
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemEvent
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditChecklistState(
    val title: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

@HiltViewModel
class EditChecklistViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _checklistTitle = mutableStateOf(
        EditChecklistState(hint = "Enter checklist title")
    )
    val checklistTitle: State<EditChecklistState> = _checklistTitle

    private val items = mutableStateListOf<ChecklistItemState>()

    private val _itemsFlow = MutableStateFlow(items)
    val itemsFlow: StateFlow<List<ChecklistItemState>> = _itemsFlow

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentChecklistId: Int? = null

    init {
        savedStateHandle.get<Int>("checklistId")?.let { checklistId ->
            if (checklistId != -1) {
                viewModelScope.launch {
                    checklistRepository.getChecklistWithItems(checklistId).also {
                        currentChecklistId = checklistId
                        _checklistTitle.value = checklistTitle.value.copy(
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
                _checklistTitle.value = checklistTitle.value.copy(
                    title = event.value
                )
            }

            is EditChecklistEvent.ChangeTitleFocus -> {
                _checklistTitle.value = checklistTitle.value.copy(
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
                        )
                    )

                    itemsFlow.collectLatest { items ->
                        if (items.isEmpty()) {
                            _eventFlow.emit(UiEvent.SaveChecklist)
                        }

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

                        _eventFlow.emit(UiEvent.SaveChecklist)
                    }
                }
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
    object SaveChecklist : EditChecklistEvent()
}
