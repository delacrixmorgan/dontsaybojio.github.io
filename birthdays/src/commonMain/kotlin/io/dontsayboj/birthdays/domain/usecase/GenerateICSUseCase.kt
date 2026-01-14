package io.dontsayboj.birthdays.domain.usecase

import io.dontsayboj.birthdays.domain.model.Birthday
import io.dontsayboj.birthdays.domain.model.BirthdayEvent
import io.dontsayboj.birthdays.domain.model.EventConfig
import io.dontsayboj.birthdays.util.currentYear
import io.dontsayboj.birthdays.util.icsDateTime
import io.dontsayboj.birthdays.util.simpleDate
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GenerateICSUseCase {

    companion object {
        val fileName = "dontsaybojio_birthdays_${LocalDateTime.simpleDate()}.ics"
    }

    operator fun invoke(
        birthdays: List<Birthday>,
        config: EventConfig,
        targetYear: Int,
    ): String {
        val events = when (config) {
            is EventConfig.WithAge -> generateWithAge(birthdays, targetYear)
            is EventConfig.Recurring -> generateRecurring(birthdays)
        }
        return buildICSContent(events)
    }

    private fun generateWithAge(birthdays: List<Birthday>, targetYear: Int): List<BirthdayEvent> {
        return birthdays.map { birthday ->
            val summary = if (birthday.hasYear()) {
                val age = birthday.calculateAge(targetYear)
                "${birthday.name}'s ${age}${getOrdinalSuffix(age!!)} Birthday"
            } else {
                "${birthday.name}'s Birthday"
            }
            BirthdayEvent(
                summary = summary,
                month = birthday.month,
                day = birthday.day,
                year = targetYear,
                isRecurring = false
            )
        }
    }

    private fun generateRecurring(birthdays: List<Birthday>): List<BirthdayEvent> {
        return birthdays.map { birthday ->
            BirthdayEvent(
                summary = "${birthday.name}'s Birthday",
                month = birthday.month,
                day = birthday.day,
                year = LocalDateTime.currentYear(),
                isRecurring = true
            )
        }
    }

    private fun buildICSContent(events: List<BirthdayEvent>): String {
        val sb = StringBuilder()

        // ICS Header
        sb.appendLine("BEGIN:VCALENDAR")
        sb.appendLine("VERSION:2.0")
        sb.appendLine("PRODID:-//DontSayBojio//Birthdays//EN")
        sb.appendLine("CALSCALE:GREGORIAN")
        sb.appendLine("METHOD:PUBLISH")

        // Add each event
        for ((index, event) in events.withIndex()) {
            sb.appendLine("BEGIN:VEVENT")
            sb.appendLine("UID:birthday-${generateUid()}-${index}@dontsayboj.io")
            sb.appendLine("DTSTAMP:${getCurrentTimestamp()}")
            sb.appendLine("DTSTART;VALUE=DATE:${formatDate(event.year, event.month, event.day)}")
            sb.appendLine("SUMMARY:${event.summary}")

            if (event.isRecurring) {
                sb.appendLine("RRULE:FREQ=YEARLY")
            }

            sb.appendLine("CATEGORIES:Birthday")
            sb.appendLine("END:VEVENT")
        }

        // ICS Footer
        sb.appendLine("END:VCALENDAR")

        return sb.toString()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val yearStr = year.toString().padStart(4, '0')
        val monthStr = month.toString().padStart(2, '0')
        val dayStr = day.toString().padStart(2, '0')
        return "$yearStr$monthStr$dayStr"
    }

    private fun getCurrentTimestamp(): String {
        return LocalDateTime.icsDateTime()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun generateUid(): String {
        return Uuid.random().toString()
    }

    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number % 100 in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }
}
