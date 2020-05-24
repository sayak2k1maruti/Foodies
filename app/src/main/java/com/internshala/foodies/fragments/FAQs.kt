package com.internshala.foodies.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.internshala.foodies.R
import com.internshala.foodies.adapter.FAQDataAdapter
import com.internshala.foodies.model.FAQModel

/**
 * A simple [Fragment] subclass.
 */
class FAQs : Fragment() {
    lateinit var recyclerViewOfFAQ:RecyclerView
    val FAQs:List<FAQModel> = listOf(
        FAQModel("what is Foodies?",
            "It’s a food delevary application. At Foodies, you can order delivery online from your favorite local restaurants, liquor stores, grocery stores and dry cleaners."
        ),
        FAQModel("How can I sort my results?",
        "At the actionBar you can find a symbol for sorting by clicking there you can sort your result."),
        FAQModel("Can I cancel my order?",
        "No you can't after you placed the order successfully.As it is very harmful for the restaurants."),
        FAQModel("How can I see my order History?",
        "Just open the navigation drawer by clicking on HamburgerButton you can find a place to see previous order."),
        FAQModel("Can I placed order from different restaurant at a time?",
        "NO you can't place order from multiple restaurants simultaniously")
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_f_a_qs, container, false)

        recyclerViewOfFAQ = view.findViewById(R.id.recycleViewOfFAQ)
        recyclerViewOfFAQ.layoutManager = LinearLayoutManager(activity as Context)
        recyclerViewOfFAQ.adapter = FAQDataAdapter(FAQs,activity as Context)

        return view
    }


}
