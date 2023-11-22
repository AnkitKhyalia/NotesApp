package com.example.notes_app.firebaseauth.repository

import android.util.Log
import com.example.notes_app.firebaseauth.AuthUser
import com.example.notes_app.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authdb:FirebaseAuth
):AuthRepository {
    override fun createUser(authUser: AuthUser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        authdb.createUserWithEmailAndPassword(
            authUser.email!!,
            authUser.password!!
        ).addOnCompleteListener{
                if(it.isSuccessful){
                    trySend(ResultState.Success("User Created Successfully"))
                    Log.d("main","current user id :${authdb.currentUser?.uid}")
                }
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))

        }
        awaitClose{
            close()
        }
    }

    override fun longinUser(authUser: AuthUser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        authdb.signInWithEmailAndPassword(
            authUser.email!!,
            authUser.password!!
        ).addOnCompleteListener{
            trySend(ResultState.Success("login Successful"))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))

        }
        awaitClose{
            close()
        }
    }
}