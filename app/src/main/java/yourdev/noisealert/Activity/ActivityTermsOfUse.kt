package yourdev.noisealert.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import yourdev.noisealert.R

class ActivityTermsOfUse : AppCompatActivity(){


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termos_de_uso)

        val webview = findViewById<WebView>(R.id.web_view_terms_of_use) as WebView
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.i("teste","")
            }
        }
        webview.loadUrl("https://www.yourdev.com.br/NoiseAlert/Noise_Alert_Termos_de_uso.pdf")
    }
}