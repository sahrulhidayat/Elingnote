package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.ChecklistEntity
import com.sahi.elingnote.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _checklistContent = mutableStateOf(
        EditChecklistState(hint = "Add checklist")
    )
    val checklistContent: State<EditChecklistState> = _checklistContent

    private var currentChecklistId: Int? = null

    init {
        savedStateHandle.get<Int>("checklistId")?.let { checklistId ->
            if (checklistId != -1) {
                viewModelScope.launch {
                    checklistRepository.getChecklistById(checklistId)?.also { checklist ->
                        currentChecklistId = checklist.id
                        _checklistTitle.value = checklistTitle.value.copy(
                            text = checklist.title,
                            isHintVisible = false
                        )
                        _checklistContent.value = checklistContent.value.copy(
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: EditChecklistEvent) {
        when (event) {
            is EditChecklistEvent.EnteredTitle -> {
                _checklistTitle.value = checklistTitle.value.copy(
                    text = event.value
                )
            }

            is EditChecklistEvent.ChangeTitleFocus -> {
                _checklistTitle.value = checklistTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            checklistTitle.value.text.isBlank()
                )
            }

            is EditChecklistEvent.ChangeContentFocus -> {
                _checklistContent.value = checklistContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            checklistContent.value.text.isBlank()
                )
            }

            is EditChecklistEvent.AddChecklistItem -> {
                _checklistContent.value.content?.add(event.item)
            }

            is EditChecklistEvent.DeleteChecklistItem -> {
                _checklistContent.value.content?.remove(event.item)
            }

            is EditChecklistEvent.SaveChecklist -> {
                viewModelScope.launch {
                    checklistRepository.addChecklist(
                        ChecklistEntity(
                            id = currentChecklistId,
                            title = checklistTitle.value.text.ifBlank { "<New checklist>" },
                            content = checklistContent.value.content,
                            timestamp = System.currentTimeMillis(),
                        )
                    )
                }
            }
        }
    }
}
