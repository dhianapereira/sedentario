package io.github.dhianapereira.sedentario.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLanguageTest {
    @Test
    fun `english language code selects english`() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromLanguageCode("en"))
    }

    @Test
    fun `portuguese language code selects portuguese`() {
        assertEquals(AppLanguage.PORTUGUESE, AppLanguage.fromLanguageCode("pt"))
    }

    @Test
    fun `unsupported language falls back to portuguese`() {
        assertEquals(AppLanguage.PORTUGUESE, AppLanguage.fromLanguageCode("es"))
    }
}
