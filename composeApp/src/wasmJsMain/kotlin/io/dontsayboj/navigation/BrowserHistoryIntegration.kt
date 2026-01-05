package io.dontsayboj.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import kotlinx.browser.window
import org.w3c.dom.events.Event

/**
 * Integrates browser history with Compose Navigation.
 * - Updates browser URL when navigation occurs
 * - Handles browser back/forward buttons
 * - Supports initial deep linking
 */
@OptIn(ExperimentalWasmJsInterop::class)
@Composable
fun BrowserHistoryIntegration(navController: NavHostController) {
    // Handle initial URL on first load
    LaunchedEffect(Unit) {
        val initialPath = window.location.pathname
        if (initialPath != "/" && initialPath.isNotEmpty()) {
            navController.navigate(initialPath)
        }
    }

    // Listen to NavController changes and update browser URL
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val route = destination.route ?: "/"
            val currentPath = window.location.pathname
            
            // Only update if the path is different to avoid infinite loops
            if (route != currentPath) {
                window.history.pushState(null, "", route)
            }
        }
    }

    // Listen to browser back/forward buttons
    DisposableEffect(navController) {
        val popStateListener: (Event) -> Unit = { _ ->
            val path = window.location.pathname
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            
            // Only navigate if we're not already on that route
            if (path != currentRoute) {
                navController.navigate(path) {
                    // Don't add to back stack since we're handling browser history
                    launchSingleTop = true
                }
            }
        }

        window.addEventListener("popstate", popStateListener)

        onDispose {
            window.removeEventListener("popstate", popStateListener)
        }
    }
}
