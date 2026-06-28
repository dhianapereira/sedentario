package io.github.dhianapereira.sedentario.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.dhianapereira.sedentario.data.activity.ActivityEntryRepository
import io.github.dhianapereira.sedentario.data.activity.RoomActivityEntryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityEntryModule {
    @Binds
    @Singleton
    abstract fun bindActivityEntryRepository(
        repository: RoomActivityEntryRepository,
    ): ActivityEntryRepository
}
