package com.example.todoapp.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentShowBinding
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.utils.Constants.TASK_ID
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowFragment : DialogFragment() {
    lateinit var binding: FragmentShowBinding

    private val viewModel : TaskViewModel by viewModels()

    @Inject
    lateinit var task : TaskEntity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowBinding.inflate(layoutInflater , container , false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
        val id = arguments?.getInt(TASK_ID) ?: return

        viewModel.getTask(id).observe(viewLifecycleOwner, Observer{
            taskEntity ->
            if(taskEntity != null){
                binding.apply {
                    title.text = taskEntity.title
                    if(!taskEntity.completed){
                        activeText.text = "Status: Active"
                    }
                    else{
                        activeText.text = "Status: Completed"
                        checkbox.isChecked = true
                    }
                    deadlineText.text = taskEntity.deadLine
                    descriptionText.text = taskEntity.description
                    task = taskEntity
                }
            }
            else {
                Toast.makeText(context, "Task not found", Toast.LENGTH_SHORT).show()
            }
        })

        var state : Boolean
        binding.apply {
            checkbox.setOnCheckedChangeListener{buttonView , isChecked ->
                if(!isChecked){
                    activeText.text = "Status: Active"
                    state = false
                }
                else{
                    activeText.text = "Status: Completed"
                    state = true
                }
                task.completed = state
                viewModel.updateTask(task)
            }
            delete.setOnClickListener{
                viewModel.deleteTask(task)
                dismiss()
            }
            close.setOnClickListener{
                dismiss()
            }
            edit.setOnClickListener{
                val editFragment = EditFragment()
                val bundle = Bundle()
                bundle.putInt(TASK_ID , id)
                editFragment.arguments = bundle
                editFragment.show(parentFragmentManager , editFragment.tag)
                dismiss()

            }
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