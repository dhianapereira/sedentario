package io.github.dhianapereira.sedentario.data.activity

import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomActivityEntryRepository @Inject constructor(
    private val dao: ActivityEntryDao,
) : ActivityEntryRepository {
    override val entries: Flow<Map<LocalDate, WorkoutActivity>> = dao.observeEntries()
        .map { entities -> entities.mapNotNull(ActivityEntryEntity::toEntry).toMap() }

    override suspend fun saveEntry(date: LocalDate, activity: WorkoutActivity) {
        dao.upsertEntry(ActivityEntryEntity.from(date, activity))
    }

    override suspend fun deleteEntry(date: LocalDate) {
        dao.deleteEntry(date.toEpochDay())
    }
}
