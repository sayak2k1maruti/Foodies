package com.internshala.foodies.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
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

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {


    lateinit var recycleViewOfResturents: androidx.recyclerview.widget.RecyclerView
    lateinit var homeProgressLayout: RelativeLayout
    lateinit var homeProgressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val homeFragment = inflater.inflate(R.layout.fragment_home, container, false)



        val restaurantLists: ArrayList<Restaurants> = arrayListOf()
        recycleViewOfResturents = homeFragment.findViewById(R.id.recyclerViewOfResrurentList)
        homeProgressLayout = homeFragment.findViewById(R.id.homeprogressLayout)
        homeProgressBar = homeFragment.findViewById(R.id.homeprogressBar)


        homeProgressLayout.visibility = View.VISIBLE
        homeProgressBar.visibility = View.VISIBLE


        if (Connectionmanager().checkConnectivity(activity as Context)) {

            try {


                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

                val jsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                        val success = it.getJSONObject("data").getBoolean("success")
                        if (success) {

                            val restaurants: JSONArray = it.getJSONObject("data").getJSONArray("data")
                            for (i in 0 until restaurants.length()) {
                                val restaurantDetails = restaurants.getJSONObject(i)
                                val data = Restaurants(
                                    restaurantDetails.getString("id"),
                                    restaurantDetails.getString("name"),
                                    restaurantDetails.getString("rating"),
                                    restaurantDetails.getString("cost_for_one"),
                                    restaurantDetails.getString("image_url")
                                )
                                restaurantLists.add(data)
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some unexpected Error Occurs",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                        /*Adapter and LayoutManager of RecyclerView is declared*/
                        recycleViewOfResturents.adapter =
                            HomeViewAdapter(restaurantLists, activity as Context)
                        recycleViewOfResturents.layoutManager = LinearLayoutManager(activity as Context)
                        homeProgressBar.visibility = View.GONE
                        homeProgressLayout.visibility = View.GONE
                    },
                        Response.ErrorListener {

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
                /*If some Json Error Occurs*/
                AlertDialog.Builder(activity as Context)
                    .setTitle("Error")
                    .setMessage("Some Unexpected Error Occurs")
                    .setPositiveButton("Exit") { text, listener ->
                        activity?.finishAffinity()

                    }
                    .create()
                    .show()

            }

        } else {

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
        return homeFragment
    }

}