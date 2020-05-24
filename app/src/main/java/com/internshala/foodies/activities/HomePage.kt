package com.internshala.foodies.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayout
import com.internshala.foodies.fragments.*
import com.internshala.foodies.R

class HomePage : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbarHome: androidx.appcompat.widget.Toolbar
    lateinit var frameHomePage: FrameLayout
    lateinit var navigationDrawerHome: com.google.android.material.navigation.NavigationView
    lateinit var txtHeaderUserPhoneNumber:TextView
    lateinit var txtHeaderUserName:TextView
    lateinit var tabLayout:com.google.android.material.tabs.TabLayout
    lateinit var txtUserProfile:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        toolbarHome = findViewById(R.id.toolbarHome)
        frameHomePage = findViewById(R.id.frameHome)
        navigationDrawerHome = findViewById(R.id.navigationDrawerHome)
        drawerLayout = findViewById(R.id.drawerLayout)
        val headerView = navigationDrawerHome.getHeaderView(0)
        txtHeaderUserPhoneNumber =headerView.findViewById(R.id.txtHeaderUserPhoneNumber)
        txtHeaderUserName = headerView.findViewById(R.id.txtHeaderUserName)
        txtUserProfile = headerView.findViewById(R.id.txtUserProfile)

        tabLayout = findViewById(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                val tab = p0?.position
                when(tab){
                    0 -> openHomeFragment()
                    1 -> openFavouriteResturentFragment()
                    2 -> openOrderHistoryFragment()
                }
            }

        })

        setSupportActionBar(toolbarHome) /*toolBar is Setb as actionBar*/
        supportActionBar?.setHomeButtonEnabled(true)  /*hamBurget button is Enabled*/
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val navigationDrawerToggle = ActionBarDrawerToggle(
            this@HomePage,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(navigationDrawerToggle)      /*click listener to HamBurger Button*/
        navigationDrawerToggle.syncState()      /*to sync drawer with hamburger icon*/
        openHomeFragment()

        txtHeaderUserPhoneNumber.text = getSharedPreferences("logInDetails", Context.MODE_PRIVATE).getString("mobile_number","123456798")
        txtUserProfile.text = getSharedPreferences("logInDetails", Context.MODE_PRIVATE).getString("name","Z")!!.take(1)
        txtHeaderUserName.text = getSharedPreferences("logInDetails", Context.MODE_PRIVATE).getString("name","Z")

        txtUserProfile.setOnClickListener {
            openmyProfileFragment()
        }

        txtHeaderUserName.setOnClickListener {
            openmyProfileFragment()
        }

        txtHeaderUserPhoneNumber.setOnClickListener {
            openmyProfileFragment()
        }
        navigationDrawerHome.setNavigationItemSelectedListener {
            it.isCheckable = true
            it.isChecked = true
            when (it.itemId) {
                /*click listerner to menu items*/
                R.id.menuHome -> {
                    openHomeFragment()
                    drawerLayout.closeDrawers()
                }
                R.id.menumyProfile -> {
                    openmyProfileFragment()
                    drawerLayout.closeDrawers()
                }
                R.id.menuFAQs -> {
                    openFAQsFragment()
                    drawerLayout.closeDrawers()
                }
                R.id.menuFavourite -> {
                    openFavouriteResturentFragment()
                    drawerLayout.closeDrawers()
                }
                R.id.menuOrderHistory -> {
                    openOrderHistoryFragment()
                    drawerLayout.closeDrawers()
                }
                R.id.contactDeveloper ->{
                    AlertDialog.Builder(this@HomePage).setTitle("Developer's address")
                        .setMessage("sayakdas2k1@gmail.com")
                        .setPositiveButton("Done"){text,listener ->

                        }
                        .create()
                        .show()
                }
                R.id.menuLogOut -> {
                    /*Dialog Box to confirm LogOut*/
                    AlertDialog.Builder(this@HomePage).setTitle("Confirmation")
                        .setMessage("Are you sure you want to LogOut?")
                        .setPositiveButton("Yes") { text, listener ->
                            val loginDetails = getSharedPreferences("logInDetails", Context.MODE_PRIVATE)
                            loginDetails.edit().putBoolean("isLoggedIn", false).apply()
                            loginDetails.edit().remove("mobileNumber").apply()
                            loginDetails.edit().remove("password").apply()

                            finishAffinity()

                        }.setNegativeButton("NO") { text, listener ->
                            Toast.makeText(this@HomePage, "LogOut Cancelled", Toast.LENGTH_SHORT)
                                .show()
                        }.create().show()
                }
            }



            return@setNavigationItemSelectedListener true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)  /*default id of hamBurget Button is android.R.id.home*/
        }
        return super.onOptionsItemSelected(item)
    }


    private fun openHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameHome, Home()).commit()
        supportActionBar?.title = "All Resturent"
        navigationDrawerHome.setCheckedItem(R.id.menuHome)
        tabLayout.visibility = View.VISIBLE
    }

    private fun openmyProfileFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameHome, myProfile()).commit()
        supportActionBar?.title = "Profile"
        tabLayout.visibility = View.GONE
    }

    private fun openFavouriteResturentFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameHome, FavouriteRestaurents()).commit()
        supportActionBar?.title = "Favourite"
        tabLayout.visibility = View.VISIBLE
    }

    private fun openOrderHistoryFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameHome, orderHistory()).commit()
        supportActionBar?.title = "History"
        tabLayout.visibility = View.VISIBLE
    }

    private fun openFAQsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameHome, FAQs()).commit()
        supportActionBar?.title = "FAQs"
        tabLayout.visibility = View.GONE
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frameHome)
        when (fragment) {
            !is Home -> openHomeFragment()
            else -> super.onBackPressed()
        }
    }

}
