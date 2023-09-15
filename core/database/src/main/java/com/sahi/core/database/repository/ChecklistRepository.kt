package com.sahi.core.database.repository

import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistItem
import com.sahi.core.model.entity.ChecklistWithItems
import com.sahi.core.database.ChecklistDao
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getChecklists(): Flow<List<ChecklistWithItems>>
    suspend fun getChecklistWithItems(id: Int): ChecklistWithItems
    suspend fun addChecklist(checklist: Checklist): Long
    suspend fun deleteTrashChecklists()
    suspend fun deleteChecklist(checklist: Checklist)
    suspend fun addChecklistItem(item: ChecklistItem): Long
    suspend fun updateChecklistItem(item: ChecklistItem)
    suspend fun deleteChecklistItem(item: ChecklistItem)
}

class ChecklistRepositoryImpl(
    private val checklistDao: ChecklistDao
) : ChecklistRepository {
    override fun getChecklists(): Flow<List<ChecklistWithItems>> {
        return checklistDao.getChecklists()
    }
    override suspend fun getChecklistWithItems(id: Int): ChecklistWithItems {
        return checklistDao.getChecklistWithItems(id)
    }
    override suspend fun addChecklist(checklist: Checklist): Long {
        return checklistDao.addChecklist(checklist)
    }
    override suspend fun deleteTrashChecklists() {
        return checklistDao.deleteTrashChecklists()
    }
    override suspend fun deleteChecklist(checklist: Checklist) {
        return checklistDao.deleteChecklist(checklist)
    }
    override suspend fun addChecklistItem(item: ChecklistItem): Long {
        return checklistDao.addChecklistItem(item)
    }
    override suspend fun updateChecklistItem(item: ChecklistItem) {
        return checklistDao.updateChecklistItem(item)
    }
    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        return checklistDao.deleteChecklistItem(item)
    }
}