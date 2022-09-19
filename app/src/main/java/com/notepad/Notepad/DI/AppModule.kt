package com.notepad.Notepad.DI

import com.notepad.Notepad.Retrofit.Common.Common
import com.notepad.Notepad.Retrofit.Repository.TasksRepository
import com.notepad.Notepad.Retrofit.RetrofitApi
import dagger.Module
import dagger.Provides
import org.jetbrains.annotations.NotNull
import javax.inject.Singleton

@Module
class AppModule() {
    @Provides  @Singleton @NotNull
    fun tasksRepository(): TasksRepository {
        return TasksRepository(retrofit())
    }

    @Provides  @Singleton
    fun retrofit(): RetrofitApi {
        return Common.retrofitApi
    }
}