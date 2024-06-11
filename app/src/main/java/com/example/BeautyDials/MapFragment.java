package com.example.BeautyDials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.example.BeautyDials.databinding.FragmentMapBinding;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    FragmentMapBinding binding;
    User user;
    MyListAdapter adapter;
    ArrayList<Add> adds = new ArrayList<Add>();
    private static final int YOUR_REQUEST_CODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(getLayoutInflater());

        // Initialize WebView
        WebView webView = binding.webview;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Wait for the button to be rendered (this is just an example, you might need to adjust the delay)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Execute the JavaScript to click the button
                        webView.evaluateJavascript("javascript:document.querySelector('button.accept-all').click();", null);
                    }
                }, 1000);
            }
        });

        // Load the URL
        webView.loadUrl("https://www.google.ru/maps/search/%D1%81%D0%B0%D0%BB%D0%BE%D0%BD+%D0%BA%D1%80%D0%B0%D1%81%D0%BE%D1%82%D1%8B/@52.6534031,41.8693604,16z/data=!3m1!4b1?entry=ttu");

        // ...

        return binding.getRoot();
    }
}
