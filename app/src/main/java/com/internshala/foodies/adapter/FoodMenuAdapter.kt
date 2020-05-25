package com.internshala.foodies.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodies.R
import com.internshala.foodies.activities.Cart
import com.internshala.foodies.database.FoodItemDatabase
import com.internshala.foodies.database.FoodItemEntity
import com.internshala.foodies.model.FoodMenu

class FoodMenuAdapter(val foodMenuList: ArrayList<FoodMenu>, val context: Context,val btnAddtocart:Button) :
    RecyclerView.Adapter<FoodMenuAdapter.FoodMenuViewHolder>() {
    var count = 0 /*a variable that will indicate if anything is adde4d to cart or not*/

    class FoodMenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val serialNumber: TextView = view.findViewById(R.id.menuSerialNum)
        val itemName: TextView = view.findViewById(R.id.txtItemName)
        val price: TextView = view.findViewById(R.id.txtCostForFooditem)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddtoCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodMenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_view_of_food_menu, parent, false)
        return FoodMenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodMenuList.size
    }

    override fun onBindViewHolder(holder: FoodMenuViewHolder, position: Int) {
        holder.serialNumber.text = (position+1).toString()
        holder.itemName.text = foodMenuList[position].name
        holder.price.text = "Rs. ${foodMenuList[position].cost}"
        holder.btnAddToCart.setText(R.string.add)
        val foodItemEntity = FoodItemEntity(
            foodMenuList[position].id,
            foodMenuList[position].name,
            foodMenuList[position].cost,
            foodMenuList[position].restaurantId
        )
        holder.btnAddToCart.setOnClickListener {
            if (holder.btnAddToCart.text == "ADD") {
                holder.btnAddToCart.setText(R.string.remove)
                CartManagement(context,foodItemEntity,1).execute().get()
                val color = ContextCompat.getColor(context, R.color.colorAccent)

                count+=1
                btnAddtocart.visibility = View.VISIBLE
                holder.btnAddToCart.setBackgroundColor(color)
            } else {
                holder.btnAddToCart.setText(R.string.add)
                count-=1
                CartManagement(context,foodItemEntity,2).execute().get()

                if (count == 0){
                    btnAddtocart.visibility = View.GONE
                }
                val color = ContextCompat.getColor(context, R.color.buttom)
                holder.btnAddToCart.setBackgroundColor(color)
            }
        }
    }

    class CartManagement(val context: Context,val foodItemEntity: FoodItemEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){
        val db = Room.databaseBuilder(context,FoodItemDatabase::class.java,"cart").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){

                1->{
                    /*insert in cart*/
                    db.foodItemDAO().insertFoodItem(foodItemEntity)
                    db.close()
                    return true
                }
                2 -> {
                    /*delete from cart*/
                    db.foodItemDAO().deleteFoodItem(foodItemEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }

}