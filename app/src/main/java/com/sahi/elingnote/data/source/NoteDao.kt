package com.sahi.elingnote.data.source

import androidx.room.*
import com.sahi.elingnote.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM note")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query(value = "SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity?

    @Upsert
    suspend fun addNote(note: NoteEntity)

    @Query(value = "DELETE FROM note WHERE isTrash")
    suspend fun deleteTrashNotes()
}