package com.internshala.foodies.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.internshala.foodies.R
import com.internshala.foodies.adapter.OrderHistoryAdapter
import com.internshala.foodies.database.FoodItemEntity
import com.internshala.foodies.model.PreviousOrders
import com.internshala.foodies.util.Connectionmanager
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */
class orderHistory : Fragment() {
        private lateinit var recyclerViewPreviousRestaurants:androidx.recyclerview.widget.RecyclerView
        private lateinit var progressLayout: RelativeLayout
        private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        recyclerViewPreviousRestaurants = view.findViewById(R.id.recyclerViewPreviousRestaurants)
        progressLayout = view.findViewById(R.id.progressLayoutOrderHistory)
        progressBar = view.findViewById(R.id.progressBarOrderHistory)

        val sharedPreferences =
            activity?.getSharedPreferences("logInDetails", Context.MODE_PRIVATE)

        val queue = Volley.newRequestQueue(activity as Context)
        val userId = sharedPreferences?.getString("user_id","1")
        val previousOrdersList:ArrayList<PreviousOrders> = arrayListOf()
        val url = "http://13.235.250.119/v2/orders/fetch_result/${userId}"
        if (Connectionmanager().checkConnectivity(activity as Context)) {
                try {
                    val jsonObjectRequest =
                        object :
                            JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {
                                val data = it.getJSONObject("data")
                                if(data.getBoolean("success")){
                                    val list = data.getJSONArray("data")
                                    for (i in 0 until list.length()){
                                        val foodItemList:ArrayList<FoodItemEntity> = arrayListOf()
                                        val foodItems = list.getJSONObject(i).getJSONArray("food_items")
                                        for (j in 0 until foodItems.length()){
                                            val foodItem = FoodItemEntity(
                                                foodItems.getJSONObject(j).getString("food_item_id"),
                                                foodItems.getJSONObject(j).getString("name"),
                                                foodItems.getJSONObject(j).getString("cost"),
                                                "not_needed"
                                            )
                                            foodItemList.add(foodItem)
                                        }
                                        val previousOrder = PreviousOrders(
                                            list.getJSONObject(i).getString("order_id"),
                                            list.getJSONObject(i).getString("restaurant_name"),
                                            list.getJSONObject(i).getString("total_cost"),
                                            list.getJSONObject(i).getString("order_placed_at"),
                                            foodItemList
                                        )

                                        previousOrdersList.add(previousOrder)
                                    }

                                    progressBar.visibility = View.GONE
                                    progressLayout.visibility = View.GONE

                                    recyclerViewPreviousRestaurants.layoutManager = LinearLayoutManager(activity as Context)
                                    recyclerViewPreviousRestaurants.adapter = OrderHistoryAdapter(activity as Context,previousOrdersList)
                                }else{
                                    Toast.makeText(activity,"Some unexpected Error Occurs",Toast.LENGTH_LONG)
                                        .show()
                                }
                            },
                            Response.ErrorListener {
                                Toast.makeText(activity,"Some unexpected Volley error Occurs",Toast.LENGTH_LONG)
                                    .show()
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
                    /*If some  Error Occurs*/
                    AlertDialog.Builder(activity as Context)
                        .setTitle("Error")
                        .setMessage("Some Unexpected Error Occurs")
                        .setPositiveButton("Exit") { text, listener ->
                            activity?.finishAffinity()

                        }
                        .create()
                        .show()
                }
        }else{
            /*if there is no internet Connection*/
            AlertDialog.Builder(activity as Context)
                .setTitle("Error")
                .setMessage("No Internet Connection")
                .setPositiveButton("Open Setting") { text, listener ->
                    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))

                }
                .setNegativeButton("Exit") { text, listener ->
                    activity?.finishAffinity()
                }
                .create()
                .show()
        }

            return view
    }

}
