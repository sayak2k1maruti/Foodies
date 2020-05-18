package com.internshala.foodies.fragments

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room

import com.internshala.foodies.R
import com.internshala.foodies.adapter.FavouriteViewAdapter
import com.internshala.foodies.database.RestaurantsDatabase
import com.internshala.foodies.database.RestaurantsEntity
import kotlinx.android.synthetic.main.fragment_home.*

class FavouriteRestaurents : Fragment() {
    private lateinit var recyclerViewOfFavouriteResrurentList:androidx.recyclerview.widget.RecyclerView
    private lateinit var homeprogressLayoutFavourite:RelativeLayout
    private lateinit var homeprogressBarFavourite:ProgressBar
    private lateinit var noListLayout:RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val favouriteFragment = inflater.inflate(R.layout.fragment_favourite_resturents, container, false)

        recyclerViewOfFavouriteResrurentList = favouriteFragment.findViewById(R.id.recyclerViewOfFavouriteResrurentList)
        homeprogressBarFavourite = favouriteFragment.findViewById(R.id.homeprogressBarFavourite)
        homeprogressLayoutFavourite = favouriteFragment.findViewById(R.id.homeprogressLayoutFavourite)
        noListLayout = favouriteFragment.findViewById(R.id.noListLayout)



        homeprogressLayoutFavourite.visibility = View.VISIBLE
        homeprogressBarFavourite.visibility = View.VISIBLE
        val restaurentList:List<RestaurantsEntity> = findAllFavourites(activity as Context).execute().get()


        if(activity!=null) {
            if(restaurentList.isNotEmpty()){
                homeprogressBarFavourite.visibility = View.GONE
                homeprogressLayoutFavourite.visibility = View.GONE
                noListLayout.visibility = View.GONE
                recyclerViewOfFavouriteResrurentList.layoutManager =  LinearLayoutManager(activity as Context)
                recyclerViewOfFavouriteResrurentList.adapter = FavouriteViewAdapter(restaurentList,activity as Context,activity!!.supportFragmentManager)
            }
        }
        return favouriteFragment
    }

class findAllFavourites(val context: Context):AsyncTask<Void,Void,List<RestaurantsEntity>>(){
 /*   Async Tasks*/
    override fun doInBackground(vararg params: Void?): List<RestaurantsEntity> {
        val db = Room.databaseBuilder(context,RestaurantsDatabase::class.java,"restaurants-db").build()
        return db.restaurantDAO().gerAllRestaurants()
    }
}

}
