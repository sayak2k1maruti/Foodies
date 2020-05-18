package com.internshala.foodies.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import com.internshala.foodies.R

private lateinit var edtMobileNumber: EditText
private lateinit var edtPassword: EditText
private lateinit var txtForgotPassword: TextView
private lateinit var txtRegister: TextView
private lateinit var btnLogIn: Button
private lateinit var showpassword:ImageView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtMobileNumber = findViewById(R.id.edtMobileNumber)
        edtPassword = findViewById(R.id.edtPasswoard)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)
        btnLogIn = findViewById(R.id.btnLogIn)
        showpassword = findViewById(R.id.showPassword)
        var showPassword = false


        showpassword.setOnClickListener {
            if(!showPassword){
                edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPassword = true
                showpassword.setImageResource(R.drawable.ic_hide_passwoard)
            }else{
                edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                showPassword = false
                showpassword.setImageResource(R.drawable.ic_show_passwoard)
            }

        }
        btnLogIn.setOnClickListener {
            val mobileNumber = edtMobileNumber.text.toString()
            val password = edtPassword.text.toString()
            if (mobileNumber.length == 10) {
                if (password.length > 3) {
                    backUpLogInDetails(mobileNumber, password)
                    startActivity(Intent(this@LoginActivity, HomePage::class.java))
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Password must be atleast 4 character long",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else {
                Toast.makeText(
                    this@LoginActivity,
                    "Length of phone number Must be 10",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        txtRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java))
        }
        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity,ForgotpasswordActivity::class.java))
        }

    }

    private fun backUpLogInDetails(mobile: String?, password: String?) {
        val logInDetails: SharedPreferences =
            getSharedPreferences("logInDetails", Context.MODE_PRIVATE)
        logInDetails.edit().putBoolean("isLoggedIn", true).apply()
        logInDetails.edit().putString("mobile_number", mobile).apply()
        logInDetails.edit().putString("password", password).apply()
    }

    override fun onPause() {
       super.onPause()
        finish()
    }
}
