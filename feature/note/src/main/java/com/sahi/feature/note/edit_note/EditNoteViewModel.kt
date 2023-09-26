package com.sahi.feature.note.edit_note

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.core.model.entity.Note
import com.sahi.core.notifications.NotificationScheduler
import com.sahi.core.ui.theme.itemColors
import com.sahi.usecase.NoteUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

data class EditNoteState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

class EditNoteViewModel(
    private val noteUseCase: NoteUseCase,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var noteTitle = mutableStateOf(EditNoteState(hint = "Note title"))
        private set
    var noteContent = mutableStateOf(EditNoteState(hint = "Note content"))
        private set
    var reminderTime = mutableLongStateOf(0L)
        private set
    private var noteColor = mutableIntStateOf(itemColors[0].toArgb())

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var currentNoteId: Int = 0

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCase.getNoteById(noteId)?.also { note ->
                        currentNoteId = noteId
                        noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = note.title.isBlank()
                        )
                        noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = note.content.isBlank()
                        )
                        reminderTime.longValue = note.reminderTime
                        noteColor.intValue = note.color
                    }
                }
            } else {
                viewModelScope.launch {
                    noteUseCase.addOrUpdateNote(
                        Note(
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            timestamp = System.currentTimeMillis(),
                            color = noteColor.intValue
                        )
                    ).also { noteId ->
                        currentNoteId = noteId.toInt()
                    }
                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {
        val alarmRequestCode = "1$currentNoteId".toInt()
        val note = Note(
            id = currentNoteId,
            title = noteTitle.value.text,
            content = noteContent.value.text,
            timestamp = System.currentTimeMillis(),
            color = noteColor.intValue,
            reminderTime = reminderTime.longValue
        )
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
                    if (note.title.isNotBlank() || note.content.isNotBlank()) {
                        noteUseCase.addOrUpdateNote(note)
                        if (reminderTime.longValue > System.currentTimeMillis()) {
                            notificationScheduler.schedule(
                                requestCode = alarmRequestCode,
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                time = reminderTime.longValue
                            )
                        }
                    } else {
                        noteUseCase.deleteNote(note)
                    }
                }
            }

            is EditNoteEvent.SetAlarm -> {
                reminderTime.longValue = event.dateTime
                viewModelScope.launch {
                    noteUseCase.addOrUpdateNote(note)
                    if (event.dateTime > System.currentTimeMillis()) {
                        notificationScheduler.schedule(
                            requestCode = alarmRequestCode,
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            time = event.dateTime
                        )
                        eventFlow.emit(UiEvent.ShowToast(message = "Reminder has been set"))
                    } else {
                        eventFlow.emit(UiEvent.ShowToast(message = "Reminder time has passed"))
                    }
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
    data class SetAlarm(val dateTime: Long) : EditNoteEvent()
    data object SaveNote : EditNoteEvent()
}