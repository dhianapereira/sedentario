package io.github.dhianapereira.sedentario.model

enum class AppAccentColor {
    GREEN,
    BLUE,
    ORANGE,
    PURPLE,
    ;

    companion object {
        fun fromStorageValue(value: String?): AppAccentColor {
            return entries.firstOrNull { it.name == value } ?: PURPLE
        }
    }
}
