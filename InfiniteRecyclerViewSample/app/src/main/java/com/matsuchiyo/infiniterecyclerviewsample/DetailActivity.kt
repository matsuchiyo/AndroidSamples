package com.matsuchiyo.infiniterecyclerviewsample

import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity: AppCompatActivity() {
    companion object {
        const val ITEM = "ITEM"
    }

    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureAnimatedTransition()

        setContentView(R.layout.activity_detail)

        item = intent.getSerializableExtra(ITEM) as Item

        findViewById<TextView>(R.id.name_text).text = item.name
        findViewById<Button>(R.id.back_button).setOnClickListener {
            // https://stackoverflow.com/a/45028546/8834586
            supportFinishAfterTransition()
        }
    }

    private fun configureAnimatedTransition() {
        with(window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                enterTransition = Fade()
                sharedElementEnterTransition = ChangeBounds()

                exitTransition = Fade()
                sharedElementExitTransition = ChangeBounds()
            }
        }
    }
}