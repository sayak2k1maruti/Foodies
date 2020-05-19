package com.internshala.foodies.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodies.R
import com.internshala.foodies.util.Connectionmanager
import org.json.JSONException
import org.json.JSONObject

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var edtOTP: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtNewPassWordConfirmation: EditText
    private lateinit var txtAboutOTP: TextView
    private lateinit var btnNext: Button
    private lateinit var imgShowPassword:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        edtOTP = findViewById(R.id.edtOTP)
        edtNewPassword = findViewById(R.id.edtNewPasswoard)
        edtNewPassWordConfirmation = findViewById(R.id.edtNewPasswoardConfirmation)
        txtAboutOTP = findViewById(R.id.txtAboutOTP)
        btnNext = findViewById(R.id.btnNextFromOTP)
        imgShowPassword = findViewById(R.id.imgShowNewPassword)

        var checkPasswordVisible = false
        imgShowPassword.setOnClickListener {
            /*code to make password visible or hidden*/
            if(!checkPasswordVisible){
                edtNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                checkPasswordVisible = true
                imgShowPassword.setImageResource(R.drawable.ic_hide_passwoard)
            }else{
                edtNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                checkPasswordVisible = false
                imgShowPassword.setImageResource(R.drawable.ic_show_passwoard)
            }
        }

        if (intent != null) {
            if (intent.getBooleanExtra("first_try", false)) {
                val email = intent.getStringExtra("email")
                txtAboutOTP.text = "OTP is sent to ${email} successfully"
            } else {
                val email = intent.getStringExtra("email")
                txtAboutOTP.text = "Use previous OTP sent to ${email} "
            }
        }

        btnNext.setOnClickListener {
            val otp = edtOTP.text.toString()
            val newPassword = edtNewPassword.text.toString()
            val confirmPassword = edtNewPassWordConfirmation.text.toString()
            if (newPassword == confirmPassword) {
                val queue = Volley.newRequestQueue(this@OtpVerificationActivity)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val jsonPost = JSONObject()
                jsonPost.put("mobile_number", intent.getStringArrayExtra("mobile_number"))
                jsonPost.put("password", newPassword)
                jsonPost.put("otp", otp)
                if (Connectionmanager().checkConnectivity(this@OtpVerificationActivity as Context)) {
                    try {
                        val jsonObjectRequest =
                            object :
                                JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    jsonPost,
                                    Response.Listener {
                                        val data = it.getJSONObject("data")
                                        if (data.getBoolean("success")) {
                                            AlertDialog.Builder(this@OtpVerificationActivity)
                                                .setTitle("Success")
                                                .setMessage(data.getString("successMessage"))
                                                .setPositiveButton("Ok") { text, listener ->

                                                    val sharedPreferences = getSharedPreferences(
                                                        "logInDetails",
                                                        Context.MODE_PRIVATE
                                                    )
                                                    sharedPreferences.edit().clear()
                                                        .apply() /*clear all shared preferences*/
                                                    startActivity(
                                                        Intent(
                                                            this@OtpVerificationActivity,
                                                            LoginActivity::class.java
                                                        )
                                                    )
                                                    finish()
                                                }
                                                .create()
                                                .show()
                                        } else {

                                            AlertDialog.Builder(this@OtpVerificationActivity)
                                                .setTitle("Error")
                                                .setMessage(data.getString("errorMessage"))
                                                .setPositiveButton("TryAgain") { text, listener -> }
                                                .setNegativeButton("Exit") { text, listener ->
                                                    finishAffinity()
                                                }
                                                .create()
                                                .show()

                                        }
                                    },
                                    Response.ErrorListener {
                                        Toast.makeText(
                                            this@OtpVerificationActivity,
                                            "Some unexpected Error ocurs Try Again",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }) {
                                override fun getHeaders(): MutableMap<String, String> {
                                    val headers = HashMap<String, String>()
                                    headers["Content-type"] = "application/json"
                                    headers["token"] = "f483c3c822da32"
                                    return headers
                                }
                            }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@OtpVerificationActivity,
                            "Some unexpected Error Occurs during Post the request to server",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    AlertDialog.Builder(this@OtpVerificationActivity as Context)
                        .setTitle("Error")
                        .setMessage("No Internet Connection")
                        .setPositiveButton("Open Setting") { text, listener ->
                            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))

                        }
                        .setNegativeButton("Exit") { text, listener ->
                            finishAffinity()
                        }
                        .create()
                        .show()
                }
            } else {
                Toast.makeText(
                    this@OtpVerificationActivity,
                    "Password doesnot match with Confirmpassword",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}
