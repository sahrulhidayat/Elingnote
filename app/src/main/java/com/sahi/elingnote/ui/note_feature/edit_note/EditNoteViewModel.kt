package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.NoteEntity
import com.sahi.elingnote.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditNoteState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(
        EditNoteState(hint = "Enter note title")
    )
    val noteTitle: State<EditNoteState> = _noteTitle

    private val _noteContent = mutableStateOf(
        EditNoteState(hint = "Type some content")
    )
    val noteContent: State<EditNoteState> = _noteContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteRepository.getNoteById(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {
        when (event) {
            is EditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is EditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }

            is EditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is EditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }

            is EditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    noteRepository.addNote(
                        NoteEntity(
                            id = currentNoteId,
                            title = noteTitle.value.text.ifBlank { "<New note>" },
                            content = noteContent.value.text.ifBlank { "<Note content>" },
                            timestamp = System.currentTimeMillis(),
                        )
                    )
                    _eventFlow.emit(UiEvent.SaveNote)
                }
            }
        }
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
    object SaveNote : UiEvent()
}

sealed class EditNoteEvent {
    data class EnteredTitle(val value: String) : EditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : EditNoteEvent()
    data class EnteredContent(val value: String) : EditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : EditNoteEvent()
    object SaveNote : EditNoteEvent()
}