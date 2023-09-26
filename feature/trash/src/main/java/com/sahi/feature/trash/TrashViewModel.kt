package com.sahi.feature.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistWithItems
import com.sahi.core.model.entity.Note
import com.sahi.usecase.ChecklistUseCase
import com.sahi.usecase.NoteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrashState(
    val trashNotes: List<Note> = emptyList(),
    val trashChecklist: List<ChecklistWithItems> = emptyList()
)

class TrashViewModel(
    private val noteUseCase: NoteUseCase,
    private val checklistUseCase: ChecklistUseCase
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
                    noteUseCase.addOrUpdateNote(
                        event.note.copy(isTrash = false)
                    )
                    eventFlow.emit(UiEvent.ShowSnackBar(message = "Note restored"))
                }
            }

            is TrashEvent.RestoreChecklist -> {
                viewModelScope.launch {
                    checklistUseCase.addOrUpdateChecklist(
                        event.checklist.copy(isTrash = false)
                    )
                    eventFlow.emit(UiEvent.ShowSnackBar(message = "Checklist restored"))
                }
            }

            is TrashEvent.DeleteAll -> {
                viewModelScope.launch {
                    noteUseCase.deleteTrashNotes()
                    checklistUseCase.deleteTrashChecklists()
                    eventFlow.emit(UiEvent.ShowSnackBar(message = "Items are deleted"))
                }
            }
        }
    }

    private fun getTrashItems() {
        getNotesJob?.cancel()
        getChecklistsJob?.cancel()

        getNotesJob = noteUseCase.getAllNotes()
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

        getChecklistsJob = checklistUseCase.getAllChecklists()
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
    data class RestoreNote(val note: Note) : TrashEvent()
    data class RestoreChecklist(val checklist: Checklist) : TrashEvent()
    data object DeleteAll : TrashEvent()
}
