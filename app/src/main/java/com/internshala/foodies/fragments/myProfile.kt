package com.internshala.foodies.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.internshala.foodies.R


class myProfile : Fragment() {
    private lateinit var txtprofileName: TextView
    private lateinit var txtprofileMobile: TextView
    private lateinit var txtprofileEmail: TextView
    private lateinit var txtprofileAddress: TextView
    private lateinit var txtprofilePicLetter: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        txtprofileName = view.findViewById(R.id.txtProfileName)
        txtprofileAddress = view.findViewById(R.id.txtProfileAddress)
        txtprofileMobile = view.findViewById(R.id.txtProfileMobile)
        txtprofileEmail = view.findViewById(R.id.txtProfileEmail)
        txtprofilePicLetter = view.findViewById(R.id.txtprofilePicLetter)


        if (activity != null) {
            try {
                val sharedPreferences =
                    activity!!.getSharedPreferences("logInDetails", Context.MODE_PRIVATE)
                txtprofileEmail.text = sharedPreferences.getString("email", "email")
                txtprofileMobile.text = sharedPreferences.getString("mobile_number", "1111111111")
                txtprofileAddress.text = sharedPreferences.getString("address", "address")
                txtprofileName.text = sharedPreferences.getString("name", "Anonymous")
                txtprofilePicLetter.text = sharedPreferences.getString("name","Aonymous")?.take(1)
            } catch (e: Exception) {
                Toast.makeText(
                    activity as Context,
                    "The app fail to fetch data from shared Preference",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        return view
    }


}
