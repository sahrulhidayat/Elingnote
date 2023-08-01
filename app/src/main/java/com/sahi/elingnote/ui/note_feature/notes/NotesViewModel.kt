package com.sahi.elingnote.ui.note_feature.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.data.repository.NoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class NotesState(
    val notes: List<Note> = emptyList()
)

class NotesViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    val selectedIndexes = mutableStateListOf(false)

    private var getNotesJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getNotes()
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteRepository.addNote(event.note.copy(isTrash = true))
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Notes are moved to the trash"
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

                _state.value = state.value.copy(
                    notes = notes
                )
            }
            .launchIn(viewModelScope)
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
}

sealed class NotesEvent {
    data class DeleteNote(val note: Note) : NotesEvent()
}