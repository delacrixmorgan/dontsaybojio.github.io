package io.dontsayboj.birthdays.presentation

import io.dontsayboj.birthdays.domain.model.Birthday
import io.dontsayboj.birthdays.domain.model.EventConfig

sealed class BirthdaysState {
    data object Upload : BirthdaysState()
    
    data class Overview(
        val birthdays: List<Birthday>,
        val selectedConfig: EventConfig = EventConfig.Recurring,
        val selectedYear: Int = 2026
    ) : BirthdaysState()
    
    data class Done(
        val icsContent: String,
        val fileName: String = "birthdays.ics"
    ) : BirthdaysState()
    
    data class Error(
        val message: String
    ) : BirthdaysState()
}
