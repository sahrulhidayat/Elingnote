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

    @Upsert
    suspend fun addChecklist(checklist: Checklist): Long

    @Query(value = "DELETE FROM Checklist WHERE isTrash")
    suspend fun deleteTrashChecklists()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChecklistItem(item: ChecklistItem): Long

    @Update
    suspend fun updateChecklistItem(item: ChecklistItem)

    @Delete
    suspend fun deleteChecklistItem(item: ChecklistItem)
}