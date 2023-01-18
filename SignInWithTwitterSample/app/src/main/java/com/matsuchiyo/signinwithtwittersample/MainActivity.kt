package com.matsuchiyo.signinwithtwittersample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.sign_in_with_twitter_by_webview).setOnClickListener {
            startActivity(Intent(this, WebViewTwitterSignInActivity::class.java))
        }

        findViewById<Button>(R.id.sign_in_with_twitter_by_custom_tab).setOnClickListener {
            startActivity(Intent(this, CustomTabTwitterSignInActivity::class.java))
        }
    }
}