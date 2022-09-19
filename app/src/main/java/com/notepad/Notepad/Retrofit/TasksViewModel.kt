package com.notepad.Notepad.Retrofit

import androidx.lifecycle.*
import com.notepad.Notepad.DI.DaggerAppComponent
import com.notepad.Notepad.Retrofit.Data.Tasks
import com.notepad.Notepad.Retrofit.Repository.TasksRepository
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class TasksViewModel: ViewModel() {
    private val tasksRepository: TasksRepository = DaggerAppComponent.create().tasksRepository()
    private val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    val tasksList = MutableLiveData<List<Tasks>>()
    fun getTasksList(user_id:Int) {
        job  = CoroutineScope(Dispatchers.IO).launch {
            val response = tasksRepository.getTasksList(user_id)
            withContext(Dispatchers.Main) {
                tasksList.postValue(response)
            }
        }
    }
    private fun onError(message: String) {
        errorMessage.value = message
        //loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
class TasksViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}