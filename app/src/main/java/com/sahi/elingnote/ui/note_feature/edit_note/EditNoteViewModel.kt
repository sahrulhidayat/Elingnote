package com.sahi.elingnote.ui.note_feature.edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sahi.elingnote.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _noteTitle = mutableStateOf(
        EditNoteState(hint = "Note title")
    )
    val noteTitle: State<EditNoteState> = _noteTitle

    private val _noteContent = mutableStateOf(
        EditNoteState(hint = "Type some content..")
    )
    val noteContent: State<EditNoteState> = _noteContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}