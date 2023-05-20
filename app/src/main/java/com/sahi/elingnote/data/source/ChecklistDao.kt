package com.sahi.elingnote.data.source

import androidx.room.*
import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Transaction
    @Query(value = "SELECT * FROM Checklist")
    fun getChecklists(): Flow<List<ChecklistWithItems>>

    @Transaction
    @Query(value = "SELECT * FROM Checklist WHERE id = :checklistId")
    suspend fun getChecklistWithItems(checklistId: Int): ChecklistWithItems

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChecklist(checklist: Checklist): Long

    @Delete
    suspend fun deleteChecklist(checklist: Checklist)

    @Upsert
    suspend fun addChecklistItem(item: ChecklistItem)

    @Delete
    suspend fun deleteChecklistItem(item: ChecklistItem)
}