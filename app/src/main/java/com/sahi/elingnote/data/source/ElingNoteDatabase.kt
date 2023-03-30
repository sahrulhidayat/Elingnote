package com.sahi.elingnote.data.source

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        NoteDao::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class ElingNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "elingnote_db"
    }
}