package com.example.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.repository.DatabaseRepository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: DatabaseRepository)
    :ViewModel(){

    private val _tasks = MutableLiveData<MutableList<TaskEntity>>(mutableListOf())
    val tasks: LiveData<MutableList<TaskEntity>> get() = _tasks

        fun saveTask(task : TaskEntity){
            viewModelScope.launch {
                repository.saveTask(task)
            }
        }
        fun deleteTask(task: TaskEntity){
            viewModelScope.launch {
                repository.deleteTask(task)
            }
        }

        fun updateTask(task: TaskEntity) {
            viewModelScope.launch {
                repository.updateTask(task)
                //refresh task
                //refreshTasks()
            }
        }

        fun getTask(id : Int) : LiveData<TaskEntity> {
            return repository.getTask(id).asLiveData()
        }

        fun searchTask(query : String):LiveData<List<TaskEntity>> {
            return repository.searchTask(query).asLiveData()
        }
//        fun isDatabaseEmpty() = repository.isDatabaseEmpty()

     fun refreshTasks(){
            viewModelScope.launch {
                repository.getAllTasks().collect{
                    tasksList ->
                    _tasks.value = tasksList
                }
            }
        }
    init {
        refreshTasks()
    }

}