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
import androidx.compose.ui.unit.dp
import io.dontsayboj.birthdays.platform.FileHandler
import io.dontsayboj.birthdays.ui.BirthdaysAction
import io.dontsayboj.birthdays.theme.notoColorEmojiFontFamily
import io.dontsayboj.birthdays.util.appendEmoji

@Composable
fun DoneScreen(
    icsContent: String,
    fileName: String,
    onIntent: (BirthdaysAction) -> Unit,
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
                appendEmoji("✅", MaterialTheme.typography.headlineLarge)
                append(" ")
                append("Calendar Generated!")
            },
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your birthday reminders are ready!",
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

        // Download Button (moved up)
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
                text = "⬇️ Download Calendar File (.ics)",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Import Instructions Card
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
                        appendEmoji("ℹ️", MaterialTheme.typography.titleMedium)
                        append(" ")
                        append("How to import your calendar file (.ics)")
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Google Calendar Instructions
                Text(
                    text = "💻 For Google Calendar:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Open Google Calendar on your computer\n" +
                            "2. Click the gear icon (⚙️) → Settings\n" +
                            "3. Choose \"Import & Export\" from the left menu\n" +
                            "4. Click \"Select file from your computer\"\n" +
                            "5. Choose the downloaded file and select which calendar\n" +
                            "6. Click \"Import\" and you're all set!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // iPhone/iPad Instructions
                Text(
                    text = "📱 For iPhone/iPad:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Open the downloaded file\n" +
                            "2. Tap \"Add All\" when prompted\n" +
                            "3. Choose which calendar to add them to\n" +
                            "4. Done! Check your Calendar app",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mac Calendar Instructions
                Text(
                    text = "🖥️ For Mac Calendar:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Open the Calendar app\n" +
                            "2. Double-click the downloaded file\n" +
                            "3. Choose which calendar to add them to\n" +
                            "4. Click \"OK\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Start Again Button
        OutlinedButton(
            onClick = {
                onIntent(BirthdaysAction.StartAgain)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Start Over",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
