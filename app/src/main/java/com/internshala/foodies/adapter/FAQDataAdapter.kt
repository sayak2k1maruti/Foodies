package com.internshala.foodies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodies.R
import com.internshala.foodies.model.FAQModel

class FAQDataAdapter(val FAQs:List<FAQModel>,val context:Context) :RecyclerView.Adapter<FAQDataAdapter.FAQDataHolder>(){
    class FAQDataHolder(view:View):RecyclerView.ViewHolder(view){
        val question:TextView = view.findViewById(R.id.txtFaq_question)
        val answer:TextView = view.findViewById(R.id.txtFaq_answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQDataHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_of_faq, parent, false)

        return FAQDataHolder(view)
    }

    override fun getItemCount(): Int {
        return FAQs.size
    }

    override fun onBindViewHolder(holder: FAQDataHolder, position: Int) {
        holder.question.text = FAQs[position].question
        holder.answer.text = FAQs[position].answer
    }
}