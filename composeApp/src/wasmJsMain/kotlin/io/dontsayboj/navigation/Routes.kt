package io.dontsayboj.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {

    data object Landing : Routes("/")

    data object Birthdays : Routes("/birthdays")

    data object Whoadunit : Routes("/whoadunit")
}
