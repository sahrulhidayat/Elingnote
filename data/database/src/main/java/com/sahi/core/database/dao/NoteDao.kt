package com.sahi.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sahi.core.model.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM Note")
    fun getNotes(): Flow<List<Note>>

    @Query(value = "SELECT * FROM Note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Upsert
    suspend fun addNote(note: Note): Long

    @Query(value = "DELETE FROM Note WHERE isTrash")
    suspend fun deleteTrashNotes()

    @Delete
    suspend fun deleteNote(note: Note)
}