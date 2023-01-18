package com.matsuchiyo.signinwithtwittersample

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity: AppCompatActivity() {
    companion object {
        const val AUTHORIZATION_URL = "AUTHORIZATION_URL"
        const val AUTH_VERIFIER = "AUTH_VERIFIER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val authorizationUrl = intent.getStringExtra(AUTHORIZATION_URL) ?: throw RuntimeException("AUTHORIZATION_URL is required.")

        val webView = findViewById<WebView>(R.id.web_view).also {
            it.settings.javaScriptEnabled = true
            it.webViewClient = object: WebViewClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url.toString()
                    val verifier = interceptCallbackAndGetVerifier(url) ?: return false
                    setResult(RESULT_OK, Intent().putExtra(AUTH_VERIFIER, verifier))
                    finish()
                    return true
                }
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    val verifier = interceptCallbackAndGetVerifier(url) ?: return false
                    setResult(RESULT_OK, Intent().putExtra(AUTH_VERIFIER, verifier))
                    finish()
                    return true
                }
            }
        }

        webView.loadUrl(authorizationUrl)
    }

    private fun interceptCallbackAndGetVerifier(url: String?): String? {
        Log.d("WebViewActivity", "***** url: $url")
        if (url?.startsWith(TwitterConstants.CALLBACK_URL) != true) {
            return null
        }
        return Uri.parse(url)?.getQueryParameter("oauth_verifier")
    }
}