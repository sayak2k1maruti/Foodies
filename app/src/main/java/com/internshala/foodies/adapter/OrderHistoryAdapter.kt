package com.internshala.foodies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodies.R
import com.internshala.foodies.model.PreviousOrders

class OrderHistoryAdapter (val context: Context,val dataList:List<PreviousOrders>):RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>(){
    class OrderHistoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtPreviousOrderRestaurantName:TextView = view.findViewById(R.id.txtPreviousOrderRestaurantName)
        val txtDateOfOrder:TextView = view.findViewById(R.id.txtDateOfOrder)
        val recyclerViewOFPreviousOrders:androidx.recyclerview.widget.RecyclerView = view.findViewById(R.id.recyclerViewOFPreviousOrders)
        val txtTimeOfOrder:TextView = view.findViewById(R.id.txtTimeOfOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_ordered_restaurant, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.txtPreviousOrderRestaurantName.text = dataList[position].restaurantName
        holder.txtDateOfOrder.text = dataList[position].timeOfOrder.take(8)
        holder.txtTimeOfOrder.text = dataList[position].timeOfOrder.takeLast(8)
        holder.recyclerViewOFPreviousOrders.layoutManager = LinearLayoutManager(context)
        holder.recyclerViewOFPreviousOrders.adapter = CartAdapter(context,dataList[position].foodItems)
    }
}