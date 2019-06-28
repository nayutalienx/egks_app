package com.example.demochat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PaymentFragment extends Fragment {

    WebView webView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_payment,container,false);

        webView = view.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        final String payberry_url = "https://shop.payberry.ru/pay/4253";
        webView.loadUrl(payberry_url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);

        webView.loadUrl( "javascript:window.onload = (function(){ document.getElementsByName('inputprop__0')[0].value = 000123975; })()");
//
        return view;
    }



}
