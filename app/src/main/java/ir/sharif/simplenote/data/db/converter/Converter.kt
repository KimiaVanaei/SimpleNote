package ir.sharif.simplenote.data.db.converter

import androidx.room.TypeConverter
import java.util.Date

// This converter will transform a Date object into a Long timestamp and vice-versa.
class Converter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}