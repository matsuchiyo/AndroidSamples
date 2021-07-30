package com.matsuchiyo.customviews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matsuchiyo.customviews.circleprogressview.CircleProgressViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MenuItemAdapter()
        recyclerView.adapter = adapter
//        adapter.notifyDataSetChanged()
        adapter.onItemClickListener = {
            when (it) {
                MenuItemType.CIRCLE_PROGRESS_VIEW -> {
                    startActivity(Intent(this, CircleProgressViewActivity::class.java))
                }
            }
        }
    }
}