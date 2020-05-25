package com.internshala.foodies.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
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

class ForgotpasswordActivity : AppCompatActivity() {

    private lateinit var edtMobileNumber: EditText
    private lateinit var edtEmailId: EditText
    private lateinit var btnnext: Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var progressLayout:RelativeLayout
    private lateinit var progressBar:ProgressBar

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.back) {
            startActivity(Intent(this@ForgotpasswordActivity, LoginActivity::class.java))
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
        progressLayout = findViewById(R.id.progressLayoutForgotPassword)
        progressBar = findViewById(R.id.progressBarForgotPassword)

        progressLayout.visibility = View.GONE
        progressBar.visibility = View.GONE

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Forgot Password"
        supportActionBar?.setLogo(R.drawable.ic_final_logo)
        val queue = Volley.newRequestQueue(this@ForgotpasswordActivity)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

        btnnext.setOnClickListener {
            val mobileNumber = edtMobileNumber.text.toString()
            val email = edtEmailId.text.toString()
            if (mobileNumber.length == 10 ) {
                if (email.length >= 4) {
                    val jsonPost = JSONObject()
                    jsonPost.put("mobile_number", mobileNumber)
                    jsonPost.put("email", email)
                    if (Connectionmanager().checkConnectivity(this@ForgotpasswordActivity as Context)){
                        progressLayout.visibility = View.VISIBLE
                        progressBar.visibility = View.VISIBLE
                        try {
                            val jsonObjectRequest =
                                object :
                                    JsonObjectRequest(
                                        Request.Method.POST,
                                        url,
                                        jsonPost,
                                        Response.Listener {
                                            val data = it.getJSONObject("data")
                                            val success = data.getBoolean("success")
                                            if (success) {
                                                if (data.getBoolean("first_try")) {
                                                    val intent = Intent(
                                                        this@ForgotpasswordActivity,
                                                        OtpVerificationActivity::class.java
                                                    )

                                                    intent.putExtra("first_try", true)
                                                    intent.putExtra("email",email)
                                                    intent.putExtra("mobile_number",mobileNumber)

                                                    startActivity(intent)
                                                    finish()
                                                } else {
                                                    val intent = Intent(
                                                        this@ForgotpasswordActivity,
                                                        OtpVerificationActivity::class.java
                                                    )

                                                    intent.putExtra("first_try", false)
                                                    intent.putExtra("email",email)
                                                    intent.putExtra("mobile_number",mobileNumber)

                                                    startActivity(intent)
                                                    finish()
                                                }
                                            } else {
                                                AlertDialog.Builder(this@ForgotpasswordActivity)
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
                                                this@ForgotpasswordActivity,
                                                "Some unexpected Error Occurs during Post the request to server",
                                                Toast.LENGTH_LONG
                                            ).show()
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
                            Toast.makeText(
                                this@ForgotpasswordActivity,
                                "Some unexpected Error Occurs during Post the request to server",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else{
                        /*if there is no internet Connection*/
                        AlertDialog.Builder(this@ForgotpasswordActivity as Context)
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
                        this@ForgotpasswordActivity,
                        "Enter a valid Email Id",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(
                    this@ForgotpasswordActivity,
                    "Incorrect mobile number length",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }


    }


    override fun onBackPressed() {
        startActivity(Intent(this@ForgotpasswordActivity,LoginActivity::class.java))
        finish()
    }
}
