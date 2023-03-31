package com.sahi.elingnote.data.source

import androidx.room.*
import com.sahi.elingnote.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query(value = "SELECT * FROM notes WHERE id = :noteId")
    fun getNoteById(noteId: Int): Flow<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}