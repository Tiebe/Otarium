package nl.tiebe.openbaarlyceumzeist.android.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.tiebe.magisterapi.api.account.LoginFlow
import nl.tiebe.openbaarlyceumzeist.account
import nl.tiebe.openbaarlyceumzeist.android.MainActivity
import nl.tiebe.openbaarlyceumzeist.android.R
import nl.tiebe.openbaarlyceumzeist.android.databinding.BrowserFragmentBinding
import nl.tiebe.openbaarlyceumzeist.magister.Magister
import nl.tiebe.openbaarlyceumzeist.magister.Tokens


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

        val loginFlow = LoginFlow(account)
        val webView: WebView = binding.root.findViewById(R.id.webview)

        webView.webViewClient = CustomWebViewClient(binding.root.context, loginFlow)
        webView.loadUrl(loginFlow.createAuthURL())
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

    class CustomWebViewClient(private val context: Context, private val loginFlow: LoginFlow) :
        WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            webResourceRequest: WebResourceRequest
        ): Boolean {
            if (webResourceRequest.url.toString().startsWith("m6loapp://oauth2redirect")) {
                Log.d("BrowserFragment", "Redirected to: ${webResourceRequest.url}")
                val uri = Uri.parse(webResourceRequest.url.toString().replace("#", "?"))
                uri.getQueryParameter("code")
                    ?.let {
                        runBlocking {
                            launch {
                                Tokens.getTokens(loginFlow, it)
                                Log.d("BrowserFragment", "Tokens")
                                (context as MainActivity).runOnUiThread {
                                    context.navController.navigate(R.id.action_browserFragment_to_FirstFragment)
                                    context.findViewById<ProgressBar>(R.id.progressBar_cyclic).visibility =
                                        View.VISIBLE
                                }
                                Magister.onFirstSignIn()
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