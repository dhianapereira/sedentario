package io.github.dhianapereira.sedentario.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppThemeTest {
    @Test
    fun `stored values restore their themes`() {
        AppTheme.entries.forEach { theme ->
            assertEquals(theme, AppTheme.fromStorageValue(theme.name))
        }
    }

    @Test
    fun `missing stored value falls back to dark`() {
        assertEquals(AppTheme.DARK, AppTheme.fromStorageValue(null))
    }

    @Test
    fun `invalid stored value falls back to dark`() {
        assertEquals(AppTheme.DARK, AppTheme.fromStorageValue("INVALID"))
    }
}
