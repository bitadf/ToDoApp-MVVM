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
import androidx.room.Index
import com.example.todoapp.R
import com.example.todoapp.adapter.HomeAdapter
import com.example.todoapp.databinding.FragmentActiveGridBinding
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GridActiveFragment : Fragment() {

    private lateinit var binding: FragmentActiveGridBinding
    private lateinit var taskList : ArrayList<TaskEntity>
    private lateinit var grid : GridView
    private val viewModel : TaskViewModel by viewModels()

    @Inject
    lateinit var task : TaskEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveGridBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        grid = binding.homeGridActive
        val adapter = HomeAdapter(requireContext() , ArrayList() , viewModel , parentFragmentManager , viewLifecycleOwner)
        grid.adapter = adapter
//        if(viewModel.isDatabaseEmpty()){
//            Toast.makeText(requireContext() , " Empty " , Toast.LENGTH_LONG).show()
//            goToFragment(EmptyFragment())
//        }
        viewModel.tasks.observe(viewLifecycleOwner){
            tasks ->
            val ActiveTasks = tasks.filter { taskEntity -> !taskEntity.completed }
            val taskAdapterList = ActiveTasks.map {
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

        grid.onItemClickListener = AdapterView.OnItemClickListener{_ , _ , position , _ ->
            val clickedItem = taskList[position]
        }

    }
    fun goToFragment(fragment: Fragment) {
        val bundle = Bundle()
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}