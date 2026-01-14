package io.dontsayboj.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(
    val route: String,
    val title: String
) {
    data object Landing : Routes(route = "/", title = "Don't Say Bojio")
    data object Birthdays : Routes(route = "/birthdays", title = "Birthdays - Don't Say Bojio")
}
