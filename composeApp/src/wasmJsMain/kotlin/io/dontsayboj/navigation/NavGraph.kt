package io.dontsayboj.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.dontsayboj.birthdays.BirthdaysScreen
import io.dontsayboj.screens.LandingScreen
import io.dontsayboj.screens.WhoadunitScreen

@Composable
fun NavGraph(navHostController: NavHostController = rememberNavController()) {
    // Integrate browser history with navigation
    BrowserHistoryIntegration(navHostController)

    NavHost(
        navController = navHostController,
        startDestination = Routes.Landing.route,
    ) {
        formGraph(navHostController)
    }
}

fun NavGraphBuilder.formGraph(navHostController: NavHostController) {
    composable(Routes.Landing.route) {
        LandingScreen(
            onNavigateToBirthdays = { navHostController.navigate(Routes.Birthdays.route) },
            onNavigateToWhoadunit = { navHostController.navigate(Routes.Whoadunit.route) }
        )
    }
    composable(Routes.Birthdays.route) {
        BirthdaysScreen(
            onNavigateBack = { navHostController.popBackStack() }
        )
    }
    composable(Routes.Whoadunit.route) {
        WhoadunitScreen(
            onNavigateBack = { navHostController.popBackStack() }
        )
    }
}
