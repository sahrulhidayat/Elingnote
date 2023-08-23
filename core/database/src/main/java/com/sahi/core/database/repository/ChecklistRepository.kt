package com.sahi.core.database.repository

import com.sahi.core.database.model.Checklist
import com.sahi.core.database.model.ChecklistItem
import com.sahi.core.database.model.ChecklistWithItems
import com.sahi.core.database.ChecklistDao
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getChecklists(): Flow<List<com.sahi.core.database.model.ChecklistWithItems>>
    suspend fun getChecklistWithItems(id: Int): com.sahi.core.database.model.ChecklistWithItems
    suspend fun addChecklist(checklist: com.sahi.core.database.model.Checklist): Long
    suspend fun deleteTrashChecklists()
    suspend fun deleteChecklist(checklist: com.sahi.core.database.model.Checklist)
    suspend fun addChecklistItem(item: com.sahi.core.database.model.ChecklistItem): Long
    suspend fun updateChecklistItem(item: com.sahi.core.database.model.ChecklistItem)
    suspend fun deleteChecklistItem(item: com.sahi.core.database.model.ChecklistItem)
}

class ChecklistRepositoryImpl(
    private val checklistDao: com.sahi.core.database.ChecklistDao
) : ChecklistRepository {
    override fun getChecklists(): Flow<List<com.sahi.core.database.model.ChecklistWithItems>> {
        return checklistDao.getChecklists()
    }
    override suspend fun getChecklistWithItems(id: Int): com.sahi.core.database.model.ChecklistWithItems {
        return checklistDao.getChecklistWithItems(id)
    }
    override suspend fun addChecklist(checklist: com.sahi.core.database.model.Checklist): Long {
        return checklistDao.addChecklist(checklist)
    }
    override suspend fun deleteTrashChecklists() {
        return checklistDao.deleteTrashChecklists()
    }
    override suspend fun deleteChecklist(checklist: com.sahi.core.database.model.Checklist) {
        return checklistDao.deleteChecklist(checklist)
    }
    override suspend fun addChecklistItem(item: com.sahi.core.database.model.ChecklistItem): Long {
        return checklistDao.addChecklistItem(item)
    }
    override suspend fun updateChecklistItem(item: com.sahi.core.database.model.ChecklistItem) {
        return checklistDao.updateChecklistItem(item)
    }
    override suspend fun deleteChecklistItem(item: com.sahi.core.database.model.ChecklistItem) {
        return checklistDao.deleteChecklistItem(item)
    }
}