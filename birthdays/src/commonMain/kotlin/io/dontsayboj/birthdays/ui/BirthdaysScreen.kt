package io.dontsayboj.birthdays.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
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
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    when (val currentState = state) {
                        is BirthdaysUiState.Upload -> {
                            UploadScreen(
                                fileHandler = fileHandler,
                                onAction = viewModel::onAction
                            )
                        }
                        is BirthdaysUiState.Overview -> {
                            OverviewScreen(
                                birthdays = currentState.birthdays,
                                selectedConfig = currentState.selectedConfig,
                                selectedYear = currentState.selectedYear,
                                onAction = viewModel::onAction
                            )
                        }
                        is BirthdaysUiState.Done -> {
                            DoneScreen(
                                icsContent = currentState.icsContent,
                                fileName = currentState.fileName,
                                fileHandler = fileHandler,
                                onAction = viewModel::onAction
                            )
                        }
                        is BirthdaysUiState.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        appendEmoji("😕", MaterialTheme.typography.headlineLarge)
                                        append(" ")
                                        append("Oops! Something Went Wrong")
                                    },
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "We couldn't read that file. Please make sure you're uploading a contacts file (.vcf format) that includes birthdays.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = buildAnnotatedString {
                                                appendEmoji("💡", MaterialTheme.typography.titleMedium)
                                                append(" ")
                                                append("Tip:")
                                            },
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Make sure your contacts file includes birthday information. Not all contacts may have birthdays saved.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = { viewModel.onAction(BirthdaysAction.StartAgain) }
                                ) {
                                    Text(
                                        text = "Try Another File",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    text = buildAnnotatedString {
                        withLink(LinkAnnotation.Url(url = "https://github.com/delacrixmorgan/dontsaybojio-kmp/tree/main/birthdays")) { append("Birthdays") }
                        append(" by ")
                        withLink(LinkAnnotation.Url(url = "https://github.com/delacrixmorgan")) { append("Delacrix Morgan") }
                    },
                )
            },
        )
    }
}
