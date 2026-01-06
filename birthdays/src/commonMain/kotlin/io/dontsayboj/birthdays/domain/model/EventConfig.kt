package io.dontsayboj.birthdays.domain.model

sealed class EventConfig {
    data class WithAge(val year: Int) : EventConfig()
    data object Recurring : EventConfig()
}
