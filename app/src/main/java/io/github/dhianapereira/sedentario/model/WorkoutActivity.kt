package io.github.dhianapereira.sedentario.model

enum class WorkoutActivity(
    val emoji: String,
    val description: String,
) {
    GYM("🏋️", "Academia"),
    RUN("🏃", "Corrida"),
    SOCCER("⚽", "Futebol"),
    WALK("🚶", "Caminhada"),
    MEDITATION("🧘", "Meditação"),
    BAD_SLEEP("😴", "Sono ruim"),
    REST("🛌", "Descanso"),
    MISSED("❌", "Não fui"),
}
