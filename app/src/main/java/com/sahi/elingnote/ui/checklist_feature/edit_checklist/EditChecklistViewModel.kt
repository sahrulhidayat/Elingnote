package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.repository.ChecklistRepository
import com.sahi.elingnote.ui.checklist_feature.checklist_item.ChecklistItemEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditChecklistViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _checklistTitle = mutableStateOf(
        EditChecklistState(hint = "Enter checklist title")
    )
    val checklistTitle: State<EditChecklistState> = _checklistTitle


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentChecklistId: Int? = null

    var items = mutableStateListOf<ChecklistItem>()
        private set
    var itemState = MutableStateFlow(
        ChecklistItem(
            checklistId = currentChecklistId ?: 0,
            label = "",
            checked = false
        )
    )
        private set

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
                            items.add(item)
                        }
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
                    _eventFlow.emit(UiEvent.SaveChecklist)
                }
            }

        }
    }

    fun itemEvent(event: ChecklistItemEvent) {
        when (event) {
            is ChecklistItemEvent.ChangeChecked -> {
                itemState.update {
                    it.copy(
                        checked = event.checked
                    )
                }
            }

            is ChecklistItemEvent.ChangeLabelFocus -> {

            }

            is ChecklistItemEvent.EnteredLabel -> {
                itemState.update {
                    it.copy(
                        label = event.label
                    )
                }
            }

            is ChecklistItemEvent.DeleteItem -> {
                viewModelScope.launch {
                    checklistRepository.deleteChecklistItem(event.item)
                }
                items.remove(event.item)
            }

            is ChecklistItemEvent.AddItem -> {
                val item = ChecklistItem(
                    checklistId = currentChecklistId ?: 0,
                    label = itemState.value.label,
                    checked = itemState.value.checked
                )

                viewModelScope.launch {
                    checklistRepository.addChecklistItem(item)
                }

                itemState.update {
                    it.copy(
                        checklistId = currentChecklistId ?: 0,
                        label = "",
                        checked = false
                    )
                }

                items.add(item)
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveChecklist : UiEvent()
    }
}
