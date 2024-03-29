package com.sahi.core.database.dao

import androidx.room.*
import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistItem
import com.sahi.core.model.entity.ChecklistWithItems
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