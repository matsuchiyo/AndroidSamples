package com.matsuchiyo.customviews.circleprogressview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.matsuchiyo.customviews.R

class CircleProgressViewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_progress_view)

        findViewById<CircleProgressView>(R.id.circle_progress).apply {
            setUp(0xFFDDDDDD.toInt(), 0xFF888888.toInt())
            update(0.40f)
        }
    }
}