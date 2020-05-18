package com.internshala.foodies.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.widget.AutoCompleteTextView

class Connectionmanager {
    fun checkConnectivity(context: Context):Boolean{
        val connectionmanager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val checkCollection:NetworkInfo? = connectionmanager.activeNetworkInfo

        return if(checkCollection?.isConnected != null){
            checkCollection.isConnected
        }else{
            false
        }
    }
}