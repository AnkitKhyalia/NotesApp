package com.example.notes_app.firebaseRealTimeDb.repository

import com.example.notes_app.firebaseRealTimeDb.RealtimeModelResponse
import com.example.notes_app.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {
    fun insert(
        item:RealtimeModelResponse.RealtimeItems
    ): Flow<ResultState<String>>
    fun getItems():Flow<ResultState<List<RealtimeModelResponse>>>
    fun delete(key:String):Flow<ResultState<String>>
    fun update(
        res:RealtimeModelResponse
    ):Flow<ResultState<String>>
}