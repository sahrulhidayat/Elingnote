package com.sahi.core.database.repository

import com.sahi.core.model.Entity.Note
import com.sahi.core.database.NoteDao
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note)
    suspend fun deleteTrashNotes()
}

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }
    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }
    override suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }
    override suspend fun deleteTrashNotes() {
        noteDao.deleteTrashNotes()
    }
}