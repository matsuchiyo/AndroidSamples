package com.example.carousel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.util.*

class CustomCompositeViewTestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_composite_view_test)
    }


    override fun onResume() {
        Log.i("CCC", "*** onResume")
        super.onResume()

        findViewById<CarouselView>(R.id.carouselView).images = listOf(R.drawable.baseline_add_24, R.drawable.baseline_clear_24, R.drawable.baseline_keyboard_arrow_left_24)
    }


    override fun onPause() {
        super.onPause()
    }
}

