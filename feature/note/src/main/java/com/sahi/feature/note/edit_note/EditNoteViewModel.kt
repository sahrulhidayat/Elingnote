package com.sahi.feature.note.edit_note

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.core.database.repository.NoteRepository
import com.sahi.core.model.Entity.Note
import com.sahi.core.ui.theme.itemColors
import kotlinx.coroutines.flow.MutableSharedFlow
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
    var noteTitle = mutableStateOf(EditNoteState(hint = "Note title"))
        private set
    var noteContent = mutableStateOf(EditNoteState(hint = "Note content"))
        private set
    private var noteColor = mutableIntStateOf(itemColors[0].toArgb())

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var currentNoteId: Int? = null

    private var initialNote = Note(
        id = 0,
        title = "",
        content = "",
        timestamp = 0L,
        color = 0,
        isTrash = false
    )

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

                        initialNote = Note(
                            id = note.id,
                            title = note.title,
                            content = note.content,
                            timestamp = note.timestamp,
                            color = note.color,
                            isTrash = note.isTrash
                        )
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
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is EditNoteEvent.EnteredContent -> {
                noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is EditNoteEvent.ChangeContentFocus -> {
                noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }

            is EditNoteEvent.ChangeColor -> {
                noteColor.intValue = event.color
            }

            is EditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    val finalNote = Note(
                        id = currentNoteId,
                        title = noteTitle.value.text,
                        content = noteContent.value.text,
                        timestamp = System.currentTimeMillis(),
                        color = noteColor.intValue
                    )
                    if (finalNote.title.isNotBlank() || finalNote.content.isNotBlank()) {
                        noteRepository.addNote(finalNote)
                    }
                    if (initialNote.title != finalNote.title || initialNote.content != finalNote.content) {
                        eventFlow.emit(UiEvent.ShowToast(message = "Note saved"))
                    }
                }
            }

            is EditNoteEvent.SetAlarm -> {
                viewModelScope.launch {
                    eventFlow.emit(UiEvent.ShowToast(message = "Alarm has been set"))
                }
            }
        }
    }
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
}

sealed class EditNoteEvent {
    data class EnteredTitle(val value: String) : EditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : EditNoteEvent()
    data class EnteredContent(val value: String) : EditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : EditNoteEvent()
    data class ChangeColor(val color: Int) : EditNoteEvent()
    data object SaveNote : EditNoteEvent()
    data object SetAlarm : EditNoteEvent()
}