package com.internshala.foodies.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodies.R
import com.internshala.foodies.adapter.FoodMenuAdapter
import com.internshala.foodies.database.FoodItemDatabase
import com.internshala.foodies.model.FoodMenu
import com.internshala.foodies.util.Connectionmanager
import org.json.JSONException

class RestaurantMenu : AppCompatActivity() {
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var recyclerView:androidx.recyclerview.widget.RecyclerView
    private lateinit var btnProceedToCart:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        toolbar = findViewById(R.id.toolbarRestaurentMenu)
        recyclerView = findViewById(R.id.recyclerViewOfFoodMenu)
        btnProceedToCart = findViewById(R.id.btnProceedToCart)
        btnProceedToCart.visibility = View.GONE
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val foodMenuList:ArrayList<FoodMenu> = arrayListOf()
        if(intent != null){
            supportActionBar?.title = intent.getStringExtra("name")
            val nameOfRestaurant = intent.getStringExtra("name")
            val queue = Volley.newRequestQueue(this@RestaurantMenu)
            val id = intent.getStringExtra("id")
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/${id}"
            if (Connectionmanager().checkConnectivity(this@RestaurantMenu as Context)){
                try {
                    val jsonObjectRequest =
                        object :
                            JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {
                                val data = it.getJSONObject("data")
                                if(data.getBoolean("success")){
                                    val list = data.getJSONArray("data")

                                    for(i in 0 until  list.length()){
                                        val foodMenu = FoodMenu(
                                           list.getJSONObject(i).getString("id"),
                                            list.getJSONObject(i).getString("name"),
                                            list.getJSONObject(i).getString("cost_for_one"),
                                            list.getJSONObject(i).getString("restaurant_id")
                                        )

                                        foodMenuList.add(foodMenu)
                                    }
                                    recyclerView.layoutManager = LinearLayoutManager(this@RestaurantMenu)
                                    recyclerView.adapter = FoodMenuAdapter(foodMenuList,this@RestaurantMenu,btnProceedToCart)
                                }else{
                                    Toast.makeText(this@RestaurantMenu,"Some unexpected Error occurs",Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                            Response.ErrorListener {

                            }){
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "f483c3c822da32"
                                return headers
                            }

                        }
                    queue.add(jsonObjectRequest)
                }catch (e : JSONException){
                    Toast.makeText(this@RestaurantMenu,"Some unexpected Error occurs",Toast.LENGTH_SHORT)
                        .show()
                }




            }else{
                AlertDialog.Builder(this@RestaurantMenu as Context)
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
            btnProceedToCart.setOnClickListener {
                val intent = Intent(this@RestaurantMenu,Cart::class.java)
                intent.putExtra("name",nameOfRestaurant)
                startActivity(intent)
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        /*All cart is deleted from database*/
        DeleteAllCart(this@RestaurantMenu).execute().get()
        super.onBackPressed()
    }

    class DeleteAllCart(val context: Context):AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, FoodItemDatabase::class.java,"cart").build()
            db.foodItemDAO().deleteAll()
            return true
        }

    }
}
