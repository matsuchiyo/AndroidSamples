package com.matsuchiyo.signinwithtwittersample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.OAuthAuthorization

class CustomTabTwitterSignInActivity: AppCompatActivity() {

    companion object {
        const val O_AUTH_AUTHORIZATION = "O_AUTH_AUTHORIZATION"
    }

    lateinit var oAuth: OAuthAuthorization

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CustomTabSignIn", "***** onCreate")
        super.onCreate(savedInstanceState)

        /*
        Log.d("CustomTabSignIn", "***** restore state start")
        (savedInstanceState?.getSerializable(O_AUTH_AUTHORIZATION) as OAuthAuthorization?)?.let {
            Log.d("CustomTabSignIn", "***** state exists")
            oAuth = it
        }
        Log.d("CustomTabSignIn", "***** restore state end")
         */

        setContentView(R.layout.activity_custom_tab_twitter_sign_in)

        getAccessTokenIfNeeded(intent.data)

        findViewById<Button>(R.id.sign_in_with_twitter_by_custom_tab).setOnClickListener {
            signInWithTwitter()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("CustomTab", "***** onNewIntent")
        super.onNewIntent(intent)
        getAccessTokenIfNeeded(intent?.data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("CustomTabSignIn", "***** onSaveInstanceState()")
//        outState.putSerializable(O_AUTH_AUTHORIZATION, oAuth)
        super.onSaveInstanceState(outState)
    }

    private fun signInWithTwitter() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // https://developer.twitter.com/en/docs/authentication/oauth-1-0a/obtaining-user-access-tokens
                // https://developer.twitter.com/en/docs/authentication/api-reference/request_token
                //   ↑の/oauth/request_tokenを呼ぶ時のHTTP Request Headerの作成が大変。
                //   なので、WebViewのやり方と同様に、Twitter4Jでrequest_tokenを取得。

                // https://twitter4j.org/code-examples の「OAuth support」
                oAuth = OAuthAuthorization.newBuilder()
                    .oAuthConsumer(TwitterConstants.API_KEY, TwitterConstants.API_SECRET)
                    .build()
                val requestToken = oAuth.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    val customTabsIntent = CustomTabsIntent.Builder().build()
                    customTabsIntent.launchUrl(this@CustomTabTwitterSignInActivity, Uri.parse(requestToken.authorizationURL))
                }
            }
        }
    }

    private fun getAccessTokenIfNeeded(callbackUrl: Uri?) {
        Log.d("CustomTab", "***** callbackUrl: ${callbackUrl?.toString()}")
        val verifier = callbackUrl?.getQueryParameter("oauth_verifier") ?: return
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = oAuth.getOAuthAccessToken(verifier)
                withContext(Dispatchers.Main) {
                    val textView = findViewById<TextView>(R.id.access_token_text)
                    textView.text = "Access Token [${accessToken.token}]"
                }
            }
        }
    }
}