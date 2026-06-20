package io.github.dhianapereira.sedentario.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.dhianapereira.sedentario.data.preferences.DataStoreThemePreferencesRepository
import io.github.dhianapereira.sedentario.data.preferences.ThemePreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {
    @Binds
    @Singleton
    abstract fun bindThemePreferencesRepository(
        repository: DataStoreThemePreferencesRepository,
    ): ThemePreferencesRepository
}
