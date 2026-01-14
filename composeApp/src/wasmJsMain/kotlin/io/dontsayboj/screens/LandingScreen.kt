package io.dontsayboj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp

@Composable
fun LandingScreen(
    onNavigateToBirthdays: () -> Unit = {},
) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Don't Say Bojio",
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Making magic happen in Kotlin",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withLink(LinkAnnotation.Url(url = "https://bsky.app/hashtag/MakeGoodThings")) { append("#MakeGoodThings") }
                    },
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { onNavigateToBirthdays() }) {
                        Text("Birthdays")
                    }
                }
                Spacer(Modifier.height(48.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Made by ")
                        withLink(LinkAnnotation.Url(url = "https://github.com/delacrixmorgan")) { append("Delacrix Morgan") }
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}