package nl.tiebe.openbaarlyceumzeist.android.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.openbaarlyceumzeist.android.R
import nl.tiebe.openbaarlyceumzeist.android.databinding.BrowserFragmentBinding
import nl.tiebe.openbaarlyceumzeist.utils.server.LoginRequest
import nl.tiebe.openbaarlyceumzeist.utils.server.exchangeUrl
import nl.tiebe.openbaarlyceumzeist.utils.server.sendFirebaseToken


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BrowserFragment : Fragment() {

    private var _binding: BrowserFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = BrowserFragmentBinding.inflate(inflater, container, false)

        return binding.root

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView: WebView = binding.root.findViewById(R.id.webview)

        val loginUrl = LoginFlow.createAuthURL()

        webView.webViewClient = CustomWebViewClient(loginUrl.codeVerifier)
        webView.loadUrl(loginUrl.url)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    class CustomWebViewClient(private val codeVerifier: String) :
        WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            webResourceRequest: WebResourceRequest
        ): Boolean {
            if (webResourceRequest.url.toString().startsWith("m6loapp://oauth2redirect")) {
                Log.d("BrowserFragment", "Redirected to: ${webResourceRequest.url}")
                val uri = Uri.parse(webResourceRequest.url.toString().replace("#", "?"))
                uri.getQueryParameter("code")
                    ?.let { code ->
                        runBlocking {
                            launch {
                                val login = exchangeUrl(LoginRequest(code, codeVerifier))

                                //get firebase token
                                FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                    OnCompleteListener { task ->
                                        if (!task.isSuccessful) {
                                            Log.w("Firebase", "Fetching FCM registration token failed", task.exception)
                                            return@OnCompleteListener
                                        }

                                        // Get new FCM registration token
                                        val token = task.result
                                        runBlocking {
                                            launch {
                                                sendFirebaseToken(
                                                    login.accessTokens.accessToken,
                                                    token
                                                )
                                            }
                                        }
                                })
                            }
                        }
                    }

            } else {
                view.loadUrl(webResourceRequest.url.toString())
            }
            return true
        }


    }

}