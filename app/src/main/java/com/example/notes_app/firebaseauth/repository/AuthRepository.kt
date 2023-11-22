package com.example.notes_app.firebaseauth.repository

import com.example.notes_app.firebaseauth.AuthUser
import com.example.notes_app.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {


    fun createUser(authUser: AuthUser): Flow<ResultState<String>>
    fun longinUser(authUser: AuthUser) :Flow<ResultState<String>>
}