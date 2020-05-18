package com.internshala.foodies.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.internshala.foodies.R

class ForgotpasswordActivity : AppCompatActivity() {
    private lateinit var edtMobileNumber:EditText
    private lateinit var edtEmailId:EditText
    private lateinit var btnnext:Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.back){
            startActivity(Intent(this@ForgotpasswordActivity,LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        edtMobileNumber = findViewById(R.id.edtMobileNumberOfUserForgotPassword)
        edtEmailId = findViewById(R.id.edtEmailForgotPassword)
        btnnext = findViewById(R.id.btnNextFromForgotPassword)
        toolbar = findViewById(R.id.toolBarForgotpassword)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Forgot Password"
        supportActionBar?.setLogo(R.drawable.ic_final_logo)


    }


    override fun onPause() {
        super.onPause()
        finish()
    }
}
