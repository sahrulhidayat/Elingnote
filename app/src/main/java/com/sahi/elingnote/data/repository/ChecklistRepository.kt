package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.ChecklistEntity
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {

    fun getChecklists(): Flow<List<ChecklistEntity>>

    suspend fun getChecklistById(id: Int): ChecklistEntity?

    suspend fun addChecklist(checklist: ChecklistEntity)

    suspend fun deleteChecklist(checklist: ChecklistEntity)
}