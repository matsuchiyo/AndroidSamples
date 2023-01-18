package com.matsuchiyo.browserfallbackurltest

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.web_view)
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://tagpad.matsuchiyo.com/static/test.html")

        webView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                if (Uri.parse(url).scheme == "intent") {
                    Log.i("AAA", "*** aaa")
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    if (intent.resolveActivity(packageManager) != null) {
                        Log.i("AAA", "*** bbb")
                        startActivity(intent)
                        return true
                    }
                    Log.i("AAA", "*** ccc")
                    // Playストアを呼び出す実装
//                    return launchPlayStore(request.url)
                }
                Log.i("AAA", "*** ddd")
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }
}