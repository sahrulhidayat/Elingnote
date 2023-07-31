package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.data.source.NoteDao
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