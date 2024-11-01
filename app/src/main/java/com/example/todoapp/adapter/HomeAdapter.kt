package com.example.todoapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.todoapp.R
import com.example.todoapp.Test
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.todoapp.Fragments.EditFragment
import com.example.todoapp.Fragments.ShowFragment
import com.example.todoapp.utils.Constants.TASK_ID
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeAdapter(var context: Context ,
                  val task_list : ArrayList<TaskEntity> ,
                  private val taskViewModel: TaskViewModel ,
                  private val fragmentManager: FragmentManager,
                  private val lifecycleOwner: LifecycleOwner):
    BaseAdapter(){
        private var layoutInflater : LayoutInflater? = null
        private lateinit var title: TextView
        private lateinit var description : TextView
//        private lateinit var activeText : TextView
        private lateinit var activeImage : ImageView


    override fun getCount(): Int {
        return task_list.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
       return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.task_card, null)
        }
        title = convertView!!.findViewById(R.id.title_text)
        description = convertView!!.findViewById(R.id.description_text)
//        activeText = convertView!!.findViewById(R.id.active_text)
        activeImage = convertView!!.findViewById(R.id.active_image)


        title.text = task_list[position].title
        description.text = task_list[position].description
        if(task_list[position].completed){
//            activeText.text = "Completed"
            activeImage.setImageResource(R.drawable.ic_check_three)
        }
        else{
//            activeText.text = "Active"
            activeImage.setImageResource(R.drawable.ic_unchecked)
        }
        /////////////
        val delete_btn = convertView!!.findViewById<ImageButton>(R.id.delete_btn)
        val share_btn = convertView!!.findViewById<ImageButton>(R.id.share_btn)
        val more_btn = convertView!!.findViewById<ImageButton>(R.id.more_btn)

        ///make a popup for more
        val popupMenu = PopupMenu(context , more_btn)
        popupMenu.inflate(R.menu.popup_menu)
        // on item click
        val showFragment = ShowFragment()
        val bundle = Bundle()
        title.setOnClickListener {
            bundle.putInt(TASK_ID , task_list[position].id)
            showFragment.arguments = bundle
            showFragment.show(fragmentManager , showFragment.tag)
        }
        description.setOnClickListener{
            bundle.putInt(TASK_ID , task_list[position].id)
            showFragment.arguments = bundle
            showFragment.show(fragmentManager , showFragment.tag)
        }

        delete_btn.setOnClickListener{
            MaterialAlertDialogBuilder(context)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete"){
                    dialog , _ ->
                    taskViewModel.deleteTask(task_list[position])
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel"){
                    dialog , _ ->
                    dialog.dismiss()
                }.show()

        }

        share_btn.setOnClickListener{
            var taskText = ""

            taskViewModel.getTask(task_list[position].id).observe(lifecycleOwner, Observer {
                taskEntity ->
                if(taskEntity != null){
                taskText += "Title : " + taskEntity.title + "\n" +
                        "Description :"+ taskEntity.description + "\n" +
                        "Start Date : "+ taskEntity.startDate + "\n" +
                        "DeadLine : "+ taskEntity.deadLine + "\n"

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,taskText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
                }
                else{
                    Toast.makeText(context , "Database Error : null task" , Toast.LENGTH_SHORT).show()
                }
            })




        }

        more_btn.setOnClickListener{
            popupMenu.show()
        }
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.pop_up_edit -> {
                    val editFragment = EditFragment()
                    bundle.putInt(TASK_ID , task_list[position].id)
                    editFragment.arguments = bundle
                    editFragment.show(fragmentManager , editFragment.tag)
                    true
                }
                R.id.pop_up_show -> {
                    bundle.putInt(TASK_ID , task_list[position].id)
                    showFragment.arguments = bundle
                    showFragment.show(fragmentManager , showFragment.tag)
                    true
                }
                R.id.pop_up_complete ->{
                    taskViewModel.getTask(task_list[position].id).observe(lifecycleOwner , Observer {
                        taskEntity ->

                        taskViewModel.updateTask(TaskEntity(taskEntity.id ,
                                                            taskEntity.title,
                                                            taskEntity.description ,
                                                            taskEntity.startDate,
                                                            taskEntity.deadLine ,
                                                            true))
                    })
                    true
                }
                else -> false
            }
        }
        return convertView


    }
fun updateData(newTasks: List<TaskEntity>) {
    task_list.clear()  // Clear the existing list
    task_list.addAll(newTasks)  // Add the new tasks to the list
    notifyDataSetChanged()  // Notify the adapter to refresh the data
}
}