package com.sahi.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sahi.core.model.Entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM Note")
    fun getNotes(): Flow<List<Note>>

    @Query(value = "SELECT * FROM Note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Upsert
    suspend fun addNote(note: Note)

    @Query(value = "DELETE FROM Note WHERE isTrash")
    suspend fun deleteTrashNotes()
}