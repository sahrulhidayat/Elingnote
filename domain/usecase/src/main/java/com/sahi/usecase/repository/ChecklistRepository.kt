package com.sahi.usecase.repository

import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistItem
import com.sahi.core.model.entity.ChecklistWithItems
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getChecklists(): Flow<List<ChecklistWithItems>>
    fun getScheduledChecklists(defaultTime: Long): List<ChecklistWithItems>
    suspend fun getChecklistWithItems(id: Int): ChecklistWithItems
    suspend fun addChecklist(checklist: Checklist): Long
    suspend fun deleteTrashChecklists()
    suspend fun deleteChecklist(checklist: Checklist)
    suspend fun addChecklistItem(item: ChecklistItem): Long
    suspend fun updateChecklistItem(item: ChecklistItem)
    suspend fun deleteChecklistItem(item: ChecklistItem)
}