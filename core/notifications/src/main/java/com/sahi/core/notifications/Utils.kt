package com.sahi.core.notifications

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun timeFormatter(hour: Int, minute: Int): String =
    String.format("%02d", hour) + ":" + String.format("%02d", minute)

fun LocalDate.simpleFormat(): String =
    this.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

fun Long.simpleDateTimeFormat(): String {
    val dateTime = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
    return dateTime.format(DateTimeFormatter.ofPattern("dd MMM, HH:mm"))
}
