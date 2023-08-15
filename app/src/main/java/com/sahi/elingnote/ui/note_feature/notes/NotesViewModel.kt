package com.sahi.elingnote.ui.note_feature.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.data.repository.NoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class NotesState(
    val notes: List<Note> = emptyList()
)

class NotesViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    var state = MutableStateFlow(NotesState())
        private set
    val selectedIndexes = mutableStateListOf(false)

    var recentlyDeletedNotes = mutableListOf<Note>()

    private var getNotesJob: Job? = null
    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    init {
        getNotes()
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNotes -> {
                viewModelScope.launch {
                    recentlyDeletedNotes.clear()
                    event.notes.forEach { note ->
                        recentlyDeletedNotes.add(note)
                        noteRepository.addNote(note.copy(isTrash = true))
                    }
                    eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Notes are moved to the trash",
                            actionLabel = "Undo"
                        )
                    )
                }
            }

            NotesEvent.RestoreNotes -> {
                viewModelScope.launch {
                    recentlyDeletedNotes.forEach { note ->
                        noteRepository.addNote(note)
                    }
                    recentlyDeletedNotes.clear()
                    eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Notes restored"
                        )
                    )
                }
            }
        }
    }

    private fun getNotes() {
        getNotesJob?.cancel()
        getNotesJob = noteRepository.getNotes()
            .onEach { notes ->
                selectedIndexes.clear()
                while (selectedIndexes.size < notes.size) {
                    selectedIndexes.add(false)
                }

                state.value = state.value.copy(
                    notes = notes
                )
            }
            .launchIn(viewModelScope)
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String, val actionLabel: String? = null) : UiEvent()
}

sealed class NotesEvent {
    data class DeleteNotes(val notes: List<Note>) : NotesEvent()
    object RestoreNotes : NotesEvent()
}