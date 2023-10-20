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
import com.sahi.core.model.entity.Notification
import com.sahi.core.notifications.NotificationScheduler
import com.sahi.core.ui.theme.itemColors
import com.sahi.usecase.NoteUseCase
import com.sahi.usecase.NotificationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

data class EditNoteState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

class EditNoteViewModel(
    private val noteUseCase: NoteUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var noteTitle = mutableStateOf(EditNoteState(hint = "Note title"))
        private set
    var noteContent = mutableStateOf(EditNoteState(hint = "Note content"))
        private set
    var noteColor = mutableIntStateOf(itemColors[0].toArgb())
        private set
    var reminderTime = mutableLongStateOf(0L)
        private set

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
                        noteColor.intValue = note.color
                        reminderTime.longValue = note.reminderTime
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
        val notificationId: Int = "1$currentNoteId".toInt()
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
                val notification = Notification(
                    id = notificationId,
                    title = noteTitle.value.text,
                    content = noteContent.value.text,
                    time = reminderTime.longValue
                )
                viewModelScope.launch {
                    if (note.title.isNotBlank() || note.content.isNotBlank()) {
                        noteUseCase.addOrUpdateNote(note)
                        if (reminderTime.longValue > System.currentTimeMillis()) {
                            notificationUseCase.addReminder(notification)
                            notificationScheduler.schedule(notification)
                        }
                    } else {
                        noteUseCase.deleteNote(note)
                    }
                }
            }

            is EditNoteEvent.SetReminder -> {
                val notification = Notification(
                    id = notificationId,
                    title = noteTitle.value.text,
                    content = noteContent.value.text,
                    time = event.time
                )
                viewModelScope.launch {
                    if (event.time > System.currentTimeMillis()) {
                        reminderTime.longValue = event.time
                        notificationUseCase.addReminder(notification)
                        notificationScheduler.schedule(notification)
                        noteUseCase.addOrUpdateNote(note)
                        eventFlow.emit(UiEvent.ShowToast(message = "Reminder has been set"))
                    } else {
                        eventFlow.emit(UiEvent.ShowToast(message = "The time has passed"))
                    }
                }
            }

            is EditNoteEvent.DeleteReminder -> {
                viewModelScope.launch {
                    reminderTime.longValue = 0L
                    notificationUseCase.deleteReminder(notificationId)
                    notificationScheduler.cancel(notificationId)
                    eventFlow.emit(UiEvent.ShowToast(message = "Reminder has been deleted"))
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
    data class SetReminder(val time: Long) : EditNoteEvent()
    data object DeleteReminder : EditNoteEvent()
}