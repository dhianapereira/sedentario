package io.github.dhianapereira.sedentario.data.activity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityEntryDao {
    @Query("SELECT * FROM activity_entries ORDER BY dateEpochDay")
    fun observeEntries(): Flow<List<ActivityEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEntry(entry: ActivityEntryEntity)

    @Query("DELETE FROM activity_entries WHERE dateEpochDay = :dateEpochDay")
    suspend fun deleteEntry(dateEpochDay: Long)

    @Query("SELECT * FROM activity_entries ORDER BY dateEpochDay")
    suspend fun getAllEntries(): List<ActivityEntryEntity>

    @Query(
        "SELECT * FROM activity_entries " +
            "WHERE dateEpochDay BETWEEN :startEpochDay AND :endEpochDay ORDER BY dateEpochDay",
    )
    suspend fun getEntriesBetween(
        startEpochDay: Long,
        endEpochDay: Long,
    ): List<ActivityEntryEntity>

    @Query(
        "SELECT COUNT(*) FROM activity_entries " +
            "WHERE dateEpochDay BETWEEN :startEpochDay AND :endEpochDay",
    )
    suspend fun countEntriesBetween(startEpochDay: Long, endEpochDay: Long): Int

    @Query("DELETE FROM activity_entries")
    suspend fun deleteAllEntries()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<ActivityEntryEntity>)

    @Transaction
    suspend fun replaceAllEntries(entries: List<ActivityEntryEntity>) {
        deleteAllEntries()
        entries.chunked(500).forEach { insertEntries(it) }
    }

    @Transaction
    suspend fun mergeEntries(entries: List<ActivityEntryEntity>) {
        entries.chunked(500).forEach { insertEntries(it) }
    }
}
