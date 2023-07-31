package com.sahi.elingnote.ui.trash_feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.data.repository.ChecklistRepository
import com.sahi.elingnote.data.repository.NoteRepository
import kotlinx.coroutines.Job
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
    private val noteRepository: NoteRepository,
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    var state = MutableStateFlow(TrashState())
        private set

    private var getNotesJob: Job? = null
    private var getChecklistsJob: Job? = null

    init {
        getTrashItems()
    }

    fun onEvent(event: TrashEvent) {
        when (event) {
            is TrashEvent.RestoreItem -> {
                viewModelScope.launch {
                    event.note?.let {
                        noteRepository.addNote(
                            it.copy(isTrash = false)
                        )
                    }
                    checklistRepository.addChecklist(
                        event.checklistWithItems?.checklist?.copy(isTrash = false) ?: return@launch
                    )
                }
            }

            is TrashEvent.DeleteAll -> {
                viewModelScope.launch {
                    noteRepository.deleteTrashNotes()
                    checklistRepository.deleteTrashChecklists()
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

sealed class TrashEvent {

    data class RestoreItem(
        val note: Note? = null,
        val checklistWithItems: ChecklistWithItems? = null
    ) : TrashEvent()

    object DeleteAll : TrashEvent()
}
