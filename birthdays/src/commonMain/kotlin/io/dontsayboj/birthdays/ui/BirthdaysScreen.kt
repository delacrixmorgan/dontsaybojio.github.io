package io.dontsayboj.birthdays.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.dontsayboj.birthdays.platform.FileHandler
import io.dontsayboj.birthdays.theme.BirthdaysTheme
import io.dontsayboj.birthdays.ui.done.DoneScreen
import io.dontsayboj.birthdays.ui.overview.OverviewScreen
import io.dontsayboj.birthdays.ui.upload.UploadScreen
import io.dontsayboj.birthdays.util.appendEmoji

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdaysScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel: BirthdaysViewModel = viewModel { BirthdaysViewModel() }
    val state by viewModel.state.collectAsState()
    val fileHandler = remember { FileHandler() }

    BirthdaysTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Birthdays",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onNavigateBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Go back",
                            )
                        }
                    },
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    when (val currentState = state) {
                        is BirthdaysUiState.Upload -> {
                            UploadScreen(
                                onIntent = viewModel::onAction,
                                fileHandler = fileHandler
                            )
                        }

                        is BirthdaysUiState.Overview -> {
                            OverviewScreen(
                                birthdays = currentState.birthdays,
                                selectedConfig = currentState.selectedConfig,
                                selectedYear = currentState.selectedYear,
                                onIntent = viewModel::onAction
                            )
                        }

                        is BirthdaysUiState.Done -> {
                            DoneScreen(
                                icsContent = currentState.icsContent,
                                fileName = currentState.fileName,
                                onIntent = viewModel::onAction,
                                fileHandler = fileHandler
                            )
                        }

                        is BirthdaysUiState.Error -> {
                            // Error state
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        appendEmoji("❌", MaterialTheme.typography.headlineLarge)
                                        append(" ")
                                        append("Error")
                                    },
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = currentState.message,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(onClick = { viewModel.onAction(BirthdaysAction.StartAgain) }) {
                                    Text("Try Again")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
