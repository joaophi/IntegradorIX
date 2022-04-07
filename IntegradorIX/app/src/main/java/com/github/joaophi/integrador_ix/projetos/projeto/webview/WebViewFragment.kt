package com.github.joaophi.integrador_ix.projetos.projeto.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.joaophi.integrador_ix.R
import com.github.joaophi.integrador_ix.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment(R.layout.fragment_web_view) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWebViewBinding.bind(view)
        val args: WebViewFragmentArgs by navArgs()

        binding.root.webViewClient = WebViewClient();
        binding.root.loadUrl(args.link)
    }
}