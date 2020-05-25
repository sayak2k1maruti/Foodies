package com.internshala.foodies.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.internshala.foodies.R


class OrderPlaced : AppCompatActivity() {
    lateinit var btnOrderSuccessful:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        btnOrderSuccessful = findViewById(R.id.btnOrderSuccessful)
        btnOrderSuccessful.setOnClickListener {
            startActivity(Intent(this@OrderPlaced,HomePage::class.java))
            finish()
        }
    }

    override fun onBackPressed() {

    }

}
