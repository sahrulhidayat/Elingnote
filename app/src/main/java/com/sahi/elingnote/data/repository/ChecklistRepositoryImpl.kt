package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.ChecklistEntity
import com.sahi.elingnote.data.source.ChecklistDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChecklistRepositoryImpl @Inject constructor(
    private val checklistDao: ChecklistDao
) : ChecklistRepository {
    override fun getChecklists(): Flow<List<ChecklistEntity>> {
        return checklistDao.getChecklists()
    }

    override suspend fun getChecklistById(id: Int): ChecklistEntity? {
        return checklistDao.getChecklistById(id)
    }

    override suspend fun addChecklist(checklist: ChecklistEntity) {
        return checklistDao.addChecklist(checklist)
    }

    override suspend fun deleteChecklist(checklist: ChecklistEntity) {
        return checklistDao.deleteChecklist(checklist)
    }
}