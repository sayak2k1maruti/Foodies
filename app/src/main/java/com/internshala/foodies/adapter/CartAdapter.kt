package com.internshala.foodies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodies.R
import com.internshala.foodies.database.FoodItemEntity


class CartAdapter(val context: Context, private val cartList: List<FoodItemEntity>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(view:View): RecyclerView.ViewHolder(view){
        val itemName:TextView = view.findViewById(R.id.txtCartItemName)
        val costOfOne:TextView = view.findViewById(R.id.txtCartCostForOne)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.itemName.text = cartList[position].name
        holder.costOfOne.text = "Rs. ${cartList[position].cost}"
    }
}