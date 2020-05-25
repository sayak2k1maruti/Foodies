package com.internshala.foodies.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodies.R
import com.internshala.foodies.util.Connectionmanager
import org.json.JSONException
import org.json.JSONObject

private lateinit var edtMobileNumber: EditText
private lateinit var edtPassword: EditText
private lateinit var txtForgotPassword: TextView
private lateinit var txtRegister: TextView
private lateinit var btnLogIn: Button
private lateinit var showpassword: ImageView
private lateinit var progressLayout: RelativeLayout
private lateinit var progressBar: ProgressBar

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
        progressLayout = findViewById(R.id.progressLayoutLogin)
        progressBar = findViewById(R.id.progressBarLogin)


        var showPassword = false


        showpassword.setOnClickListener {
            if (!showPassword) {
                edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPassword = true
                showpassword.setImageResource(R.drawable.ic_hide_passwoard)
            } else {
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
                    val queue = Volley.newRequestQueue(this@LoginActivity)
                    val url = "http://13.235.250.119/v2/login/fetch_result"
                    val jsonPut = JSONObject()
                    jsonPut.put("mobile_number", mobileNumber)
                    jsonPut.put("password", password)
                    progressLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.VISIBLE
                    if (Connectionmanager().checkConnectivity(this@LoginActivity as Context)) {
                        try {
                            val jsonObjectRequest =
                                object :
                                    JsonObjectRequest(Request.Method.POST,
                                        url,
                                        jsonPut,
                                        Response.Listener {
                                            val data = it.getJSONObject("data")
                                            if (data.getBoolean("success")) {

                                                val registrationDetails = data.getJSONObject("data")
                                                val sharedPreferences = getSharedPreferences(
                                                    "logInDetails",
                                                    Context.MODE_PRIVATE
                                                )

                                                /*value saved to shared Preference*/
                                                sharedPreferences.edit()
                                                    .putBoolean("isLoggedIn", true).apply()
                                                sharedPreferences.edit().putString(
                                                    "user_id",
                                                    registrationDetails.getString("user_id")
                                                ).apply()
                                                sharedPreferences.edit().putString(
                                                    "name",
                                                    registrationDetails.getString("name")
                                                ).apply()
                                                sharedPreferences.edit().putString(
                                                    "email",
                                                    registrationDetails.getString("email")
                                                ).apply()
                                                sharedPreferences.edit().putString(
                                                    "mobile_number",
                                                    registrationDetails.getString("mobile_number")
                                                ).apply()
                                                sharedPreferences.edit().putString(
                                                    "address",
                                                    registrationDetails.getString("address")
                                                ).apply()

                                                startActivity(
                                                    Intent(
                                                        this@LoginActivity,
                                                        HomePage::class.java
                                                    )
                                                )
                                                finish()
                                            }
                                            else {
                                                progressLayout.visibility = View.GONE
                                                progressBar.visibility = View.GONE
                                                AlertDialog.Builder(this@LoginActivity)
                                                    .setTitle("Error")
                                                    .setPositiveButton("TryAgain") { text, listener ->

                                                    }
                                                    .setNegativeButton("ForgotPassword?") { text, listener ->
                                                        startActivity(
                                                            Intent(
                                                                this@LoginActivity,
                                                                ForgotpasswordActivity::class.java
                                                            )
                                                        )
                                                        finish()
                                                    }
                                                    .setNeutralButton("Register") { text, listener ->
                                                        startActivity(
                                                            Intent(
                                                                this@LoginActivity,
                                                                RegistrationActivity::class.java
                                                            )
                                                        )
                                                        finish()
                                                    }
                                                    .setMessage("Invalid Mobile or Password")
                                                    .create()
                                                    .show()
                                            }
                                        },
                                        Response.ErrorListener {
                                            AlertDialog.Builder(this@LoginActivity)
                                                .setTitle("Error")
                                                .setPositiveButton("TryAgain") { text, listener ->

                                                }
                                                .setNegativeButton("ForgotPassword?") { text, listener ->
                                                    startActivity(
                                                        Intent(
                                                            this@LoginActivity,
                                                            ForgotpasswordActivity::class.java
                                                        )
                                                    )
                                                    finish()
                                                }
                                                .setNeutralButton("Register") { text, listener ->
                                                    startActivity(
                                                        Intent(
                                                            this@LoginActivity,
                                                            RegistrationActivity::class.java
                                                        )
                                                    )
                                                    finish()
                                                }
                                                .setMessage("Invalid Mobile or Password")
                                                .create()
                                                .show()
                                        }) {
                                    override fun getHeaders(): MutableMap<String, String> {
                                        val headers = HashMap<String, String>()
                                        headers["Content-type"] = "application/json"
                                        headers["token"] = "f483c3c822da32"
                                        return headers
                                    }
                                }

                            queue.add(jsonObjectRequest)

                        } catch (e: JSONException) {
                            progressLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity,
                                "Some unexpected Error occurs During Post the Request",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        progressLayout.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        AlertDialog.Builder(this@LoginActivity as Context)
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
                    progressLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE

                    Toast.makeText(
                        this@LoginActivity,
                        "Password must be atleast 4 character long",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                progressLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@LoginActivity,
                    "Length of phone number Must be 10",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        txtRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotpasswordActivity::class.java))
        }

    }


    override fun onBackPressed() {
        finishAffinity()
    }
}
