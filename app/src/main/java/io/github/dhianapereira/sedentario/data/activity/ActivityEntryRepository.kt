package io.github.dhianapereira.sedentario.data.activity

import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface ActivityEntryRepository {
    val entries: Flow<Map<LocalDate, WorkoutActivity>>

    suspend fun saveEntry(date: LocalDate, activity: WorkoutActivity)

    suspend fun deleteEntry(date: LocalDate)
}
