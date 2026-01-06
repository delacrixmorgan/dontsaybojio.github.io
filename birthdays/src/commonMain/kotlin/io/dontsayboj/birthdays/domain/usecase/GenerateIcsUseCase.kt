package io.dontsayboj.birthdays.domain.usecase

import io.dontsayboj.birthdays.domain.model.Birthday
import io.dontsayboj.birthdays.domain.model.BirthdayEvent
import io.dontsayboj.birthdays.domain.model.EventConfig

class GenerateIcsUseCase {
    
    operator fun invoke(
        birthdays: List<Birthday>,
        config: EventConfig
    ): String {
        val events = when (config) {
            is EventConfig.WithAge -> generateWithAge(birthdays, config.year)
            is EventConfig.Recurring -> generateRecurring(birthdays)
        }
        
        return buildIcsContent(events)
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
                year = 2026, // Use current year as starting point
                isRecurring = true
            )
        }
    }
    
    private fun buildIcsContent(events: List<BirthdayEvent>): String {
        val sb = StringBuilder()
        
        // ICS Header
        sb.appendLine("BEGIN:VCALENDAR")
        sb.appendLine("VERSION:2.0")
        sb.appendLine("PRODID:-//DontSayBojio//Birthday Converter//EN")
        sb.appendLine("CALSCALE:GREGORIAN")
        sb.appendLine("METHOD:PUBLISH")
        
        // Add each event
        for ((index, event) in events.withIndex()) {
            sb.appendLine("BEGIN:VEVENT")
            sb.appendLine("UID:birthday-${generateUid()}-${index}@dontsaybojio.io")
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
        // For simplicity, using a fixed format. In production, use proper datetime library
        return "20260106T200000Z"
    }
    
    private fun generateUid(): String {
        // Generate a simple unique identifier
        return (0..999999).random().toString()
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
