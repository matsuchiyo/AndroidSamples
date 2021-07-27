package com.example.overlappingrecyclerviewsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(OverlappingItemDecoration(this, offsetYFromPreviousItemInDp = 72f))

        val adapter = ItemAdapter()
        recyclerView.adapter = adapter

        viewModel.items.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }
}