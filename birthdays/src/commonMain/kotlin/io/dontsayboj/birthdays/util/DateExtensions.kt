package io.dontsayboj.birthdays.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun LocalDateTime.Companion.currentYear(timeZone: TimeZone = TimeZone.currentSystemDefault()): Int {
    return Clock.System.now().toLocalDateTime(timeZone).year
}

fun LocalDateTime.Companion.icsDateTime(): String {
    val localDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val icsDateTime = buildString {
        append(localDateTime.year.toString().padStart(4, '0'))
        append(localDateTime.month.number.toString().padStart(2, '0'))
        append(localDateTime.day.toString().padStart(2, '0'))
        append('T')
        append(localDateTime.hour.toString().padStart(2, '0'))
        append(localDateTime.minute.toString().padStart(2, '0'))
        append(localDateTime.second.toString().padStart(2, '0'))
        append('Z')
    }
    return icsDateTime
}

fun LocalDateTime.Companion.simpleDate(): String {
    val localDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val simpleDate = buildString {
        append(localDateTime.day.toString().padStart(2, '0'))
        append(localDateTime.month.number.toString().padStart(2, '0'))
        append(localDateTime.year.toString().padStart(4, '0'))
    }
    return simpleDate
}