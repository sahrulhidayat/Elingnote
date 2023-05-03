package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.ChecklistEntity
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItem
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {

    fun getChecklists(): Flow<List<ChecklistWithItem>>

    suspend fun getChecklistById(id: Int): ChecklistWithItem

    suspend fun addChecklist(checklist: ChecklistEntity)

    suspend fun deleteChecklist(checklist: ChecklistEntity)

    suspend fun addChecklistItem(item: ChecklistItem)

    suspend fun deleteChecklistItem(item: ChecklistItem)
}