package com.sahi.elingnote.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sahi.elingnote.data.model.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class ElingNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "elingnote_db"
    }
}