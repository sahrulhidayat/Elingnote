package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.NoteEntity
import com.sahi.elingnote.data.source.NoteDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<NoteEntity>> {
        return noteDao.getNotes()
    }

    override fun getNoteById(id: Int): Flow<NoteEntity> {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }
}