package com.smartsense.taxinearyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends AppCompatActivity {
    WebView wvTerms;
    private ImageView btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_web_view);
        wvTerms = (WebView) findViewById(R.id.webView);


        wvTerms.setWebViewClient(new MyBrowser());
        wvTerms.getSettings().setLoadsImagesAutomatically(true);
        wvTerms.getSettings().setJavaScriptEnabled(true);
        wvTerms.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


//        String url = "http://www.example.com/login.php";
//        String data = "VPSProtocol=3.0&TxType=PAYMENT&Vendor="+getIntent().getStringExtra("vendor")+"&Crypt="+getIntent().getStringExtra("key");
//
//        wvTerms.postUrl(url, EncodingUtils.getBytes(data, "base64"));


        Map hm = new HashMap();
        hm.put("VPSProtocol", "3.0");
        hm.put("TxType", "PAYMENT");
        hm.put("Vendor", getIntent().getStringExtra("vendor"));//"taxinearultd"
        hm.put("Crypt", getIntent().getStringExtra("key"));

        Collection<Map.Entry<String, String>> postData = hm.entrySet();
        webview_ClientPost(wvTerms, "https://test.sagepay.com/gateway/service/vspform-register.vsp", postData);
//        wvTerms.loadUrl("http://ssgm.host56.com/index.html");

    }

    public static void webview_ClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");

        webView.loadData(sb.toString(), "text/html", "UTF-8");
    }

//    public static void webview_ClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><head></head>");
//        sb.append("<body>");
//        sb.append(String.format("<form id='SagePayForm' name='SagePayForm' action='%s' method='%s'>", url, "post"));
//        for (Map.Entry<String, String> item : postData) {
//            Log.i("Data", item.getKey() + " " + item.getValue());
//            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
//        }
//        sb.append("</form></body></html>");
//        webView.loadData(sb.toString(), "text/html", "UTF-8");
//    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.i("url", url);
            if (url.contains("closewebview")) {
                String[] getParam = url.split("=");
                Intent i = new Intent();
                i.putExtra("msg", getParam[2]);
                if (getParam[1].contains("success")) {
                    setResult(Activity.RESULT_OK, i);
                } else {
                    setResult(Activity.RESULT_CANCELED, i);
                }
                finish();
            }
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
