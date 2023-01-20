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
import org.json.JSONObject
import twitter4j.OAuthAuthorization

class CustomTabTwitterSignInActivity: AppCompatActivity() {

    lateinit var oAuth: OAuthAuthorization

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CustomTabSignIn", "***** onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_tab_twitter_sign_in)

        findViewById<Button>(R.id.sign_in_with_twitter_by_custom_tab).setOnClickListener {
            signInWithTwitter()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("CustomTab", "***** onNewIntent")
        super.onNewIntent(intent)
        getAccessTokenIfNeeded(intent?.data)
    }

    private fun signInWithTwitter() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // https://developer.twitter.com/en/docs/authentication/oauth-1-0a/obtaining-user-access-tokens
                // https://developer.twitter.com/en/docs/authentication/api-reference/request_token

                val twitterConsumerKeyJsonText: String = resources.openRawResource(R.raw.twitter_consumer_key).bufferedReader().use { it.readText() }
                val twitterConsumerKey = JSONObject(twitterConsumerKeyJsonText)

                // https://twitter4j.org/code-examples "OAuth support"
                oAuth = OAuthAuthorization.newBuilder()
                    .oAuthConsumer(
                        twitterConsumerKey.getString("consumerKey"),
                        twitterConsumerKey.getString("consumerSecret"),
                    )
                    .build()
                val requestToken = oAuth.getOAuthRequestToken(TwitterConstants.CALLBACK_URL)
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
                    textView.text = "Access Token [${accessToken.token.replace(Regex("[0-9a-zA-Z]{1}"), "*")}]"
                }
            }
        }
    }

    private fun invalidateAccessToken() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                /*
                // ログインしてからしばらく経ってからアプリを起動したときに、呼び出す場合
                val oAuth = OAuthAuthorization.newBuilder()
                    .oAuthConsumer(TwitterConstants.API_KEY, TwitterConstants.API_SECRET)
                    .oAuthAccessToken("access_token", "secret") // TODO: Get access token and secret that is saved in EncryptedSharedPreferences and so on.
                    .build()
                 */
                oAuth.invalidateOAuthToken()
            }
        }
    }
}