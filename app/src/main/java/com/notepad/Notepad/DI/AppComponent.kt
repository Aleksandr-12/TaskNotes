package com.notepad.Notepad.DI

import com.notepad.Notepad.Retrofit.Repository.TasksRepository
import com.notepad.Notepad.Retrofit.RetrofitApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun retrofit(): RetrofitApi
    fun tasksRepository(): TasksRepository
}