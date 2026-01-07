package io.dontsayboj.birthdays.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dontsayboj.birthdays.domain.model.Birthday
import io.dontsayboj.birthdays.domain.model.EventConfig
import io.dontsayboj.birthdays.domain.usecase.GenerateICSUseCase
import io.dontsayboj.birthdays.domain.usecase.ParseVCFUseCase
import io.dontsayboj.birthdays.util.currentYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class BirthdaysViewModel : ViewModel() {

    private val _state = MutableStateFlow<BirthdaysUiState>(BirthdaysUiState.Upload)
    val state: StateFlow<BirthdaysUiState> = _state.asStateFlow()

    private val parseVcfUseCase: ParseVCFUseCase by lazy { ParseVCFUseCase() }
    private val generateIcsUseCase: GenerateICSUseCase by lazy { GenerateICSUseCase() }

    private var currentBirthdays: List<Birthday> = emptyList()
    private var currentConfig: EventConfig = EventConfig.WithAge
    private var targetYear: Int = LocalDateTime.currentYear()

    fun onAction(action: BirthdaysAction) {
        viewModelScope.launch {
            when (action) {
                is BirthdaysAction.OnFileSelected -> handleFileSelected(action.content)
                is BirthdaysAction.OnEventConfigSelected -> handleEventConfigSelected(action.config)
                is BirthdaysAction.OnYearSelected -> handleYearSelected(action.year)
                is BirthdaysAction.GenerateCalendar -> handleGenerateCalendar()
                is BirthdaysAction.DownloadFile -> handleDownloadFile()
                is BirthdaysAction.StartAgain -> handleStartAgain()
            }
        }
    }

    private fun handleFileSelected(content: String) {
        try {
            val birthdays = parseVcfUseCase(content)
            if (birthdays.isEmpty()) {
                _state.value = BirthdaysUiState.Error("No birthdays found in the VCF file")
            } else {
                currentBirthdays = birthdays
                _state.value = BirthdaysUiState.Overview(
                    birthdays = birthdays,
                    selectedConfig = currentConfig,
                    selectedYear = targetYear
                )
            }
        } catch (e: Exception) {
            _state.value = BirthdaysUiState.Error("Error parsing VCF file: ${e.message}")
        }
    }

    private fun handleEventConfigSelected(config: EventConfig) {
        currentConfig = config

        val currentState = _state.value
        if (currentState is BirthdaysUiState.Overview) {
            _state.value = currentState.copy(selectedConfig = config)
        }
    }

    private fun handleYearSelected(year: Int) {
        targetYear = year

        val currentState = _state.value
        if (currentState is BirthdaysUiState.Overview) {
            _state.value = currentState.copy(selectedYear = year)
        }
    }

    private fun handleGenerateCalendar() {
        try {
            val icsContent = generateIcsUseCase(currentBirthdays, currentConfig, targetYear)
            _state.value = BirthdaysUiState.Done(icsContent = icsContent)
        } catch (e: Exception) {
            _state.value = BirthdaysUiState.Error("Error generating calendar: ${e.message}")
        }
    }

    private fun handleDownloadFile() {
        // This will be handled by the UI layer using platform-specific code
        // The UI will access the ICS content from the Done state
    }

    private fun handleStartAgain() {
        currentBirthdays = emptyList()
        currentConfig = EventConfig.WithAge
        targetYear = LocalDateTime.currentYear()
        _state.value = BirthdaysUiState.Upload
    }
}

sealed class BirthdaysUiState {
    data object Upload : BirthdaysUiState()

    data class Overview(
        val birthdays: List<Birthday>,
        val selectedConfig: EventConfig = EventConfig.Recurring,
        val selectedYear: Int = LocalDateTime.currentYear()
    ) : BirthdaysUiState()

    data class Done(
        val icsContent: String,
        val fileName: String = GenerateICSUseCase.fileName
    ) : BirthdaysUiState()

    data class Error(
        val message: String
    ) : BirthdaysUiState()
}

sealed interface BirthdaysAction {
    data class OnFileSelected(val content: String) : BirthdaysAction
    data class OnEventConfigSelected(val config: EventConfig) : BirthdaysAction
    data class OnYearSelected(val year: Int) : BirthdaysAction
    data object GenerateCalendar : BirthdaysAction
    data object DownloadFile : BirthdaysAction
    data object StartAgain : BirthdaysAction
}
