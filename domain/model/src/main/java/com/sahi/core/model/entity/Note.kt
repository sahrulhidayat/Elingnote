package com.sahi.core.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pointlessapps.rt_editor.model.RichTextValue
import com.pointlessapps.rt_editor.utils.RichTextValueSnapshot

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: RichTextValueSnapshot,
    val timestamp: Long,
    val color: Int,
    var isTrash: Boolean = false,
    val reminderTime: Long = 0L
)

class Converters {
    @TypeConverter
    fun fromRichTextValueSnapshot(
        snapshot: RichTextValueSnapshot
    ) : String {
        return Gson().toJson(snapshot)
    }

    @TypeConverter
    fun toRichTextValueSnapshot(value: String) : RichTextValueSnapshot {
        val content = RichTextValue.get()
        val snapshot = content.getLastSnapshot()
        return Gson().fromJson(value, snapshot::class.java)
    }
}
