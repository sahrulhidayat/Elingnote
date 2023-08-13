package com.sahi.elingnote.ui.checklist_feature.checklists

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.data.repository.ChecklistRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ChecklistsState(
    val checklists: List<ChecklistWithItems> = emptyList(),
)

class ChecklistsViewModel(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {
    var checklistsState = MutableStateFlow(ChecklistsState())
        private set
    val selectedIndexes = mutableStateListOf(false)

    var recentlyDeletedChecklists = mutableListOf<Checklist>()

    private var getChecklistsJob: Job? = null
    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    init {
        getChecklists()
    }

    fun onEvent(event: ChecklistsEvent) {
        when (event) {
            is ChecklistsEvent.DeleteChecklist -> {
                viewModelScope.launch {
                    recentlyDeletedChecklists.add(event.checklistWithItems.checklist)
                    checklistRepository.addChecklist(
                        event.checklistWithItems.checklist.copy(isTrash = true)
                    )
                    eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Checklists are moved to the trash",
                            actionLabel = "Undo"
                        )
                    )
                }
            }

            ChecklistsEvent.RestoreChecklist -> {
                viewModelScope.launch {
                    recentlyDeletedChecklists.forEach { checklist ->
                        checklistRepository.addChecklist(checklist)
                    }
                    recentlyDeletedChecklists.clear()
                    eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Checklists restored"
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

                checklistsState.value = checklistsState.value.copy(
                    checklists = checklists
                )
            }
            .launchIn(viewModelScope)
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String, val actionLabel: String? = null) : UiEvent()
}

sealed class ChecklistsEvent {
    data class DeleteChecklist(val checklistWithItems: ChecklistWithItems) : ChecklistsEvent()
    object RestoreChecklist: ChecklistsEvent()
}
