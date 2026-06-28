package io.github.dhianapereira.sedentario.model

import androidx.annotation.StringRes
import io.github.dhianapereira.sedentario.R

enum class WorkoutActivity(
    val emoji: String,
    @param:StringRes val labelRes: Int,
    val category: WorkoutActivityCategory,
) {
    GYM("🏋️", R.string.activity_gym, WorkoutActivityCategory.MOVEMENT),
    RUN("🏃", R.string.activity_run, WorkoutActivityCategory.MOVEMENT),
    WALK("🚶", R.string.activity_walk, WorkoutActivityCategory.MOVEMENT),
    BIKE("🚴", R.string.activity_bike, WorkoutActivityCategory.MOVEMENT),
    SWIM("🏊", R.string.activity_swim, WorkoutActivityCategory.MOVEMENT),
    DANCE("💃", R.string.activity_dance, WorkoutActivityCategory.MOVEMENT),
    HOME_WORKOUT("🏠", R.string.activity_home_workout, WorkoutActivityCategory.MOVEMENT),
    SOCCER("⚽", R.string.activity_soccer, WorkoutActivityCategory.SPORTS),
    BASKETBALL("🏀", R.string.activity_basketball, WorkoutActivityCategory.SPORTS),
    VOLLEYBALL("🏐", R.string.activity_volleyball, WorkoutActivityCategory.SPORTS),
    TENNIS("🎾", R.string.activity_tennis, WorkoutActivityCategory.SPORTS),
    BOXING("🥊", R.string.activity_boxing, WorkoutActivityCategory.SPORTS),
    HIKE("🥾", R.string.activity_hike, WorkoutActivityCategory.SPORTS),
    SKATE("🛼", R.string.activity_skate, WorkoutActivityCategory.SPORTS),
    YOGA("🧘", R.string.activity_yoga, WorkoutActivityCategory.WELLBEING),
    PILATES("🤸", R.string.activity_pilates, WorkoutActivityCategory.WELLBEING),
    STRETCHING("🙆", R.string.activity_stretching, WorkoutActivityCategory.WELLBEING),
    MEDITATION("🧘", R.string.activity_meditation, WorkoutActivityCategory.WELLBEING),
    PERIOD("🩸", R.string.activity_period, WorkoutActivityCategory.MENSTRUAL_CYCLE),
    CRAMPS("🤕", R.string.activity_cramps, WorkoutActivityCategory.MENSTRUAL_CYCLE),
    PMS("🌧️", R.string.activity_pms, WorkoutActivityCategory.MENSTRUAL_CYCLE),
    SICK("🤒", R.string.activity_sick, WorkoutActivityCategory.REST_AND_REASONS),
    INJURY("🩹", R.string.activity_injury, WorkoutActivityCategory.REST_AND_REASONS),
    BAD_SLEEP("😴", R.string.activity_bad_sleep, WorkoutActivityCategory.REST_AND_REASONS),
    TIRED("🥱", R.string.activity_tired, WorkoutActivityCategory.REST_AND_REASONS),
    BUSY("📚", R.string.activity_busy, WorkoutActivityCategory.REST_AND_REASONS),
    WORK("💻", R.string.activity_work, WorkoutActivityCategory.REST_AND_REASONS),
    RAIN("🌧️", R.string.activity_rain, WorkoutActivityCategory.REST_AND_REASONS),
    TRAVEL("🧳", R.string.activity_travel, WorkoutActivityCategory.REST_AND_REASONS),
    REST("🛌", R.string.activity_rest, WorkoutActivityCategory.REST_AND_REASONS),
    MISSED("❌", R.string.activity_missed, WorkoutActivityCategory.REST_AND_REASONS),
}

enum class WorkoutActivityCategory(
    @param:StringRes val labelRes: Int,
) {
    MOVEMENT(R.string.activity_category_movement),
    SPORTS(R.string.activity_category_sports),
    WELLBEING(R.string.activity_category_wellbeing),
    MENSTRUAL_CYCLE(R.string.activity_category_menstrual_cycle),
    REST_AND_REASONS(R.string.activity_category_rest_and_reasons),
}
