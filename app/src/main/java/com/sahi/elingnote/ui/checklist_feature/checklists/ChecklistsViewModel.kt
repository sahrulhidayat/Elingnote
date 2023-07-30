package com.sahi.elingnote.ui.checklist_feature.checklists

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.data.repository.ChecklistRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ChecklistsState(
    val checklists: List<ChecklistWithItems> = emptyList(),
)

class ChecklistsViewModel (
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _checklistsState = MutableStateFlow(ChecklistsState())
    val checklistsState = _checklistsState.asStateFlow()

    val selectedIndexes = mutableStateListOf(false)

    private var getChecklistsJob: Job? = null

    init {
        getChecklists()
    }

    fun onEvent(event: ChecklistsEvent) {
        when (event) {
            is ChecklistsEvent.DeleteChecklist -> {
                viewModelScope.launch {
                    checklistRepository.addChecklist(event.checklistWithItems.checklist.copy(isTrash = true))
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

sealed class ChecklistsEvent {
    data class DeleteChecklist(val checklistWithItems: ChecklistWithItems) : ChecklistsEvent()
}
