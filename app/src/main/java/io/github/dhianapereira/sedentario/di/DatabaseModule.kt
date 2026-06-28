package io.github.dhianapereira.sedentario.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.dhianapereira.sedentario.data.activity.ActivityEntryDao
import io.github.dhianapereira.sedentario.data.database.SedentarioDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): SedentarioDatabase {
        return Room.databaseBuilder(
            context,
            SedentarioDatabase::class.java,
            "sedentario.db",
        ).build()
    }

    @Provides
    fun provideActivityEntryDao(database: SedentarioDatabase): ActivityEntryDao {
        return database.activityEntryDao()
    }
}
