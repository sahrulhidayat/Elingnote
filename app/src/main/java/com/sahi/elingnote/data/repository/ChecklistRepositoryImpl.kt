package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.ChecklistEntity
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItem
import com.sahi.elingnote.data.source.ChecklistDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChecklistRepositoryImpl @Inject constructor(
    private val checklistDao: ChecklistDao
) : ChecklistRepository {
    override fun getChecklists(): Flow<List<ChecklistWithItem>> {
        return checklistDao.getChecklists()
    }

    override suspend fun getChecklistById(id: Int): ChecklistWithItem {
        return checklistDao.getChecklistById(id)
    }

    override suspend fun addChecklist(checklist: ChecklistEntity) {
        return checklistDao.addChecklist(checklist)
    }

    override suspend fun deleteChecklist(checklist: ChecklistEntity) {
        return checklistDao.deleteChecklist(checklist)
    }

    override suspend fun addChecklistItem(item: ChecklistItem) {
        return checklistDao.addChecklistItem(item)
    }

    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        return checklistDao.deleteChecklistItem(item)
    }
}