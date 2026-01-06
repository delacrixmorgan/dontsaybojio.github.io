package io.dontsayboj.birthdays.presentation

import io.dontsayboj.birthdays.domain.model.EventConfig

sealed class BirthdaysIntent {
    data class FileSelected(val content: String) : BirthdaysIntent()
    data class EventConfigSelected(val config: EventConfig) : BirthdaysIntent()
    data class YearSelected(val year: Int) : BirthdaysIntent()
    data object GenerateCalendar : BirthdaysIntent()
    data object DownloadFile : BirthdaysIntent()
    data object StartAgain : BirthdaysIntent()
}
