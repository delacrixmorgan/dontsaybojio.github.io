package io.dontsayboj.birthdays.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dontsayboj.birthdays.domain.model.Birthday
import io.dontsayboj.birthdays.domain.model.EventConfig
import io.dontsayboj.birthdays.domain.usecase.GenerateIcsUseCase
import io.dontsayboj.birthdays.domain.usecase.ParseVcfUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BirthdaysViewModel(
    private val parseVcfUseCase: ParseVcfUseCase = ParseVcfUseCase(),
    private val generateIcsUseCase: GenerateIcsUseCase = GenerateIcsUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow<BirthdaysState>(BirthdaysState.Upload)
    val state: StateFlow<BirthdaysState> = _state.asStateFlow()

    private var currentBirthdays: List<Birthday> = emptyList()
    private var currentConfig: EventConfig = EventConfig.WithAge(2026)
    private var currentYear: Int = 2026

    fun handleIntent(intent: BirthdaysIntent) {
        viewModelScope.launch {
            when (intent) {
                is BirthdaysIntent.FileSelected -> handleFileSelected(intent.content)
                is BirthdaysIntent.EventConfigSelected -> handleEventConfigSelected(intent.config)
                is BirthdaysIntent.YearSelected -> handleYearSelected(intent.year)
                is BirthdaysIntent.GenerateCalendar -> handleGenerateCalendar()
                is BirthdaysIntent.DownloadFile -> handleDownloadFile()
                is BirthdaysIntent.StartAgain -> handleStartAgain()
            }
        }
    }

    private fun handleFileSelected(content: String) {
        try {
            val birthdays = parseVcfUseCase(content)

            if (birthdays.isEmpty()) {
                _state.value = BirthdaysState.Error("No birthdays found in the VCF file")
            } else {
                currentBirthdays = birthdays
                _state.value = BirthdaysState.Overview(
                    birthdays = birthdays,
                    selectedConfig = currentConfig,
                    selectedYear = currentYear
                )
            }
        } catch (e: Exception) {
            _state.value = BirthdaysState.Error("Error parsing VCF file: ${e.message}")
        }
    }

    private fun handleEventConfigSelected(config: EventConfig) {
        currentConfig = config

        val currentState = _state.value
        if (currentState is BirthdaysState.Overview) {
            _state.value = currentState.copy(selectedConfig = config)
        }
    }

    private fun handleYearSelected(year: Int) {
        currentYear = year

        val currentState = _state.value
        if (currentState is BirthdaysState.Overview) {
            _state.value = currentState.copy(selectedYear = year)
        }
    }

    private fun handleGenerateCalendar() {
        try {
            val icsContent = generateIcsUseCase(currentBirthdays, currentConfig)
            _state.value = BirthdaysState.Done(icsContent = icsContent)
        } catch (e: Exception) {
            _state.value = BirthdaysState.Error("Error generating calendar: ${e.message}")
        }
    }

    private fun handleDownloadFile() {
        // This will be handled by the UI layer using platform-specific code
        // The UI will access the ICS content from the Done state
    }

    private fun handleStartAgain() {
        currentBirthdays = emptyList()
        currentConfig = EventConfig.WithAge(2026)
        currentYear = 2026
        _state.value = BirthdaysState.Upload
    }
}
