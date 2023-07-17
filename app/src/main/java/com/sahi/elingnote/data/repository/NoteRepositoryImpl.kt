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

    override suspend fun getNoteById(id: Int): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    override suspend fun addNote(note: NoteEntity) {
        noteDao.addNote(note)
    }

    override suspend fun deleteTrashNotes() {
        noteDao.deleteTrashNotes()
    }
}