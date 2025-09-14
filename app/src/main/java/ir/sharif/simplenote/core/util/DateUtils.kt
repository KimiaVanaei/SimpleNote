package ir.sharif.simplenote.core.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun toTimestamp(date: String?): Long {
    return try {
        if (date == null) System.currentTimeMillis()
        else ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
            .toInstant()
            .toEpochMilli()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}
