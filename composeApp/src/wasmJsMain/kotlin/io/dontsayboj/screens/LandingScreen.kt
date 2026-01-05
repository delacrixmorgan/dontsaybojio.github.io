package io.dontsayboj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LandingScreen(
    onNavigateToBirthdays: () -> Unit = {},
    onNavigateToWhoadunit: () -> Unit = {}
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Don't Say Bojio")

            Button(onClick = { onNavigateToBirthdays() }) {
                Text("Birthdays")
            }
            Button(onClick = { onNavigateToWhoadunit() }) {
                Text("Whoadunit")
            }
        }
    }
}