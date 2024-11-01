package com.example.todoapp.Fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCreateTaskBinding
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    lateinit var binding: FragmentCreateTaskBinding

    @Inject
    lateinit var task : TaskEntity

    private val viewModel : TaskViewModel by viewModels()

    private var title = ""
    private var description = "No description set"
    private var startDate = ""
    private var deadLine = "No deadline"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //add current date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        startDate = LocalDate.now().format(formatter)
        binding.startedDate.text = startDate



        //select deadline
        binding.deadlineLayout.setOnClickListener {
            selectDeadline()
        }
        binding.calendarIc.setOnClickListener{
            selectDeadline()
        }


        binding.create.setOnClickListener {
            title = binding.titleEntry.text.toString()
            description = if(binding.descriptionEntry.text.isNotEmpty())binding.descriptionEntry.text.toString()
            else{
                "No Description"
            }
            task.title = title
            task.description = description
            task.startDate = startDate
            task.deadLine = deadLine
            if(title.isNotEmpty()) {
                viewModel.saveTask(task)
                Toast.makeText(context , "Task Created " , Toast.LENGTH_SHORT).show()
                goToFragment(HomeFragment())
            }
            else {
                Toast.makeText(context , "Title cannot be empty" , Toast.LENGTH_SHORT).show()
            }
        }
        binding.home.setOnClickListener{
            goToFragment(HomeFragment())
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
    fun selectDeadline(){
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                deadLine =
                    (year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString())
                binding.deadlineText.text = deadLine

            }, year, month, day)
        datePickerDialog.show()
    }

}