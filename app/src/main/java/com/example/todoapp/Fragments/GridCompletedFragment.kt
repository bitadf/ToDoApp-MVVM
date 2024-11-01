package com.example.todoapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.todoapp.R
import com.example.todoapp.Test
import com.example.todoapp.adapter.HomeAdapter
import com.example.todoapp.databinding.FragmentGridBinding
import com.example.todoapp.databinding.FragmentGridCompletedBinding
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class GridCompletedFragment : Fragment() {

    private lateinit var binding : FragmentGridCompletedBinding
    private lateinit var taskList: ArrayList<TaskEntity>
    private lateinit var grid: GridView
    private val viewModel : TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGridCompletedBinding.inflate(layoutInflater , container , false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        grid = binding.homeGridCompleted
        val adapter = HomeAdapter(requireContext(), ArrayList() , viewModel , parentFragmentManager , viewLifecycleOwner)
        grid.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner){
            taskList ->
            val completedTasks = taskList.filter { taskEntity -> taskEntity.completed }
            val taskAdapterList = completedTasks.map {
                taskEntity ->
                TaskEntity(
                    taskEntity.id,
                    taskEntity.title,
                    taskEntity.description,
                    taskEntity.startDate,
                    taskEntity.deadLine,
                    taskEntity.completed
                )
            }

            adapter.updateData(taskAdapterList)

        }
        viewModel.refreshTasks()


        grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val clickedItem = taskList[position]
            // Handle the click event here
            Toast.makeText(requireContext(), "Clicked: ${clickedItem.title}", Toast.LENGTH_SHORT).show()
            // You can start a new activity, show a dialog, or perform any other action
        }
    }
}