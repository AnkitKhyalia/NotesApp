package com.example.notes_app.firebaseauth.ui

import androidx.lifecycle.ViewModel
import com.example.notes_app.firebaseauth.AuthUser
import com.example.notes_app.firebaseauth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo:AuthRepository
) :ViewModel(){

    fun createUser(authUser: AuthUser)= repo.createUser(authUser)
    fun loginUser(authUser: AuthUser)= repo.longinUser(authUser)
}