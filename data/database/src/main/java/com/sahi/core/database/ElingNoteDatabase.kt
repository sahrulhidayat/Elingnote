package com.sahi.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sahi.core.database.dao.ChecklistDao
import com.sahi.core.database.dao.NoteDao
import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistItem
import com.sahi.core.model.entity.Note

@Database(
    entities = [
        Note::class,
        Checklist::class,
        ChecklistItem::class
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ElingNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    abstract val checklistDao: ChecklistDao

    companion object {
        const val DATABASE_NAME = "elingnote_db"
    }
}