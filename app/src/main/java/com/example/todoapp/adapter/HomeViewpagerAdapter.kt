package com.example.todoapp.adapter
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoapp.Fragments.GridActiveFragment
import com.example.todoapp.Fragments.GridCompletedFragment
import com.example.todoapp.Fragments.GridFragment

class HomeViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment)
{
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> GridActiveFragment()
            1 -> GridCompletedFragment()
            2 -> GridFragment()
            else -> GridFragment()
        }
    }

}