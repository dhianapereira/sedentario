package io.github.dhianapereira.sedentario.data.activity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityEntryDao {
    @Query("SELECT * FROM activity_entries ORDER BY dateEpochDay")
    fun observeEntries(): Flow<List<ActivityEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEntry(entry: ActivityEntryEntity)

    @Query("DELETE FROM activity_entries WHERE dateEpochDay = :dateEpochDay")
    suspend fun deleteEntry(dateEpochDay: Long)
}
