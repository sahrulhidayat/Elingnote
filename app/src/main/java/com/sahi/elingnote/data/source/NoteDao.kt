package com.sahi.elingnote.data.source

import androidx.room.*
import com.sahi.elingnote.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query(value = "SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Upsert
    suspend fun addNote(note: Note)

    @Query(value = "DELETE FROM note WHERE isTrash")
    suspend fun deleteTrashNotes()
}