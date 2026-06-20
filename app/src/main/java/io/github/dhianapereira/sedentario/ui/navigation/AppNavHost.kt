package io.github.dhianapereira.sedentario.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.settings.SettingsScreen
import io.github.dhianapereira.sedentario.ui.tracker.TrackerRoute

private const val TRACKER_ROUTE = "tracker"
private const val SETTINGS_ROUTE = "settings"

@Composable
fun AppNavHost(
    appTheme: AppTheme,
    appLanguage: AppLanguage,
    onThemeSelected: (AppTheme) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TRACKER_ROUTE,
    ) {
        composable(TRACKER_ROUTE) {
            TrackerRoute(
                onSettingsClick = { navController.navigate(SETTINGS_ROUTE) },
            )
        }
        composable(SETTINGS_ROUTE) {
            SettingsScreen(
                appTheme = appTheme,
                appLanguage = appLanguage,
                onBackClick = navController::navigateUp,
                onThemeSelected = onThemeSelected,
                onLanguageSelected = onLanguageSelected,
            )
        }
    }
}
