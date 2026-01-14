package io.dontsayboj.birthdays.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.withStyle
import io.dontsayboj.birthdays.theme.notoColorEmojiFontFamily

@Composable
fun AnnotatedString.Builder.appendEmoji(
    emoji: String,
    textStyle: TextStyle,
) {
    withStyle(style = textStyle.copy(fontFamily = notoColorEmojiFontFamily).toSpanStyle()) {
        append(emoji)
    }
}