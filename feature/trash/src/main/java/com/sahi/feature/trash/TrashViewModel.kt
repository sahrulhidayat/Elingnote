package com.sahi.feature.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.core.database.model.Checklist
import com.sahi.core.database.model.ChecklistWithItems
import com.sahi.core.database.model.Note
import com.sahi.core.database.repository.ChecklistRepository
import com.sahi.core.database.repository.NoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrashState(
    val trashNotes: List<com.sahi.core.database.model.Note> = emptyList(),
    val trashChecklist: List<com.sahi.core.database.model.ChecklistWithItems> = emptyList()
)

class TrashViewModel(
    private val noteRepository: com.sahi.core.database.repository.NoteRepository,
    private val checklistRepository: com.sahi.core.database.repository.ChecklistRepository
) : ViewModel() {
    var state = MutableStateFlow(TrashState())
        private set
    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var getNotesJob: Job? = null
    private var getChecklistsJob: Job? = null

    init {
        getTrashItems()
    }

    fun onEvent(event: TrashEvent) {
        when (event) {
            is TrashEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteRepository.addNote(
                        event.note.copy(isTrash = false)
                    )
                    eventFlow.emit(UiEvent.ShowSnackBar(message = "Note restored"))
                }
            }

            is TrashEvent.RestoreChecklist -> {
                viewModelScope.launch {
                    checklistRepository.addChecklist(
                        event.checklist.copy(isTrash = false)
                    )
                    eventFlow.emit(UiEvent.ShowSnackBar(message = "Checklist restored"))
                }
            }

            is TrashEvent.DeleteAll -> {
                viewModelScope.launch {
                    noteRepository.deleteTrashNotes()
                    checklistRepository.deleteTrashChecklists()
                    eventFlow.emit(UiEvent.ShowSnackBar(message = "Items are deleted"))
                }
            }
        }
    }

    private fun getTrashItems() {
        getNotesJob?.cancel()
        getChecklistsJob?.cancel()

        getNotesJob = noteRepository.getNotes()
            .onEach { notes ->
                state.update { state ->
                    state.copy(
                        trashNotes = notes.filter {
                            it.isTrash
                        }
                    )
                }
            }
            .launchIn(viewModelScope)

        getChecklistsJob = checklistRepository.getChecklists()
            .onEach { checklists ->
                state.update { state ->
                    state.copy(
                        trashChecklist = checklists.filter {
                            it.checklist.isTrash
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
}

sealed class TrashEvent {
    data class RestoreNote(val note: com.sahi.core.database.model.Note) : TrashEvent()
    data class RestoreChecklist(val checklist: com.sahi.core.database.model.Checklist) : TrashEvent()
    object DeleteAll : TrashEvent()
}
