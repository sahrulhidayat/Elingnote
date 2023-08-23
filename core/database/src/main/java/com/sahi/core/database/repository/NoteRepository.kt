package com.sahi.core.database.repository

import com.sahi.core.database.model.Note
import com.sahi.core.database.NoteDao
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<com.sahi.core.database.model.Note>>
    suspend fun getNoteById(id: Int): com.sahi.core.database.model.Note?
    suspend fun addNote(note: com.sahi.core.database.model.Note)
    suspend fun deleteTrashNotes()
}

class NoteRepositoryImpl(
    private val noteDao: com.sahi.core.database.NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<com.sahi.core.database.model.Note>> {
        return noteDao.getNotes()
    }
    override suspend fun getNoteById(id: Int): com.sahi.core.database.model.Note? {
        return noteDao.getNoteById(id)
    }
    override suspend fun addNote(note: com.sahi.core.database.model.Note) {
        noteDao.addNote(note)
    }
    override suspend fun deleteTrashNotes() {
        noteDao.deleteTrashNotes()
    }
}