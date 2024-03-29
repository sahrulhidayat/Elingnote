package com.sahi.feature.note.edit_note

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pointlessapps.rt_editor.model.RichTextValue
import com.pointlessapps.rt_editor.model.Style
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
    val timestamp: Long = 0L
)

class EditNoteViewModel(
    private val noteUseCase: NoteUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var noteTitle = mutableStateOf(EditNoteState(hint = "Note title"))
        private set
    var noteContent = mutableStateOf(RichTextValue.get())
        private set
    var noteColor = mutableIntStateOf(itemColors[0].toArgb())
        private set
    var reminderTime = mutableLongStateOf(0L)
        private set

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var currentNoteId: Int = 0
    private lateinit var initialNote: Note

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCase.getNoteById(noteId)?.also { note ->
                        currentNoteId = noteId
                        noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = note.title.isBlank(),
                            timestamp = note.timestamp
                        )
                        noteContent.value = RichTextValue.fromSnapshot(note.content)
                        noteColor.intValue = note.color
                        reminderTime.longValue = note.reminderTime
                        initialNote = note.copy(timestamp = 0L)
                    }
                }
            } else {
                viewModelScope.launch {
                    val note = Note(
                        title = noteTitle.value.text,
                        content = noteContent.value.getLastSnapshot(),
                        timestamp = System.currentTimeMillis(),
                        color = noteColor.intValue
                    )
                    noteUseCase.addOrUpdateNote(note).also { noteId ->
                        currentNoteId = noteId.toInt()
                    }
                    initialNote = note
                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {
        val notificationId: Int = "1$currentNoteId".toInt()
        val note = Note(
            id = currentNoteId,
            title = noteTitle.value.text,
            content = noteContent.value.getLastSnapshot(),
            timestamp = 0L,
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
                noteContent.value = event.value
            }

            is EditNoteEvent.InsertStyle -> {
                noteContent.value = noteContent.value.insertStyle(event.style)
            }

            is EditNoteEvent.ChangeColor -> {
                noteColor.intValue = event.color
            }

            is EditNoteEvent.Undo -> {
                noteContent.value = noteContent.value.undo()
            }

            is EditNoteEvent.Redo -> {
                noteContent.value = noteContent.value.redo()
            }

            is EditNoteEvent.SaveNote -> {
                val notification = Notification(
                    id = notificationId,
                    title = noteTitle.value.text,
                    content = noteContent.value.getLastSnapshot().text,
                    time = reminderTime.longValue
                )
                val title = note.title
                val content = note.content.text
                viewModelScope.launch {
                    when {
                        initialNote == note -> {
                            return@launch
                        }

                        title.isNotBlank() || content.isNotBlank() -> {
                            noteUseCase.addOrUpdateNote(
                                note.copy(
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                            if (reminderTime.longValue > System.currentTimeMillis()) {
                                notificationUseCase.addReminder(notification)
                                notificationScheduler.schedule(notification)
                            }
                        }

                        title.isBlank() && content.isBlank() && notification.time != 0L -> {
                            if (reminderTime.longValue > System.currentTimeMillis()) {
                                noteUseCase.addOrUpdateNote(
                                    note.copy(
                                        title = "Unnamed reminder",
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                                notificationUseCase.addReminder(
                                    notification.copy(title = "Unnamed reminder")
                                )
                                notificationScheduler.schedule(
                                    notification.copy(title = "Unnamed reminder")
                                )
                            }
                        }

                        else -> {
                            noteUseCase.deleteNote(note)
                        }
                    }
                }
            }

            is EditNoteEvent.SetReminder -> {
                val notification = Notification(
                    id = notificationId,
                    title = noteTitle.value.text,
                    content = noteContent.value.getLastSnapshot().text,
                    time = event.time
                )
                viewModelScope.launch {
                    if (event.time > System.currentTimeMillis()) {
                        reminderTime.longValue = event.time
                        notificationUseCase.addReminder(notification)
                        notificationScheduler.schedule(notification)
                        noteUseCase.addOrUpdateNote(
                            note.copy(timestamp = System.currentTimeMillis())
                        )
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
    data class EnteredContent(val value: RichTextValue) : EditNoteEvent()
    data class InsertStyle(val style: Style) : EditNoteEvent()
    data class ChangeColor(val color: Int) : EditNoteEvent()
    data object Undo : EditNoteEvent()
    data object Redo : EditNoteEvent()
    data object SaveNote : EditNoteEvent()
    data class SetReminder(val time: Long) : EditNoteEvent()
    data object DeleteReminder : EditNoteEvent()
}