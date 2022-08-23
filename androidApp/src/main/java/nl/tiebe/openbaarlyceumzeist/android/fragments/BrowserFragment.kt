package nl.tiebe.openbaarlyceumzeist.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.tiebe.openbaarlyceumzeist.android.databinding.BrowserFragmentBinding


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        val loginFlow = LoginFlow(MainActivity.account)
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
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
/*
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
                                Tokens().getTokens(loginFlow, it, context)
                                Log.d("BrowserFragment", "Tokens")
                                (context as MainActivity).runOnUiThread {
                                    context.navController.navigate(R.id.action_browserFragment_to_FirstFragment)
                                    context.findViewById<ProgressBar>(R.id.progressBar_cyclic).visibility =
                                        View.VISIBLE
                                }
                                Magister.onFirstSignIn(context)
                            }
                        }
                    }

            }
            view.loadUrl(webResourceRequest.url.toString())
            return true
        }


    }*/

}