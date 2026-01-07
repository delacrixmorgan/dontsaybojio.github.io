package io.dontsayboj.birthdays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.dontsayboj.birthdays.platform.FileHandler
import io.dontsayboj.birthdays.presentation.BirthdaysState
import io.dontsayboj.birthdays.presentation.BirthdaysViewModel
import io.dontsayboj.birthdays.theme.BirthdaysTheme
import io.dontsayboj.birthdays.ui.done.DoneScreen
import io.dontsayboj.birthdays.ui.overview.OverviewScreen
import io.dontsayboj.birthdays.ui.upload.UploadScreen

@Composable
fun BirthdaysScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel: BirthdaysViewModel = viewModel { BirthdaysViewModel() }
    val state by viewModel.state.collectAsState()
    val fileHandler = remember { FileHandler() }

    BirthdaysTheme {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                when (val currentState = state) {
                    is BirthdaysState.Upload -> {
                        UploadScreen(
                            onIntent = viewModel::handleIntent,
                            fileHandler = fileHandler
                        )
                    }

                    is BirthdaysState.Overview -> {
                        OverviewScreen(
                            birthdays = currentState.birthdays,
                            selectedConfig = currentState.selectedConfig,
                            selectedYear = currentState.selectedYear,
                            onIntent = viewModel::handleIntent
                        )
                    }

                    is BirthdaysState.Done -> {
                        DoneScreen(
                            icsContent = currentState.icsContent,
                            fileName = currentState.fileName,
                            onIntent = viewModel::handleIntent,
                            fileHandler = fileHandler
                        )
                    }

                    is BirthdaysState.Error -> {
                        // Error state
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "❌ Error",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = currentState.message,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = { viewModel.handleIntent(io.dontsayboj.birthdays.presentation.BirthdaysIntent.StartAgain) }) {
                                Text("Try Again")
                            }
                        }
                    }
                }
            }
        }
    }
}
