package com.sahi.elingnote.ui.note_feature.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.NoteEntity
import com.sahi.elingnote.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotesState(
    val notes: List<NoteEntity> = emptyList()
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    val selectedIndexes = mutableStateListOf(false)

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteRepository.addNote(event.note.copy(isTrash = true))
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

sealed class NotesEvent {
    data class DeleteNote(val note: NoteEntity) : NotesEvent()
}