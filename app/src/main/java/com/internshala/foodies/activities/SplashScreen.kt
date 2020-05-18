package com.internshala.foodies.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.internshala.foodies.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val logInDetails = getSharedPreferences("logInDetails", Context.MODE_PRIVATE)
        Handler().postDelayed({
            if (logInDetails.getBoolean("isLoggedIn", false)) {
                val start = Intent(this@SplashScreen, HomePage::class.java)
                startActivity(start)
            } else {
                val start = Intent(this@SplashScreen, LoginActivity::class.java)
                startActivity(start)
            }
        }, 2000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
