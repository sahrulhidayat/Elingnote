package com.sahi.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sahi.core.model.Entity.Checklist
import com.sahi.core.model.Entity.ChecklistItem
import com.sahi.core.model.Entity.Note

@Database(
    entities = [
        Note::class,
        Checklist::class,
        ChecklistItem::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class ElingNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    abstract val checklistDao: ChecklistDao

    companion object {
        const val DATABASE_NAME = "elingnote_db"
    }
}