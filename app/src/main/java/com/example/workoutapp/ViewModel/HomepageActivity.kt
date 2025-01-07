package com.example.workoutapp.ViewModel

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.workoutapp.HomeScreenActivity
import com.example.workoutapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomepageActivity : AppCompatActivity() {
    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize the BottomNavigationView and Toolbar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation)
        val myToolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)
        toolbar = supportActionBar!!

        // Initial fragment for HomePage
        toolbar.title = "Home Page"
        val homeFragment = HomeScreenActivity.newInstance()
        openFragment(homeFragment)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    // Bottom Navigation item selection listener
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                toolbar.title = "Home Page"
                val homeFragment = HomeScreenActivity.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_workout -> {
                toolbar.title = "Workouts"
                val workoutFragment = WorkoutActivity.newInstance()
                openFragment(workoutFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_history -> {
                toolbar.title = "History"
                val historyFragment = HistoryActivity.newInstance()
                openFragment(historyFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                toolbar.title = "Profile"
                val profileFragment = ProfileActivity.newInstance()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    // Method for fragment transactions
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
