package io.dontsayboj.util

import io.dontsayboj.navigation.Routes
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
        Routes.Landing.route -> Routes.Landing.title
        Routes.Birthdays.route -> Routes.Birthdays.title
        else -> Routes.Landing.title
    }
}
