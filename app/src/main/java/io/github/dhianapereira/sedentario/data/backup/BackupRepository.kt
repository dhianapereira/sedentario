package io.github.dhianapereira.sedentario.data.backup

interface BackupRepository {
    suspend fun countEntries(range: BackupDateRange): Int

    suspend fun exportTo(uri: String, range: BackupDateRange? = null): Int

    suspend fun importFrom(uri: String, mode: BackupImportMode): Int
}

data class BackupDateRange(
    val startEpochDay: Long,
    val endEpochDay: Long,
) {
    init {
        require(startEpochDay <= endEpochDay)
    }
}

enum class BackupImportMode { MERGE, REPLACE }

sealed class BackupException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class InvalidFile(cause: Throwable? = null) : BackupException("Invalid backup file", cause)
    class UnsupportedVersion : BackupException("Unsupported backup version")
    class TooManyEntries : BackupException("Backup contains too many entries")
    class CannotOpenFile(cause: Throwable? = null) : BackupException("Cannot open backup file", cause)
    class EmptyBackup : BackupException("Empty backup cannot replace current data")
}
