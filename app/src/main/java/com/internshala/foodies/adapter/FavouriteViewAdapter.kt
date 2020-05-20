package com.internshala.foodies.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodies.R
import com.internshala.foodies.activities.RestaurentMenu
import com.internshala.foodies.database.RestaurantsDatabase
import com.internshala.foodies.database.RestaurantsEntity
import com.internshala.foodies.fragments.FavouriteRestaurents
import com.internshala.foodies.model.Restaurants
import com.squareup.picasso.Picasso

class FavouriteViewAdapter(
    val restaurantList: List<RestaurantsEntity>,
    val context: Context,
    val favouritefragment: FragmentManager
) :
    RecyclerView.Adapter<FavouriteViewAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgImageOfresturant: ImageView = view.findViewById(R.id.imgHomeFoodImage)
        var txtHomeResturantName: TextView = view.findViewById(R.id.txtHomeResturantName)
        var txtPriceForOne: TextView = view.findViewById(R.id.txtHeaderResturantPriceForOne)
        var imgHomefavouriteImage: ImageView = view.findViewById(R.id.imgHomefavouriteImage)
        var txtHomeResturantRating: TextView = view.findViewById(R.id.txtHomeResturantRating)
        var parentLayout: RelativeLayout = view.findViewById(R.id.ParentRestaurentDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_of_resturent_list, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.txtHomeResturantName.text = restaurantList[position].restaurantName
        holder.txtPriceForOne.text = restaurantList[position].restaurantCostOfOne
        holder.txtHomeResturantRating.text = restaurantList[position].restaurantRating
        Picasso.get().load(restaurantList[position].restaurantImageUrl)
            .error(R.drawable.ic_default_food_icon)
            .into(holder.imgImageOfresturant)

        holder.imgHomefavouriteImage.setImageResource(R.drawable.ic_favourite_fillup)
        val restaurant = RestaurantsEntity(
            restaurantList[position].restaurant_id,
            restaurantList[position].restaurantName,
            restaurantList[position].restaurantRating,
            restaurantList[position].restaurantCostOfOne,
            restaurantList[position].restaurantImageUrl
        )
        holder.imgHomefavouriteImage.setOnClickListener {

            DeleteFromFavourite(context, restaurant).execute().get()
            favouritefragment.beginTransaction().replace(R.id.frameHome, FavouriteRestaurents())
                .commit()
        }
        holder.parentLayout.setOnClickListener {
            openMenu(restaurantList[position].restaurant_id.toString(),restaurantList[position].restaurantName)
        }
        holder.imgImageOfresturant.setOnClickListener {
            openMenu(restaurantList[position].restaurant_id.toString(),restaurantList[position].restaurantName)
        }
        holder.txtHomeResturantRating.setOnClickListener {
            openMenu(restaurantList[position].restaurant_id.toString(),restaurantList[position].restaurantName)
        }
    }



    class DeleteFromFavourite(val context: Context, val restaurent: RestaurantsEntity) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(
            context.applicationContext,
            RestaurantsDatabase::class.java,
            "restaurants-db"
        )
            .build()

        override fun doInBackground(vararg params: Void?): Boolean {
            db.restaurantDAO().deleteRestaurant(restaurent)
            return true
        }

    }
    fun openMenu(id:String,name:String){
        /*function to open restaurentMenu*/
        val intent = Intent(context, RestaurentMenu::class.java)
        intent.putExtra("id",id)
        intent.putExtra("name",name)
        context.startActivity(intent)
    }

}
