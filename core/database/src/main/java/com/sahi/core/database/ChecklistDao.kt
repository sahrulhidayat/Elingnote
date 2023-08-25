package com.sahi.core.database

import androidx.room.*
import com.sahi.core.model.Entity.Checklist
import com.sahi.core.model.Entity.ChecklistItem
import com.sahi.core.model.Entity.ChecklistWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Transaction
    @Query(value = "SELECT * FROM Checklist")
    fun getChecklists(): Flow<List<ChecklistWithItems>>

    @Transaction
    @Query(value = "SELECT * FROM Checklist WHERE id = :checklistId")
    suspend fun getChecklistWithItems(checklistId: Int): ChecklistWithItems

    @Upsert
    suspend fun addChecklist(checklist: Checklist): Long

    @Transaction
    @Query(value = "DELETE FROM Checklist WHERE isTrash")
    suspend fun deleteTrashChecklists()

    @Transaction
    @Delete
    suspend fun deleteChecklist(checklist: Checklist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChecklistItem(item: ChecklistItem): Long

    @Update
    suspend fun updateChecklistItem(item: ChecklistItem)

    @Delete
    suspend fun deleteChecklistItem(item: ChecklistItem)
}