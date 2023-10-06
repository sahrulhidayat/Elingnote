package com.sahi.usecase

import com.sahi.core.model.entity.Notification
import com.sahi.usecase.repository.ChecklistRepository
import com.sahi.usecase.repository.NoteRepository

class NotificationUseCaseImpl(
    private val noteRepository: NoteRepository,
    private val checklistRepository: ChecklistRepository
) : NotificationUseCase {

    override fun getAllScheduledNotifications(): List<Notification> {
        val scheduledNotes = noteRepository.getScheduledNotes(defaultTime = 0L)
        val scheduledChecklists = checklistRepository.getScheduledChecklists(defaultTime = 0L)

        val notifications = arrayListOf<Notification>()

        scheduledNotes
            .filter { it.reminderTime > System.currentTimeMillis() }
            .map {
                Notification(
                    "1${it.id}".toInt(),
                    it.title,
                    it.content,
                    it.timestamp
                )
            }.forEach { notifications.add(it) }

        scheduledChecklists
            .filter { it.checklist.reminderTime > System.currentTimeMillis() }
            .map {
                val content = it.checklistItems.joinToString("\n") { item -> item.label }

                Notification(
                    "2${it.checklist.id}".toInt(),
                    it.checklist.title,
                    content,
                    it.checklist.timestamp
                )
            }.forEach { notifications.add(it) }

        return notifications
    }

}

interface NotificationUseCase {
    fun getAllScheduledNotifications(): List<Notification>
}