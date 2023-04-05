package com.sahi.elingnote.di

import android.content.Context
import androidx.room.Room
import com.sahi.elingnote.data.repository.ChecklistRepository
import com.sahi.elingnote.data.repository.ChecklistRepositoryImpl
import com.sahi.elingnote.data.repository.NoteRepository
import com.sahi.elingnote.data.repository.NoteRepositoryImpl
import com.sahi.elingnote.data.source.ElingNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesElingNoteDatabase(
        @ApplicationContext context: Context
    ) : ElingNoteDatabase {
        return Room.databaseBuilder(
            context,
            ElingNoteDatabase::class.java,
            ElingNoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(db: ElingNoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun providesChecklistRepository(db: ElingNoteDatabase): ChecklistRepository {
        return ChecklistRepositoryImpl(db.checklistDao)
    }
}