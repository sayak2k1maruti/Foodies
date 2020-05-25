package com.internshala.foodies.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodies.R
import com.internshala.foodies.adapter.CartAdapter
import com.internshala.foodies.database.FoodItemDatabase
import com.internshala.foodies.database.FoodItemEntity
import com.internshala.foodies.database.RestaurantsDatabase
import com.internshala.foodies.model.FoodMenu
import com.internshala.foodies.util.Connectionmanager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Cart : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var txtCartRestaurantName: TextView
    private lateinit var recyclerViewOfCart: androidx.recyclerview.widget.RecyclerView
    private lateinit var btnPlaceOrder: Button
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbar = findViewById(R.id.toolbarCart)
        txtCartRestaurantName = findViewById(R.id.txtCartRestaurantName)
        recyclerViewOfCart = findViewById(R.id.recyclerViewOfCart)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        progressLayout = findViewById(R.id.progressLayoutCart)
        progressBar = findViewById(R.id.progressBarCart)

        progressBar.visibility = View.GONE
        progressLayout.visibility = View.GONE

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent != null) {
            supportActionBar?.title = intent.getStringExtra("name")
            txtCartRestaurantName.text = intent.getStringExtra("name")
        }

        val list = getCart(this@Cart).execute().get() /*list of all cart item*/

        recyclerViewOfCart.layoutManager = LinearLayoutManager(this@Cart)
        recyclerViewOfCart.adapter = CartAdapter(this@Cart, list)

        var totalPrice = 0
        for (element in list) {
            /*piece of code to calculate total price of order*/
            totalPrice += element.cost.toInt()
        }


        btnPlaceOrder.setText("Place Order(Rs. ${totalPrice})")

        btnPlaceOrder.setOnClickListener {


            progressBar.visibility = View.VISIBLE
            progressLayout.visibility = View.VISIBLE


            val queue = Volley.newRequestQueue(this@Cart)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            val jsonObject = JSONObject()
            val jsonArray = JSONArray()

            for (element in list) {
                val jsonItemId = JSONObject()
                jsonItemId.put("food_item_id", element.id)
                jsonArray.put(jsonItemId)
            }

            val sharedPreferences = getSharedPreferences(
                "logInDetails",
                Context.MODE_PRIVATE
            )
            jsonObject.put("user_id", sharedPreferences.getString("user_id", "0"))
            jsonObject.put("restaurant_id", list[0].restaurantId)
            jsonObject.put("total_cost", totalPrice.toString())
            jsonObject.put("food", jsonArray)

            if (Connectionmanager().checkConnectivity(this@Cart as Context)) {
                try {

                    val jsonObjectRequest =
                        object :
                            JsonObjectRequest(Request.Method.POST,
                                url,
                                jsonObject,
                                Response.Listener {

                                    if (it.getJSONObject("data").getBoolean("success")) {
                                        for (element in list) {

                                            deleteDb(this@Cart, element).execute().get()
                                            /*cart element is deleted from database*/
                                        }
                                        startActivity(Intent(this@Cart, OrderPlaced::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@Cart,
                                            "Some Unexpected Error Occurs",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                },
                                Response.ErrorListener {
                                    Toast.makeText(
                                        this@Cart,
                                        "Some Unexpected Error Occurs",
                                        Toast.LENGTH_LONG
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
                    queue.add(jsonObjectRequest)

                } catch (e: JSONException) {
                    Toast.makeText(this@Cart, "Some Unexpected Error Occurs", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                AlertDialog.Builder(this@Cart as Context)
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
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class getCart(val context: Context) : AsyncTask<Void, Void, List<FoodItemEntity>>() {
        /*backgrounf task to get the orderes added to cart*/
        override fun doInBackground(vararg params: Void?): List<FoodItemEntity> {
            val db = Room.databaseBuilder(context, FoodItemDatabase::class.java, "cart").build()
            val list = db.foodItemDAO().gerAllFoodItems()
            return list
        }

    }

    class deleteDb(val context: Context, val foodItemEntity: FoodItemEntity) :
        AsyncTask<Void, Void, Boolean>() {
        /*background task to delete cart from database after order is placed*/

        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, FoodItemDatabase::class.java, "cart").build()
            db.foodItemDAO().deleteFoodItem(foodItemEntity)
            return true
        }

    }
}
