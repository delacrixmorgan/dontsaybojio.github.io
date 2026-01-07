package io.dontsayboj.birthdays.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun BirthdaysTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}