package com.example.todoapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todoapp.utils.Constants.TASK_TABLE
import kotlinx.coroutines.flow.Flow
import java.security.KeyStore.TrustedCertificateEntry


@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(task: TaskEntity)
    @Update
    suspend fun updateTask(task: TaskEntity)
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM $TASK_TABLE WHERE id ==:id")
    fun getTask(id : Int) : Flow<TaskEntity>

    @Query("SELECT * FROM $TASK_TABLE ")
    fun getAllTasks() : Flow<MutableList<TaskEntity>>



    @Query("SELECT * FROM $TASK_TABLE WHERE title LIKE '%' || :query ||'%'")
    fun searchTasks(query : String): Flow<MutableList<TaskEntity>>

//    @Query("SELECT COUNT(*) FROM $TASK_TABLE")
//    fun getTaskCountLive(): LiveData<Int>
//
//    // Use LiveData<Boolean> to observe changes
//    fun isDatabaseEmpty(): LiveData<Boolean> {
//        return Transformations.map(getTaskCountLive()) { count ->
//            count == 0
//        }
//    }


}