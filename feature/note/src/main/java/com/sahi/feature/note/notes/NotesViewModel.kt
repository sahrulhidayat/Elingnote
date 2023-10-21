package com.sahi.feature.note.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.core.model.entity.Note
import com.sahi.core.notifications.NotificationScheduler
import com.sahi.usecase.NoteUseCase
import com.sahi.usecase.NotificationUseCase
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
    private val noteUseCase: NoteUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val notificationScheduler: NotificationScheduler,
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
                        noteUseCase.addOrUpdateNote(
                            note.copy(
                                isTrash = true,
                                reminderTime = 0L
                            )
                        )

                        val notificationId = "1${note.id}".toInt()
                        notificationUseCase.deleteReminder(notificationId)
                        notificationScheduler.cancel(notificationId)
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
                        noteUseCase.addOrUpdateNote(note.copy(reminderTime = 0L))
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
        getNotesJob = noteUseCase.getAllNotes()
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
    data object RestoreNotes : NotesEvent()
}