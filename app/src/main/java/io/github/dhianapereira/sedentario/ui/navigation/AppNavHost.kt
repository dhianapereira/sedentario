package io.github.dhianapereira.sedentario.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.model.AppAccentColor
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.settings.SettingsRoute
import io.github.dhianapereira.sedentario.ui.tracker.TrackerRoute

private const val TRACKER_ROUTE = "tracker"
private const val SETTINGS_ROUTE = "settings"
private const val TRANSITION_DURATION_MILLIS = 160

@Composable
fun AppNavHost(
    appTheme: AppTheme,
    accentColor: AppAccentColor,
    appLanguage: AppLanguage,
    onThemeSelected: (AppTheme) -> Unit,
    onColorSelected: (AppAccentColor) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TRACKER_ROUTE,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        enterTransition = {
            fadeIn(animationSpec = tween(TRANSITION_DURATION_MILLIS))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(TRANSITION_DURATION_MILLIS))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(TRANSITION_DURATION_MILLIS))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(TRANSITION_DURATION_MILLIS))
        },
    ) {
        composable(TRACKER_ROUTE) {
            TrackerRoute(
                onSettingsClick = { navController.navigate(SETTINGS_ROUTE) },
            )
        }
        composable(SETTINGS_ROUTE) {
            SettingsRoute(
                appTheme = appTheme,
                accentColor = accentColor,
                appLanguage = appLanguage,
                onBackClick = navController::navigateUp,
                onThemeSelected = onThemeSelected,
                onColorSelected = onColorSelected,
                onLanguageSelected = onLanguageSelected,
            )
        }
    }
}
