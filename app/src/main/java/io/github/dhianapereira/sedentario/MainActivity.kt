package io.github.dhianapereira.sedentario

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.dhianapereira.sedentario.model.AppLanguage
import io.github.dhianapereira.sedentario.model.AppTheme
import io.github.dhianapereira.sedentario.ui.navigation.AppNavHost
import io.github.dhianapereira.sedentario.ui.theme.SedentarioTheme
import io.github.dhianapereira.sedentario.ui.theme.ThemeViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appTheme by themeViewModel.appTheme.collectAsStateWithLifecycle()
            val appLanguage = AppLanguage.fromLanguageCode(
                LocalConfiguration.current.locales[0].language,
            )
            val isDarkTheme = when (appTheme) {
                AppTheme.SYSTEM -> isSystemInDarkTheme()
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
            }

            SideEffect {
                val systemBarStyle = if (isDarkTheme) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(
                    statusBarStyle = systemBarStyle,
                    navigationBarStyle = systemBarStyle,
                )
            }

            SedentarioTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavHost(
                        appTheme = appTheme,
                        appLanguage = appLanguage,
                        onThemeSelected = themeViewModel::setTheme,
                        onLanguageSelected = { language ->
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(language.languageTag),
                            )
                        },
                    )
                }
            }
        }
    }
}
