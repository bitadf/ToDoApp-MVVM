package com.example.todoapp.db

import android.webkit.WebSettings.RenderPriority
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.utils.Constants.TASK_TABLE

@Entity(tableName = TASK_TABLE)
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var title : String = "" ,
    var description : String = "No description",
    var startDate : String = "",
    var deadLine : String = "No deadline",
    var completed : Boolean = false,

)