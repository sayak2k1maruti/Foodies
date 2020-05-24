package com.internshala.foodies.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodies.R
import com.internshala.foodies.activities.RestaurentMenu
import com.internshala.foodies.database.RestaurantsDatabase
import com.internshala.foodies.database.RestaurantsEntity
import com.internshala.foodies.model.Restaurants
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class HomeViewAdapter(
    private val restaurantList: ArrayList<Restaurants>,
    private val context: Context
) : RecyclerView.Adapter<HomeViewAdapter.HomeViewHolder>(),Filterable{
    val ratingComparator = Comparator<Restaurants> { restaurant1, restaurant2 ->
        if (restaurant1.rating.compareTo(restaurant2.rating, true) == 0) {
            restaurant1.name.compareTo(restaurant2.name, true)
        } else {
            restaurant1.rating.compareTo(restaurant2.rating, true)
        }
    }

    val costComparator = Comparator<Restaurants> { restaurant1, restaurant2 ->
        if (restaurant1.costforOne.compareTo(restaurant2.costforOne, true) == 0) {
            restaurant1.name.compareTo(restaurant2.name, true)
        } else {
            restaurant1.costforOne.compareTo(restaurant2.costforOne, true)
        }
    }
    var filerRestaurants = ArrayList<Restaurants>()
    init {
        filerRestaurants = restaurantList
    }
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
        return filerRestaurants.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.txtHomeResturantName.text = filerRestaurants[position].name
        holder.txtPriceForOne.text = filerRestaurants[position].costforOne
        holder.txtHomeResturantRating.text = filerRestaurants[position].rating
        Picasso.get().load(filerRestaurants[position].imageUrl).error(R.drawable.ic_default_food_icon)
            .into(holder.imgImageOfresturant)

        val restaurant = RestaurantsEntity(
            filerRestaurants[position].id.toInt(),
            filerRestaurants[position].name,
            filerRestaurants[position].rating,
            filerRestaurants[position].costforOne,
            filerRestaurants[position].imageUrl
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
            openMenu(filerRestaurants[position].id,filerRestaurants[position].name)
        }
        holder.imgImageOfresturant.setOnClickListener {
            openMenu(filerRestaurants[position].id,filerRestaurants[position].name)
        }
        holder.txtHomeResturantRating.setOnClickListener {
            openMenu(filerRestaurants[position].id,filerRestaurants[position].name)
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

    fun openMenu(id:String,name:String){
        /*function to open restaurentMenu*/
        val intent = Intent(context,RestaurentMenu::class.java)
        intent.putExtra("id",id)
        intent.putExtra("name",name)
        context.startActivity(intent)
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val search = constraint.toString()
                if(search.isEmpty()){
                    filerRestaurants = restaurantList
                }else{
                    val resultList = ArrayList<Restaurants>()
                    for(row in restaurantList) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))){
                            resultList.add(row)
                        }
                    }
                    filerRestaurants = resultList

                }
                val filterResult = FilterResults()
                filterResult.values = filerRestaurants
                return filterResult
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filerRestaurants = results?.values as ArrayList<Restaurants>
                notifyDataSetChanged()
            }

        }
    }
    fun sort()
    {
        val buttoms = arrayOf("Cost(Low to High)", "Cost(High to Low)", "Rating")
        var selected = -1
        AlertDialog.Builder(context)
            .setTitle("SortBy?")
            .setSingleChoiceItems(
                buttoms,
                -1,
                DialogInterface.OnClickListener { dialog, which ->
                    selected = which
                })
            .setPositiveButton("Ok") { text, listener ->
                when (selected) {
                    2 -> {
                        Collections.sort(filerRestaurants, ratingComparator)
                        filerRestaurants.reverse()
                        notifyDataSetChanged()
                    }
                    0 -> {
                        Collections.sort(filerRestaurants, costComparator)
                        notifyDataSetChanged()
                    }
                    1 -> {
                        Collections.sort(filerRestaurants, costComparator)
                        filerRestaurants.reverse()
                        notifyDataSetChanged()
                    }
                    -1 -> {
                        Toast.makeText(context, "No option selected", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            .create()
            .show()
    }

}
