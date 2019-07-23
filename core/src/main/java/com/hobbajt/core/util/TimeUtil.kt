package com.hobbajt.core.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

enum class TimeFormat(val pattern: String) {
    Full("dd MMM, yyyy"),
    Hour("ha"),
    DayName("EEE")
}

fun ZonedDateTime.toText(timeFormat: TimeFormat): String {
    return LocalDateTime.ofInstant(toInstant(), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern(timeFormat.pattern))
}

