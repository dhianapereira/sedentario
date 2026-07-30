package io.github.dhianapereira.sedentario.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.dhianapereira.sedentario.data.backup.BackupRepository
import io.github.dhianapereira.sedentario.data.backup.JsonBackupRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BackupModule {
    @Binds
    @Singleton
    abstract fun bindBackupRepository(repository: JsonBackupRepository): BackupRepository
}
