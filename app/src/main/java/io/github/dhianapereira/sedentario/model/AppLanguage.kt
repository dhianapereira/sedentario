package io.github.dhianapereira.sedentario.model

enum class AppLanguage(val languageTag: String) {
    PORTUGUESE("pt-BR"),
    ENGLISH("en"),
    ;

    companion object {
        fun fromLanguageCode(languageCode: String): AppLanguage {
            return if (languageCode == "en") ENGLISH else PORTUGUESE
        }
    }
}
