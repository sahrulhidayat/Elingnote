package com.sahi.elingnote.ui.checklist_feature.checklists

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChecklistsViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _checklistsState = MutableStateFlow(ChecklistsState())
    val checklistsState = _checklistsState.asStateFlow()

    val itemSelectedIndexes = mutableStateListOf(false)

    private var recentlyDeletedChecklists = mutableListOf<Checklist>()

    private var getChecklistsJob: Job? = null

    init {
        getChecklists()
    }

    fun onEvent(event: ChecklistsEvent) {
        when(event) {
            is ChecklistsEvent.DeleteChecklist -> {
                viewModelScope.launch {
                    checklistRepository.deleteChecklist(event.checklistWithItems.checklist)
                    event.checklistWithItems.checklistItems.forEach {
                        checklistRepository.deleteChecklistItem(it)
                    }
                }
            }
            is ChecklistsEvent.RestoreChecklist -> {

            }
        }
    }

    private fun getChecklists() {
        getChecklistsJob?.cancel()
        getChecklistsJob = checklistRepository.getChecklists()
            .onEach { checklists ->
                _checklistsState.value = checklistsState.value.copy(
                    checklists = checklists
                )
            }
            .launchIn(viewModelScope)
    }
}
