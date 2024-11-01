package com.example.todoapp.repository

import com.example.todoapp.db.TaskDao
import com.example.todoapp.db.TaskEntity
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val dao : TaskDao)
{
    suspend fun saveTask(task : TaskEntity) = dao.saveTask(task)

    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)

    fun getTask(id : Int) = dao.getTask(id)

    fun getAllTasks() = dao.getAllTasks()

    fun searchTask(query : String) = dao.searchTasks(query)

//    fun isDatabaseEmpty() = dao.isDatabaseEmpty()


}