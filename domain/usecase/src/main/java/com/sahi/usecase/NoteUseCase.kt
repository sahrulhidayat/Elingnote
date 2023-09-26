package com.sahi.usecase

import com.sahi.core.model.entity.Note
import com.sahi.usecase.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteUseCaseImpl(private val repository: NoteRepository): NoteUseCase {
    override fun getAllNotes(): Flow<List<Note>> {
        return repository.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return repository.getNoteById(id)
    }

    override suspend fun addOrUpdateNote(note: Note): Long {
        return repository.addNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return repository.deleteNote(note)
    }

    override suspend fun deleteTrashNotes() {
        return repository.deleteTrashNotes()
    }
}

interface NoteUseCase {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addOrUpdateNote(note: Note): Long
    suspend fun deleteNote(note: Note)
    suspend fun deleteTrashNotes()
}