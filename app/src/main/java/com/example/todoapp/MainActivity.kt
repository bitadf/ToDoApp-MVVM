package com.example.todoapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.viewModels

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.room.InvalidationTracker
import com.example.todoapp.Fragments.CreateTaskFragment
import com.example.todoapp.Fragments.HomeFragment
import com.example.todoapp.adapter.HomeAdapter
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import com.example.todoapp.Fragments.EmptyFragment
import com.example.todoapp.Fragments.GridSearchFragment
import com.example.todoapp.Fragments.ShowFragment
import com.example.todoapp.utils.Constants.SEARCH_FLAG

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout : DrawerLayout

    private val viewModel : TaskViewModel by viewModels()

    lateinit var homeAdapter : HomeAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigation_view = findViewById<NavigationView>(R.id.nav_view)
        navigation_view.setNavigationItemSelectedListener(this)
        val toogle = ActionBarDrawerToggle(this,drawerLayout , toolbar,R.string.app_name , R.string.app_name)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu) // Use your menu icon drawable

        drawerLayout.addDrawerListener(toogle)
//        if(viewModel.isDatabaseEmpty()){
//            goToFragment(EmptyFragment())
//        }


    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (fragment !is HomeFragment) {
            goToFragment(HomeFragment())
        } else {
//            super.onBackPressed()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu , menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_ic_toolbar)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {// Handle search submission
                searchView.clearFocus()
                searchItem.collapseActionView()

                query?.let {
                    viewModel.searchTask(it).observe(this@MainActivity){
                        searchRes ->
                        if(searchRes.isEmpty()){
                            Toast.makeText(this@MainActivity , "Task does not exist" , Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                           searchView.clearFocus()
                           goToFragment(GridSearchFragment() , SEARCH_FLAG , query)

                        }

                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle live search updates (optional)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_ic_toolbar -> goToFragment(CreateTaskFragment())
            R.id.home_ic_toolbar -> goToFragment(HomeFragment())
        }
        return super.onOptionsItemSelected(item)
    }
    fun goToFragment(fragment: Fragment , key : String = "" , value : String = "") {

        if (key.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putString(key, value)
            fragment.arguments = bundle
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
//            .addToBackStack(null)
            .commit()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home_nav_item -> goToFragment(HomeFragment())
            R.id.create_task_nav_item -> goToFragment(CreateTaskFragment())
            R.id.category_nav_item -> Toast.makeText(this,"Category",Toast.LENGTH_SHORT).show()
            R.id.profile_nav_item -> Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show()
            R.id.logout_nav_item -> Toast.makeText(this,"logOut",Toast.LENGTH_SHORT).show()

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}