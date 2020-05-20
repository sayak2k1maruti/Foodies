package com.internshala.foodies.fragments

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
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
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {
    val restaurantLists: ArrayList<Restaurants> = arrayListOf()
    val ratingComparator = Comparator<Restaurants>{restaurant1,restaurant2 ->
            if(restaurant1.rating.compareTo(restaurant2.rating,true) == 0){
                restaurant1.name.compareTo(restaurant2.name,true)
            }else{
                restaurant1.rating.compareTo(restaurant2.rating,true)
            }
    }

    val costComparator = Comparator<Restaurants>{restaurant1,restaurant2 ->
        if(restaurant1.costforOne.compareTo(restaurant2.costforOne,true) == 0){
            restaurant1.name.compareTo(restaurant2.name,true)
        }else{
            restaurant1.costforOne.compareTo(restaurant2.costforOne,true)
        }
    }

    lateinit var recycleViewOfResturents: androidx.recyclerview.widget.RecyclerView
    lateinit var homeProgressLayout: RelativeLayout
    lateinit var homeProgressBar: ProgressBar
    lateinit var adapter: HomeViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val homeFragment = inflater.inflate(R.layout.fragment_home, container, false)




        recycleViewOfResturents = homeFragment.findViewById(R.id.recyclerViewOfResrurentList)
        homeProgressLayout = homeFragment.findViewById(R.id.homeprogressLayout)
        homeProgressBar = homeFragment.findViewById(R.id.homeprogressBar)


        homeProgressLayout.visibility = View.VISIBLE
        homeProgressBar.visibility = View.VISIBLE

        setHasOptionsMenu(true)

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
                        adapter = HomeViewAdapter(restaurantLists, activity as Context)
                        recycleViewOfResturents.adapter =
                            adapter
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort,menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.searchHome)
        if(searchItem!=null){
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val buttoms = arrayOf("Cost(Low to High)","Cost(High to Low)","Rating")
        if(id == R.id.sortRestaurant){
            var selected = -1
                AlertDialog.Builder(activity as Context)
                    .setItems(buttoms,DialogInterface.OnClickListener { dialog, which ->
                        selected = which
                    })
                    .setTitle("Sort")
                    .setPositiveButton("Ok"){text,listener ->
                        when (selected){
                            2->{
                                sort(restaurantLists,ratingComparator)
                                restaurantLists.reverse()
                                adapter.notifyDataSetChanged()
                            }
                            0->{
                                sort(restaurantLists,costComparator)
                                adapter.notifyDataSetChanged()
                            }
                            1->{
                                sort(restaurantLists,costComparator)
                                restaurantLists.reverse()
                                adapter.notifyDataSetChanged()
                            }
                            -1->{
                                Toast.makeText(activity,"No option selected",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .create()
                    .show()
        }
        return super.onOptionsItemSelected(item)
    }
}