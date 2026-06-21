package io.github.dhianapereira.sedentario.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppAccentColorTest {
    @Test
    fun `stored values restore their accent colors`() {
        AppAccentColor.entries.forEach { color ->
            assertEquals(color, AppAccentColor.fromStorageValue(color.name))
        }
    }

    @Test
    fun `missing or invalid value falls back to purple`() {
        assertEquals(AppAccentColor.PURPLE, AppAccentColor.fromStorageValue(null))
        assertEquals(AppAccentColor.PURPLE, AppAccentColor.fromStorageValue("INVALID"))
    }
}
