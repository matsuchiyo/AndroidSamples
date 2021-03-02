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

class MainActivity : AppCompatActivity() {

    private var myAdapter: MyAdapter? = null

    private var timer: Timer? = null

    private val clipOutlineProvider = @SuppressLint("NewApi") object : ViewOutlineProvider() {

        @SuppressLint("NewApi")
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 40f)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // シャドウを作成してビューをクリップする
        // https://developer.android.com/training/material/shadows-clipping?hl=ja
        // ViewOutlineProvider を使う
        // http://y-anz-m.blogspot.com/2018/06/viewoutlineprovider.html
        val carouselViewPager = findViewById<ViewPager2>(R.id.carouselViewPager)
        carouselViewPager.outlineProvider = clipOutlineProvider
        carouselViewPager.clipToOutline = true
    }


    override fun onResume() {
        super.onResume()

        val carouselViewPager = findViewById<ViewPager2>(R.id.carouselViewPager)
        val items = listOf(R.drawable.baseline_add_24, R.drawable.baseline_clear_24, R.drawable.baseline_keyboard_arrow_left_24)
        myAdapter = MyAdapter(this, items)
        carouselViewPager.adapter = myAdapter
        if (items.size > 1) {
            carouselViewPager.setCurrentItem(1, false)
        }

        carouselViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    val realIndex = (carouselViewPager.currentItem - 1 + items.size) % items.size
                    val newFakeIndex = realIndex + 1
                    carouselViewPager.setCurrentItem(newFakeIndex, false)
                }
            }
        })

        if (timer != null) {
            timer?.cancel()
            timer = null
        }

        timer = Timer()
        timer?.schedule(object: TimerTask() {
            override fun run() {
                val itemCount = myAdapter?.itemCount ?: return
                if (itemCount > 1) {
                    val nextIndex = (carouselViewPager.currentItem + 1) % itemCount
                    carouselViewPager.setCurrentItem(nextIndex, true)
                }
            }
        }, 0, 3000)
    }


    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }
}

class MyAdapter(val context: Context, var items: List<Int>): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_item, parent, false))
    }

    override fun getItemCount(): Int {
        return if (items.size > 1) {
            items.size + 2
        } else {
            items.size
        }
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val index = if (items.size > 1) (position - 1 + items.size) % items.size else items.size
        holder.itemView.findViewById<ImageView>(R.id.imageView).setImageDrawable(context.getDrawable(items[index]))
    }

}

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
