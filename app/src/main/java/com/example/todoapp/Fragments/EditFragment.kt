package com.example.todoapp.Fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentEditBinding
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.utils.Constants.TASK_ID
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditFragment : DialogFragment() {

    lateinit var binding : FragmentEditBinding

    @Inject
    lateinit var task : TaskEntity

    private val viewModel : TaskViewModel by viewModels()

    lateinit var deadLine : String
    var deadlineFlag : Int = 0
    var state : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(layoutInflater , container , false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(TASK_ID) ?: return

        viewModel.getTask(id).observe(viewLifecycleOwner, Observer { taskEntity ->
            if (taskEntity != null) {
                task = taskEntity
                binding.apply {
                    title.setText(task.title)
                    deadlineText.text = task.deadLine
                    descriptionText.setText(task.description)
                    startedDate.text = task.startDate
                    if (!task.completed) {
                        activeText.text = "Status: Active"
                    } else {
                        activeText.text = "Status: Completed"
                        checkbox.isChecked = true
                    }

                }
            } else {
                Toast.makeText(context, "Task not found", Toast.LENGTH_SHORT).show()
            }
        })

        binding.apply {
            checkbox.setOnCheckedChangeListener{
                buttonView , isCheched ->
                if(!isCheched){
                    activeText.text = "Status: Active"
                    state = 1
                }else{
                    activeText.text = "Status: Completed"
                    state = 2
                }
            }

            deadlineLayout.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog =
                    DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                        deadLine =
                            (year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString())
                        binding.deadlineText.text = deadLine
                        deadlineFlag = 1
                    }, year, month, day)
                datePickerDialog.show()
            }
            save.setOnClickListener{
                if(deadlineFlag == 1)task.deadLine = deadLine
                if(state == 1)task.completed = false
                if(state == 2) task.completed = true
                task.title = title.text.toString()
                task.description = descriptionText.text.toString()
                viewModel.updateTask(task)
                dismiss()
            }
            close.setOnClickListener{
                dismiss()
            }
        }
    }


}