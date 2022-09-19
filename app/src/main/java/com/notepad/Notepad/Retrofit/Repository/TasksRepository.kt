package com.notepad.Notepad.Retrofit.Repository
import com.notepad.Notepad.Retrofit.Data.Tasks
import com.notepad.Notepad.Retrofit.RetrofitApi

class TasksRepository(private val retrofitApi: RetrofitApi) {

    suspend fun getTasksList(user_id:Int): MutableList<Tasks> = retrofitApi.getTasksList(user_id).await()
}