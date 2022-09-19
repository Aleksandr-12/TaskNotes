package com.notepad.Notepad.Retrofit

import com.notepad.Notepad.Retrofit.Data.Register
import com.notepad.Notepad.Retrofit.Data.Success
import com.notepad.Notepad.Retrofit.Data.Tasks
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitApi {
    @POST("json")
    @FormUrlEncoded
    fun getTasksList(@Field("user_id") user_id: Int): Deferred<MutableList<Tasks>>

    @POST("json")
    @FormUrlEncoded
    fun getTasksListCall(@Field("user_id") user_id: Int): Call<MutableList<Tasks>>

    @POST("addNote")
    @FormUrlEncoded
    fun addTask(@Field("user_id") user_id: Int,
                @Field("title") name: String,
                @Field("note") text: String): Call<Success>

    @POST("complete")
    @FormUrlEncoded
    fun completeTask(@Field("task_id") task_id: Int): Call<Success>

    @POST("delete")
    @FormUrlEncoded
    fun deleteTask(@Field("task_id") task_id: Int): Call<Success>

    @POST("registration")
    @FormUrlEncoded
    fun registerUser(@Field("name") name: String,
                     @Field("password") password: String,
                     @Field("email") email: String,): Call<Register>

    @POST("authorization")
    @FormUrlEncoded
    fun authorizationUser(@Field("name") name: String,
                     @Field("password") password: String): Call<Register>
}