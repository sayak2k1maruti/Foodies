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
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodies.R
import com.internshala.foodies.activities.RestaurentMenu
import com.internshala.foodies.database.RestaurantsDatabase
import com.internshala.foodies.database.RestaurantsEntity
import com.internshala.foodies.model.Restaurants
import com.squareup.picasso.Picasso

class HomeViewAdapter(
    private val resturantList: ArrayList<Restaurants>,
    private val context: Context
) : RecyclerView.Adapter<HomeViewAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgImageOfresturant: ImageView = view.findViewById(R.id.imgHomeFoodImage)
        var txtHomeResturantName: TextView = view.findViewById(R.id.txtHomeResturantName)
        var txtPriceForOne: TextView = view.findViewById(R.id.txtHeaderResturantPriceForOne)
        var imgHomefavouriteIcon: ImageView = view.findViewById(R.id.imgHomefavouriteImage)
        var txtHomeResturantRating: TextView = view.findViewById(R.id.txtHomeResturantRating)
        var parentLayout:RelativeLayout = view.findViewById(R.id.ParentRestaurentDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_of_resturent_list, parent, false)

        return HomeViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return resturantList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.txtHomeResturantName.text = resturantList[position].name
        holder.txtPriceForOne.text = resturantList[position].costforOne
        holder.txtHomeResturantRating.text = resturantList[position].rating
        Picasso.get().load(resturantList[position].imageUrl).error(R.drawable.ic_default_food_icon)
            .into(holder.imgImageOfresturant)

        val restaurant = RestaurantsEntity(
            resturantList[position].id.toInt(),
            resturantList[position].name,
            resturantList[position].rating,
            resturantList[position].costforOne,
            resturantList[position].imageUrl
        )
        if((HomeAsyncTask(context.applicationContext,restaurant,1).execute().get()))
        {
            holder.imgHomefavouriteIcon.setImageResource(R.drawable.ic_favourite_fillup)
        }else{
            holder.imgHomefavouriteIcon.setImageResource(R.drawable.ic_default_hollo_favourite)
        }

        holder.imgHomefavouriteIcon.setOnClickListener {

            if (! (HomeAsyncTask(context.applicationContext,restaurant,1).execute().get())){
                HomeAsyncTask(context.applicationContext,restaurant,2).execute().get()
                Toast.makeText(context,"Successfully added to favourite",Toast.LENGTH_SHORT).show()
                holder.imgHomefavouriteIcon.setImageResource(R.drawable.ic_favourite_fillup)
            }else{
                HomeAsyncTask(context.applicationContext,restaurant,3).execute().get()
                Toast.makeText(context.applicationContext,"Successfully removed to favourite",Toast.LENGTH_SHORT).show()
                holder.imgHomefavouriteIcon.setImageResource(R.drawable.ic_default_hollo_favourite)
            }
        }
        holder.parentLayout.setOnClickListener {
            openMenu(resturantList[position].id)
        }
        holder.imgImageOfresturant.setOnClickListener {
            openMenu(resturantList[position].id)
        }
        holder.txtHomeResturantRating.setOnClickListener {
            openMenu(resturantList[position].id)
        }
    }

    class HomeAsyncTask(
        val context: Context,
        val restaurantsEntity: RestaurantsEntity,
        val mode: Int
    ) : AsyncTask<Void, Void, Boolean>() {
        val db =
            Room.databaseBuilder(context.applicationContext, RestaurantsDatabase::class.java, "restaurants-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

           when(mode){
               1->{
                   /* check if it is already in favourite*/
                   val restaurant:RestaurantsEntity? = db.restaurantDAO().getRestaurantById(restaurantsEntity.restaurant_id.toString())
                   db.close()
                   return (restaurant != null)
               }
                2->{
                    /*insert to database*/
                    db.restaurantDAO().insertRestaurant(restaurantsEntity)
                    db.close()
                    return true
                }
               3->{
                   /*Delete from database*/
                   db.restaurantDAO().deleteRestaurant(restaurantsEntity)
                   db.close()
                   return true
               }

           }
            return false
        }

    }

    fun openMenu(id:String){
        /*function to open restaurentMenu*/
        val intent = Intent(context,RestaurentMenu::class.java)
        intent.putExtra("id",id)
        context.startActivity(intent)
    }
}
