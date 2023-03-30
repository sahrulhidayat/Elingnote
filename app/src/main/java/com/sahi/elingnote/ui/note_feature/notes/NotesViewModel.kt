package com.sahi.elingnote.ui.note_feature.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.repository.NoteRepository
import com.sahi.elingnote.ui.note_feature.notes.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    private fun getNotes() {
        noteRepository.getNotes()
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes
                )
            }
            .launchIn(viewModelScope)
    }
}
