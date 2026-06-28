package io.github.dhianapereira.sedentario.data.activity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.time.LocalDate

@Entity(tableName = "activity_entries")
data class ActivityEntryEntity(
    @PrimaryKey val dateEpochDay: Long,
    val activityName: String,
) {
    fun toEntry(): Pair<LocalDate, WorkoutActivity>? {
        val activity = WorkoutActivity.entries.find { it.name == activityName } ?: return null
        return LocalDate.ofEpochDay(dateEpochDay) to activity
    }

    companion object {
        fun from(date: LocalDate, activity: WorkoutActivity): ActivityEntryEntity {
            return ActivityEntryEntity(
                dateEpochDay = date.toEpochDay(),
                activityName = activity.name,
            )
        }
    }
}
