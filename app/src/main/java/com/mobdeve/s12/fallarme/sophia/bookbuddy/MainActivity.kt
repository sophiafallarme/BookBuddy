package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.AllFragment
import com.mobdeve.s12.fallarme.sophia.bookbuddy.collection.ViewPagerAdapter
import com.mobdeve.s12.fallarme.sophia.bookbuddy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var accountId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        // Set up the NavController for handling navigation
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Get the account ID from the intent
        accountId = intent.getLongExtra("account_id", -1)
        if (accountId == -1L) {
            Toast.makeText(this, "Account ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d("MainActivity", "Received account_id: $accountId")

        // Pass the accountId to the navigation graph
        val bundle = Bundle()
        bundle.putLong("account_id", accountId)
        navController.setGraph(navController.graph, bundle)

        // Handle navigation item selection
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home, bundle)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard, bundle)
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.navigation_notifications, bundle)
                    true
                }
                else -> false
            }
        }

        // Save the account ID to SharedPreferences
//        saveAccountIdToPreferences(accountId)

        // Set up the fragment transaction
        if (savedInstanceState == null) {
            val allFragment = AllFragment()

//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, allFragment)
//                .commit()
        }
    }

//    private fun saveAccountIdToPreferences(accountId: Long) {
//        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putLong("accountId", accountId)
//        editor.apply()
//        Log.d("MainActivity", "Account ID saved to SharedPreferences: $accountId")
//    }
}


/*
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var accountId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Get the account ID from the intent
        accountId = intent.getLongExtra("account_id", -1)
        if (accountId == -1L) {
            Toast.makeText(this, "Account ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inside MainActivity, when you navigate to BookSearchActivity
        val action = MainActivityDirections.actionNavigationHomeToBookSearchActivity(accountId)
        findNavController().navigate(action)


        Log.d("MainActivity", "Received account_id: $accountId")

        // Example button to navigate to BookActivity (you can replace this with your actual UI trigger)
        val bundle = Bundle()
        bundle.putLong("account_id", accountId)
        navController.setGraph(navController.graph, bundle)
    }
}


 */
/*
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var accountId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


       /* accountId = intent.getLongExtra("account_id", -1)
        if (accountId == -1L) {
            Toast.makeText(this, "Account ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d("MainActivity", "Received account_id: $accountId")

        */

    }



    }


 */


