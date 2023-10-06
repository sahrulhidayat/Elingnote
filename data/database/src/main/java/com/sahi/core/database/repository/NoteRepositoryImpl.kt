package com.sahi.core.database.repository

import com.sahi.core.model.entity.Note
import com.sahi.core.database.NoteDao
import com.sahi.usecase.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }

    override fun getScheduledNotes(defaultTime: Long): List<Note> {
        return noteDao.getScheduledNotes(defaultTime)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }
    override suspend fun addNote(note: Note): Long {
        return noteDao.addNote(note)
    }
    override suspend fun deleteTrashNotes() {
        noteDao.deleteTrashNotes()
    }
    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}