package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class EditNoteState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

class EditNoteViewModel(
    private val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var noteTitle = mutableStateOf(EditNoteState(hint = "Enter note title"))
        private set
    var noteContent = mutableStateOf(EditNoteState(hint = "Type some content"))
        private set
    private var noteColor = mutableIntStateOf(Note.noteColors[0].toArgb())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteRepository.getNoteById(noteId)?.also { note ->
                        currentNoteId = note.id
                        noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = note.title.isBlank()
                        )
                        noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = note.content.isBlank()
                        )
                        noteColor.intValue = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {
        when (event) {
            is EditNoteEvent.EnteredTitle -> {
                noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is EditNoteEvent.ChangeTitleFocus -> {
                noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }

            is EditNoteEvent.EnteredContent -> {
                noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is EditNoteEvent.ChangeContentFocus -> {
                noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }

            is EditNoteEvent.ChangeColor -> {
                noteColor.intValue = event.color
            }

            is EditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    if (noteTitle.value.text.isNotBlank() || noteContent.value.text.isNotBlank())
                        noteRepository.addNote(
                            Note(
                                id = currentNoteId,
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.intValue
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
    data class ChangeColor(val color: Int) : EditNoteEvent()
    object SaveNote : EditNoteEvent()
}