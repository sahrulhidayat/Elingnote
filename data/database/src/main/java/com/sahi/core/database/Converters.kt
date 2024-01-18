package com.sahi.core.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pointlessapps.rt_editor.model.RichTextValue
import com.pointlessapps.rt_editor.utils.RichTextValueSnapshot

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