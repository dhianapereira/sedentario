package io.github.dhianapereira.sedentario.data.backup

import android.content.Context
import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.net.toUri
import io.github.dhianapereira.sedentario.data.activity.ActivityEntryDao
import io.github.dhianapereira.sedentario.data.activity.ActivityEntryEntity
import io.github.dhianapereira.sedentario.model.WorkoutActivity
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class JsonBackupRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val activityEntryDao: ActivityEntryDao,
) : BackupRepository {
    override suspend fun countEntries(range: BackupDateRange): Int = withContext(Dispatchers.IO) {
        activityEntryDao.countEntriesBetween(range.startEpochDay, range.endEpochDay)
    }

    override suspend fun exportTo(uri: String, range: BackupDateRange?): Int = withContext(Dispatchers.IO) {
        val documentUri = uri.toUri()
        val entries = range?.let {
            activityEntryDao.getEntriesBetween(it.startEpochDay, it.endEpochDay)
        } ?: activityEntryDao.getAllEntries()
        try {
            val output = context.contentResolver.openOutputStream(documentUri, "wt")
                ?: throw BackupException.CannotOpenFile()
            output.use { stream ->
                JsonWriter(OutputStreamWriter(BufferedOutputStream(stream), Charsets.UTF_8)).use { writer ->
                    writer.setIndent("  ")
                    writer.beginObject()
                    writer.name("format").value(FORMAT)
                    writer.name("version").value(VERSION.toLong())
                    writer.name("exportedAt").value(Instant.now().toString())
                    writer.name("entries").beginArray()
                    entries.forEach { entry ->
                        coroutineContext.ensureActive()
                        writer.beginObject()
                        writer.name("dateEpochDay").value(entry.dateEpochDay)
                        writer.name("activity").value(entry.activityName)
                        writer.endObject()
                    }
                    writer.endArray()
                    writer.endObject()
                }
            }
            entries.size
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: BackupException) {
            throw exception
        } catch (exception: IOException) {
            throw BackupException.CannotOpenFile(exception)
        } catch (exception: SecurityException) {
            throw BackupException.CannotOpenFile(exception)
        }
    }

    override suspend fun importFrom(uri: String, mode: BackupImportMode): Int = withContext(Dispatchers.IO) {
        val documentUri = uri.toUri()
        val entries = try {
            val input = context.contentResolver.openInputStream(documentUri)
                ?: throw BackupException.CannotOpenFile()
            input.use { stream ->
                val reader = JsonReader(InputStreamReader(BufferedInputStream(stream), Charsets.UTF_8))
                try {
                    readBackup(reader)
                } finally {
                    reader.close()
                }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: BackupException) {
            throw exception
        } catch (exception: SecurityException) {
            throw BackupException.CannotOpenFile(exception)
        } catch (exception: Exception) {
            throw BackupException.InvalidFile(exception)
        }
        coroutineContext.ensureActive()
        if (mode == BackupImportMode.REPLACE && entries.isEmpty()) {
            throw BackupException.EmptyBackup()
        }
        when (mode) {
            BackupImportMode.MERGE -> activityEntryDao.mergeEntries(entries)
            BackupImportMode.REPLACE -> activityEntryDao.replaceAllEntries(entries)
        }
        entries.size
    }

    private suspend fun readBackup(reader: JsonReader): List<ActivityEntryEntity> {
        var format: String? = null
        var version: Int? = null
        var entries: List<ActivityEntryEntity>? = null
        reader.beginObject()
        while (reader.hasNext()) {
            coroutineContext.ensureActive()
            when (reader.nextName()) {
                "format" -> format = reader.nextString()
                "version" -> version = reader.nextInt()
                "entries" -> entries = readEntries(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        if (reader.peek() != JsonToken.END_DOCUMENT || format != FORMAT) {
            throw BackupException.InvalidFile()
        }
        if (version != VERSION) throw BackupException.UnsupportedVersion()
        return entries ?: throw BackupException.InvalidFile()
    }

    private suspend fun readEntries(reader: JsonReader): List<ActivityEntryEntity> {
        val result = LinkedHashMap<Long, ActivityEntryEntity>()
        reader.beginArray()
        while (reader.hasNext()) {
            coroutineContext.ensureActive()
            if (result.size >= MAX_ENTRIES) throw BackupException.TooManyEntries()
            var date: Long? = null
            var activityName: String? = null
            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "dateEpochDay" -> date = reader.nextLong()
                    "activity" -> activityName = reader.nextString()
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
            val epochDay = date ?: throw BackupException.InvalidFile()
            val activity = activityName?.let { name ->
                WorkoutActivity.entries.find { it.name == name }
            } ?: throw BackupException.InvalidFile()
            if (epochDay !in MIN_EPOCH_DAY..MAX_EPOCH_DAY || result.containsKey(epochDay)) {
                throw BackupException.InvalidFile()
            }
            result[epochDay] = ActivityEntryEntity(epochDay, activity.name)
        }
        reader.endArray()
        return result.values.toList()
    }

    private companion object {
        const val FORMAT = "sedentario-backup"
        const val VERSION = 1
        const val MAX_ENTRIES = 1_000_000
        const val MIN_EPOCH_DAY = -365_243_219_162L
        const val MAX_EPOCH_DAY = 365_241_780_471L
    }
}
