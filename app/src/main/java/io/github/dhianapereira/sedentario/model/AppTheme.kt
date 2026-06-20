package io.github.dhianapereira.sedentario.model

enum class AppTheme {
    SYSTEM,
    DARK,
    LIGHT,
    ;

    companion object {
        fun fromStorageValue(value: String?): AppTheme {
            return entries.firstOrNull { it.name == value } ?: DARK
        }
    }
}
