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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import io.dontsayboj.birthdays.platform.FileHandler
import io.dontsayboj.birthdays.theme.notoColorEmojiFontFamily
import io.dontsayboj.birthdays.ui.BirthdaysAction
import io.dontsayboj.birthdays.util.appendEmoji

@Composable
fun UploadScreen(
    fileHandler: FileHandler,
    onAction: (BirthdaysAction) -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        fileHandler.setupDragAndDrop(
            onDragEnter = { isDragging = true },
            onDragLeave = { isDragging = false },
            onFileDrop = { content -> onAction(BirthdaysAction.OnFileSelected(content)) }
        )
        onDispose {
            fileHandler.cleanup()
        }
    }
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
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Missing automatic birthday reminders in your calendar?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This tool recreates that experience. Upload your contacts and generate birthday events—complete with ages if you want them.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Why you'll love this:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        appendEmoji("🔒", MaterialTheme.typography.bodySmall)
                        append(" Completely private – Everything happens in your browser\n")

                        appendEmoji("📅", MaterialTheme.typography.bodySmall)
                        append(" Works with any calendar – Google, Apple, or any .ics-compatible app\n")

                        appendEmoji("♻️", MaterialTheme.typography.bodySmall)
                        append(" Recurring option available – Set it once and let it repeat every year\n")

                        appendEmoji("💚", MaterialTheme.typography.bodySmall)
                        append(" Free & open source – ")
                        withLink(LinkAnnotation.Url(url = "https://github.com/delacrixmorgan/dontsaybojio-kmp/tree/main/birthdays")) { append("View on GitHub") }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
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
                        "Drop your file here!"
                    } else {
                        "Drag your contacts file here (.vcf)\nor click below to browse"
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
                    onAction(BirthdaysAction.OnFileSelected(content))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Choose Contacts File (.vcf)",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = buildAnnotatedString {
                        appendEmoji("ℹ️", MaterialTheme.typography.titleMedium)
                        append(" ")
                        append("How to export your contacts")
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = buildAnnotatedString {
                        appendEmoji("📱", MaterialTheme.typography.titleSmall)
                        append(" ")
                        append("iPhone/iPad:")
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Go to iCloud.com and sign in\n" +
                            "2. Open Contacts\n" +
                            "3. Select the contacts you want (or ⌘ /Ctrl+A for all)\n" +
                            "4. Click the gear icon and choose \"Export vCard\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = buildAnnotatedString {
                        appendEmoji("💻", MaterialTheme.typography.titleSmall)
                        append(" ")
                        append("Google Contacts:")
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Go to contacts.google.com\n" +
                            "2. Select contacts or choose \"All contacts\"\n" +
                            "3. Click \"Export\" and choose \"vCard (.vcf) format\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = buildAnnotatedString {
                        appendEmoji("💡", MaterialTheme.typography.bodySmall)
                        append(" ")
                        append("Make sure to save as VCF file (.vcf format) — this is the standard contacts format.")
                    },
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
