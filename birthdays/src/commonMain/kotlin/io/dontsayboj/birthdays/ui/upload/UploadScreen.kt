package io.dontsayboj.birthdays.ui.upload

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.dontsayboj.birthdays.platform.FileHandler
import io.dontsayboj.birthdays.presentation.BirthdaysIntent
import io.dontsayboj.birthdays.theme.notoColorEmojiFontFamily

@Composable
fun UploadScreen(
    onIntent: (BirthdaysIntent) -> Unit,
    fileHandler: FileHandler
) {
    var isDragging by remember { mutableStateOf(false) }

    // Setup drag and drop
    DisposableEffect(Unit) {
        fileHandler.setupDragAndDrop(
            onDragEnter = { isDragging = true },
            onDragLeave = { isDragging = false },
            onFileDrop = { content ->
                onIntent(BirthdaysIntent.FileSelected(content))
            }
        )

        onDispose {
            fileHandler.cleanup()
        }
    }

    // Animate colors based on drag state
    val borderColor by animateColorAsState(
        targetValue = if (isDragging) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primary
        }
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isDragging) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        } else {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.headlineLarge.copy(fontFamily = notoColorEmojiFontFamily).toSpanStyle()) {
                    append("📤")
                }
                append(" ")
                append("Upload VCF File")
            },
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Upload a .vcf (vCard) file to extract birthdays and create calendar events",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Drop zone / Upload area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(
                    width = if (isDragging) 3.dp else 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎂",
                    style = MaterialTheme.typography.displayLarge.copy(fontFamily = notoColorEmojiFontFamily)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isDragging) {
                        "Drop your VCF file here!"
                    } else {
                        "Drag & drop a VCF file here\nor click the button below"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = if (isDragging) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                fileHandler.pickFile { content ->
                    onIntent(BirthdaysIntent.FileSelected(content))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Select VCF File",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Instructions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = MaterialTheme.typography.titleMedium.copy(fontFamily = notoColorEmojiFontFamily).toSpanStyle()) {
                            append("ℹ️")
                        }
                        append(" ")
                        append("How to use:")
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Export contacts with birthdays from your device as a .vcf file\n" +
                            "2. Select the file using the button above\n" +
                            "3. Review detected birthdays and choose your preferred format\n" +
                            "4. Download the generated calendar file",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
