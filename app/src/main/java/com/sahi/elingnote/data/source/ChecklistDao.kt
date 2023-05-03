package com.sahi.elingnote.data.source

import androidx.room.*
import com.sahi.elingnote.data.model.ChecklistEntity
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Transaction
    @Query(value = "SELECT * FROM checklist")
    fun getChecklists(): Flow<List<ChecklistWithItem>>

    @Transaction
    @Query(value = "SELECT * FROM checklist WHERE id = :checklistId")
    suspend fun getChecklistById(checklistId: Int): ChecklistWithItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChecklist(checklist: ChecklistEntity)

    @Delete
    suspend fun deleteChecklist(checklist: ChecklistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChecklistItem(item: ChecklistItem)

    @Delete
    suspend fun deleteChecklistItem(item: ChecklistItem)
}