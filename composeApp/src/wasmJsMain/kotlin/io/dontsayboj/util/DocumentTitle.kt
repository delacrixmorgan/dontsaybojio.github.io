package io.dontsayboj.util

import kotlinx.browser.document

/**
 * Updates the browser's document title.
 * @param title The new title to set
 */
fun setDocumentTitle(title: String) {
    document.title = title
}

/**
 * Gets page title for a given route path.
 * @param route The route path
 * @return The formatted page title
 */
fun getTitleForRoute(route: String): String {
    return when (route) {
        "/" -> "Don't Say Bojio"
        "/birthdays" -> "Birthdays - Don't Say Bojio"
        "/whoadunit" -> "Whoadunit - Don't Say Bojio"
        else -> "Don't Say Bojio"
    }
}
