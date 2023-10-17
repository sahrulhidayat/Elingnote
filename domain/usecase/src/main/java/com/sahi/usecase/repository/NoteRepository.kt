package com.sahi.usecase.repository

import com.sahi.core.model.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note): Long
    suspend fun deleteTrashNotes()
    suspend fun deleteNote(note: Note)
}