package com.internshala.foodies.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodies.R
import com.internshala.foodies.adapter.HomeViewAdapter
import com.internshala.foodies.model.Restaurants
import com.internshala.foodies.util.Connectionmanager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {
    private lateinit var registrationActivityToolBar:androidx.appcompat.widget.Toolbar
    private lateinit var edtEnterName: EditText
    private lateinit var edtEnterEmail: EditText
    private lateinit var edtMobileNumberOfUser: EditText
    private lateinit var txtDeliveryAddressOfUser: TextView
    private lateinit var edtPasswordOfUser: EditText
    private lateinit var edtConfirmPasswordOfUser: EditText
    private lateinit var btnRegister: Button
    private lateinit var showPassword:ImageView
    private lateinit var progressLayout:RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var edtRegistrationAddressLine1:EditText
    private lateinit var edtRegistrationAddressLine2:EditText
    private lateinit var edtRegistrationAddressLine3:EditText
    private lateinit var edtRegistrationAddressLine4:EditText
    private lateinit var btnRegistrationAlAddress:Button
    private lateinit var registrationAddressInput:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        edtEnterName = findViewById(R.id.edtEnterName)
        edtEnterEmail = findViewById(R.id.edtEnterEmail)
        edtMobileNumberOfUser = findViewById(R.id.edtMobileNumberOfUser)
        txtDeliveryAddressOfUser = findViewById(R.id.txtDeliveryAddressOfUser)
        edtPasswordOfUser = findViewById(R.id.edtPasswordOfUser)
        edtConfirmPasswordOfUser = findViewById(R.id.edtConfirmPasswordOfUser)
        btnRegister = findViewById(R.id.btnRegister)
        registrationActivityToolBar = findViewById(R.id.registrationActivityToolBar)
        showPassword = findViewById(R.id.registrationShowPassword)
        progressBar = findViewById(R.id.progressBarReg)
        progressLayout = findViewById(R.id.progressLayoutReg)

        registrationAddressInput = findViewById(R.id.registrationAddressInput)
        btnRegistrationAlAddress = findViewById(R.id.btnRegistrationAddressAllLines)
        edtRegistrationAddressLine1 = findViewById(R.id.edtRegistrationAddressLine1)
        edtRegistrationAddressLine2 = findViewById(R.id.edtRegistrationAddressLine2)
        edtRegistrationAddressLine3 = findViewById(R.id.edtRegistrationAddressLine3)
        edtRegistrationAddressLine4 = findViewById(R.id.edtRegistrationAddressLine4)

        progressLayout.visibility = View.GONE
        progressBar.visibility = View.GONE

        registrationAddressInput.visibility = View.GONE

        setSupportActionBar(registrationActivityToolBar)
        supportActionBar?.title = "Register Here"
        supportActionBar?.setLogo(R.drawable.ic_final_logo)
        supportActionBar?.show()

        var checkPasswordVisible = false
        showPassword.setOnClickListener {
            if(!checkPasswordVisible){
                edtPasswordOfUser.transformationMethod = HideReturnsTransformationMethod.getInstance()
                checkPasswordVisible = true
                showPassword.setImageResource(R.drawable.ic_hide_passwoard)
            }else{
                edtPasswordOfUser.transformationMethod = PasswordTransformationMethod.getInstance()
                checkPasswordVisible = false
                showPassword.setImageResource(R.drawable.ic_show_passwoard)
            }

        }
        txtDeliveryAddressOfUser.setOnClickListener {
            registrationAddressInput.visibility = View.VISIBLE
            progressLayout.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

        }
        btnRegistrationAlAddress.setOnClickListener {
            if ((edtRegistrationAddressLine1.text.toString().length > 3) && (edtRegistrationAddressLine4.text.toString().length == 6)){
                val address:String = "${edtRegistrationAddressLine1.text.toString()}\n${edtRegistrationAddressLine2.text.toString()}\n${edtRegistrationAddressLine3.text.toString()}\n${edtRegistrationAddressLine4.text.toString()}"
                txtDeliveryAddressOfUser.text = address
                progressLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                registrationAddressInput.visibility = View.GONE
            }
            else
            {
                Toast.makeText(this@RegistrationActivity,"Please fill up row 1 and row4 correctly",Toast.LENGTH_LONG).show()
            }
        }
        btnRegister.setOnClickListener {

            val name: String? = edtEnterName.text.toString()
            val email: String? = edtEnterEmail.text.toString()
            val mobilleNumber: String? = edtMobileNumberOfUser.text.toString()
            val deliveryAddress: String? = txtDeliveryAddressOfUser.text.toString()
            val password: String? = edtPasswordOfUser.text.toString()
            val confirmPassword: String? = edtConfirmPasswordOfUser.text.toString()

            if ((name?.length!! > 0) && (email?.length!! > 5) && (deliveryAddress?.length!! > 0)) {
                if (name.length > 2 && mobilleNumber?.length == 10 && password?.length!! > 3) {
                    if (password == confirmPassword) {

                        if (Connectionmanager().checkConnectivity(this@RegistrationActivity as Context)) {
                            progressLayout.visibility = View.VISIBLE
                            progressBar.visibility = View.VISIBLE
                            try {


                                val queue = Volley.newRequestQueue(this@RegistrationActivity as Context)
                                val url = "http://13.235.250.119/v2/register/fetch_result"
                                val sharedPreferences = getSharedPreferences("logInDetails", Context.MODE_PRIVATE)
                                val jsonPost = JSONObject() /*jsonObject for post Request*/
                                jsonPost.put("name",name)
                                jsonPost.put("mobile_number",mobilleNumber)
                                jsonPost.put("password",password)
                                jsonPost.put("address",deliveryAddress)
                                jsonPost.put("email",email)

                                val jsonObjectRequest =
                                    object :
                                        JsonObjectRequest(Request.Method.POST , url , jsonPost , Response.Listener {
                                            val data = it.getJSONObject("data")
                                            val success = data.getBoolean("success")
                                            if(success){
                                                val registrationDetails = data.getJSONObject("data")
                                                sharedPreferences.edit().putString("user_id",registrationDetails.getString("user_id")).apply()
                                                sharedPreferences.edit().putString("name",registrationDetails.getString("name")).apply()
                                                sharedPreferences.edit().putString("email",registrationDetails.getString("email")).apply()
                                                sharedPreferences.edit().putString("mobile_number",registrationDetails.getString("mobile_number")).apply()
                                                sharedPreferences.edit().putString("address",registrationDetails.getString("address")).apply()
                                                sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                                                Toast.makeText(this@RegistrationActivity,"You registered Successfully",Toast.LENGTH_LONG).show()
                                                startActivity(Intent(this@RegistrationActivity,SplashScreen::class.java))
                                            }
                                            else{
                                                progressLayout.visibility = View.GONE
                                                progressBar.visibility = View.GONE

                                                AlertDialog.Builder(this@RegistrationActivity)
                                                    .setTitle("Error")
                                                    .setMessage(data.getString("errorMessage"))
                                                    .setPositiveButton("TryAgain"){text, listener ->}
                                                    .setNegativeButton("Exit"){text, listener ->
                                                        finishAffinity()
                                                    }
                                                    .create()
                                                    .show()
                                            }
                                        },Response.ErrorListener {
                                            Toast.makeText(this@RegistrationActivity,"Some unexpected error Occurs",Toast.LENGTH_LONG).show()
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
                                    Toast.makeText(this@RegistrationActivity,"Some unexpected Error Occurs during Post the request to Server",Toast.LENGTH_LONG).show()
                            }

                        } else {
                            progressLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE

                            /*if there is no internet Connection*/
                            AlertDialog.Builder(this@RegistrationActivity as Context)
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
                            this@RegistrationActivity,
                            "!!Password and ConfirmPassword doesn't match!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    progressLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Name must contains atleast 3 character & Mobile Number must contains 10 character & passwordmustbe atleast 4 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                progressLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@RegistrationActivity,
                    "!All Fields must be filled up & email mustcontain atleast 6 characters!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.back)
        {
            startActivity(Intent(this@RegistrationActivity,LoginActivity::class.java))
            finish()

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@RegistrationActivity,LoginActivity::class.java))
        finish()
    }

}
