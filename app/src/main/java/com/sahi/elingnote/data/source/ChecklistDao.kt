package com.sahi.elingnote.data.source

import androidx.room.*
import com.sahi.elingnote.data.model.ChecklistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Query(value = "SELECT * FROM checklists")
    fun getChecklists(): Flow<List<ChecklistEntity>>

    @Query(value = "SELECT * FROM checklists WHERE id = checklistId")
    suspend fun getChecklistById(id: Int): ChecklistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChecklist(checklist: ChecklistEntity)

    @Delete
    suspend fun deleteChecklist(checklist: ChecklistEntity)
}