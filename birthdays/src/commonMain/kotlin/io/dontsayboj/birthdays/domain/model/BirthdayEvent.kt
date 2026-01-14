package io.dontsayboj.birthdays.domain.model

data class BirthdayEvent(
    val summary: String,
    val month: Int,
    val day: Int,
    val year: Int,
    val isRecurring: Boolean
)
