package com.example.notes_app.di

import com.example.notes_app.firebaseRealTimeDb.repository.RealtimeDbRespository
import com.example.notes_app.firebaseRealTimeDb.repository.RealtimeRepository
import com.example.notes_app.firebaseauth.repository.AuthRepository
import com.example.notes_app.firebaseauth.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract  class RepositoryModule {
    @Binds
    abstract fun providesRealtimeRepository(
        repo:RealtimeDbRespository
    ):RealtimeRepository

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ):AuthRepository
}