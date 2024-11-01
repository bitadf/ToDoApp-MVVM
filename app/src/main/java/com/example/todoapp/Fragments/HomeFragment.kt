package com.example.todoapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.todoapp.adapter.HomeViewpagerAdapter
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.db.TaskEntity
import com.example.todoapp.utils.Constants.SEARCH_FLAG
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val tabTitles = arrayListOf("Active" , "Completed" , "All" )

    @Inject
    lateinit var task : TaskEntity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setUpTabLayoutWithViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    private fun setUpTabLayoutWithViewPager(){
        binding.homeViewPager2.adapter = HomeViewpagerAdapter(this)
        TabLayoutMediator(binding.homeTabLayout , binding.homeViewPager2){
            tab , position ->
            tab.text = tabTitles[position]
        }.attach()
        for(i in 0..3){
            val text_view = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title , null) as TextView
            binding.homeTabLayout.getTabAt(i)?.customView = text_view
        }
    }
}