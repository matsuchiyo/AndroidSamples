package com.matsuchiyo.signinwithtwittersample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.OAuthAuthorization

class WebViewTwitterSignInActivity: AppCompatActivity() {

    private val getTwitterOAuthVerifier = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode != RESULT_OK) return@registerForActivityResult
        val verifier = it.data!!.getStringExtra(WebViewActivity.AUTH_VERIFIER)!!
        getTwitterAccessToken(verifier)
    }

    lateinit var oAuth: OAuthAuthorization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_twitter_sign_in)

        findViewById<Button>(R.id.sign_in_with_twitter_by_webview).setOnClickListener {
            signInWithTwitter()
        }
    }

    private fun signInWithTwitter() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // https://twitter4j.org/code-examples の「OAuth support」
                oAuth = OAuthAuthorization.newBuilder()
                    .oAuthConsumer(TwitterConstants.API_KEY, TwitterConstants.API_SECRET)
                    .build()
                val requestToken = oAuth.getOAuthRequestToken(TwitterConstants.CALLBACK_URL)
                withContext(Dispatchers.Main) {
                    getTwitterOAuthVerifier.launch(Intent(this@WebViewTwitterSignInActivity, WebViewActivity::class.java).also {
                        it.putExtra(WebViewActivity.AUTHORIZATION_URL, requestToken.authorizationURL)
                    })
                }
            }
        }
    }

    private fun getTwitterAccessToken(verifier: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = oAuth.getOAuthAccessToken(verifier)
                withContext(Dispatchers.Main) {
                    val textView = findViewById<TextView>(R.id.access_token_text)
//                    textView.text = "Access Token: [${accessToken.token}]"
                    textView.text = "Access Token: [${accessToken.token.replace(Regex("[0-9a-zA-Z]{1}"), "*")}]" // mask access token
                }
            }
        }
    }
}