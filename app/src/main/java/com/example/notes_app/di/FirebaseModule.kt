package com.example.notes_app.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    @Singleton
    fun providesRealtimeDb():DatabaseReference = Firebase.database.reference.child("user")

    @Singleton
    @Provides
    fun providesFirebaseAuth():FirebaseAuth=  Firebase.auth
}