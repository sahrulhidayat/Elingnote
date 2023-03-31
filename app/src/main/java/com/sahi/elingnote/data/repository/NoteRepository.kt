package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<NoteEntity>>

    fun getNoteById(id: Int): Flow<NoteEntity>

    suspend fun insertNote(note: NoteEntity)

    suspend fun deleteNote(note: NoteEntity)
}