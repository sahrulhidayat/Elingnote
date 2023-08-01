package com.sahi.elingnote.ui.checklist_feature.checklists

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.data.repository.ChecklistRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ChecklistsState(
    val checklists: List<ChecklistWithItems> = emptyList(),
)

class ChecklistsViewModel(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _checklistsState = MutableStateFlow(ChecklistsState())
    val checklistsState = _checklistsState.asStateFlow()

    val selectedIndexes = mutableStateListOf(false)

    private var getChecklistsJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getChecklists()
    }

    fun onEvent(event: ChecklistsEvent) {
        when (event) {
            is ChecklistsEvent.DeleteChecklist -> {
                viewModelScope.launch {
                    checklistRepository.addChecklist(event.checklistWithItems.checklist.copy(isTrash = true))
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Items are moved to the trash"
                        )
                    )
                }
            }
        }
    }

    private fun getChecklists() {
        getChecklistsJob?.cancel()
        getChecklistsJob = checklistRepository.getChecklists()
            .onEach { checklists ->
                selectedIndexes.clear()
                while (selectedIndexes.size < checklists.size) {
                    selectedIndexes.add(false)
                }

                _checklistsState.value = checklistsState.value.copy(
                    checklists = checklists
                )
            }
            .launchIn(viewModelScope)
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
}

sealed class ChecklistsEvent {
    data class DeleteChecklist(val checklistWithItems: ChecklistWithItems) : ChecklistsEvent()
}
