package io.dontsayboj.birthdays.domain.model

sealed class EventConfig {
    data object WithAge : EventConfig()
    data object Recurring : EventConfig()
}
