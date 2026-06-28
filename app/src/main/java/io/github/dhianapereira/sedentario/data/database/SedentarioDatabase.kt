package io.github.dhianapereira.sedentario.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.dhianapereira.sedentario.data.activity.ActivityEntryDao
import io.github.dhianapereira.sedentario.data.activity.ActivityEntryEntity

@Database(
    entities = [ActivityEntryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class SedentarioDatabase : RoomDatabase() {
    abstract fun activityEntryDao(): ActivityEntryDao
}
