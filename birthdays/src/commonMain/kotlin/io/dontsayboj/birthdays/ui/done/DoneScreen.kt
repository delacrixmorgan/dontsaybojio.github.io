package io.dontsayboj.birthdays.ui.done

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun DoneScreen(
    icsContent: String,
    fileName: String,
    onIntent: (BirthdaysIntent) -> Unit,
    fileHandler: FileHandler
) {
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
                    append("✅")
                }
                append(" ")
                append("Calendar Generated!")
            },
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your birthday calendar file is ready to download",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Success Icon
        Card(
            modifier = Modifier.size(120.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(60.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎂",
                    style = MaterialTheme.typography.displayLarge.copy(fontFamily = notoColorEmojiFontFamily)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Information Card
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
                        append("Next Steps:")
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("1. Click the download button below\n" +
                                "2. Open the downloaded .ics file\n" +
                                "3. Import it into your calendar app\n" +
                                "4. Never miss a birthday again! ")

                        withStyle(style = MaterialTheme.typography.bodyMedium.copy(fontFamily = notoColorEmojiFontFamily).toSpanStyle()) {
                            append("🥳")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Download Button
        Button(
            onClick = {
                fileHandler.downloadFile(icsContent, fileName)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "⬇️ Download Calendar File",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Again Button
        OutlinedButton(
            onClick = {
                onIntent(BirthdaysIntent.StartAgain)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "🔄 Convert Another File",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // File info
        Text(
            text = "File: $fileName",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
