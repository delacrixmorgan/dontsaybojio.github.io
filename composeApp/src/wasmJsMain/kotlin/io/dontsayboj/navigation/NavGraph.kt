package io.dontsayboj.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.dontsayboj.birthdays.ui.BirthdaysScreen
import io.dontsayboj.screens.LandingScreen
import io.dontsayboj.util.getTitleForRoute
import io.dontsayboj.util.setDocumentTitle

@Composable
fun NavGraph(navHostController: NavHostController = rememberNavController()) {
    // Integrate browser history with navigation
    BrowserHistoryIntegration(navHostController)

    // Observe current route and update document title
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            val title = getTitleForRoute(route)
            setDocumentTitle(title)
        }
    }

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
        )
    }
    composable(Routes.Birthdays.route) {
        BirthdaysScreen(
            onNavigateBack = { navHostController.popBackStack() }
        )
    }
}
