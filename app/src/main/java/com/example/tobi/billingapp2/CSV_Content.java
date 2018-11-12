package com.example.tobi.billingapp2;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;

import java.util.ArrayList;

public class CSV_Content extends AppCompatActivity {

    ArrayList<String[]> rowList;
    ListView csv_contentListView;
    WebView billing_table;
    String[][] csv_Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv__content);

        Intent intent = getIntent();
        rowList = (ArrayList) intent.getSerializableExtra("rowList");
        csv_Data = (String[][]) intent.getSerializableExtra("csvData");

        billing_table = (WebView) findViewById(R.id.table);
        WebSettings webSettings = billing_table.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        billing_table.setWebChromeClient(new WebChromeClient());
        Log.i("Debug", "CSV DATA LENGTH in CSV CONTENT: " + csv_Data.length);
        billing_table.addJavascriptInterface(new JavaScriptInterface(csv_Data, this), "Android");
        billing_table.loadUrl("file:///android_asset/www/index.html");

    }


    public class JavaScriptInterface {
        Context mContext;
        String[][] csvData;

        public JavaScriptInterface(String[][] csv_data, Context context) {
            mContext = context;
            this.csvData = csv_data;
            Log.i("Debug", " JSInterface Class: " + csvData.length);
        }

        @JavascriptInterface
        public String getDataArrayAsString() {
            String result = "";
            for (int i = 0; i < csvData.length; i++) {

                for (int j = 0; j < csvData[i].length; j++) {
                    //result += "'";
                    result += csvData[i][j];
                    if (j == csvData[i].length - 1) {
                        //result += "'";
                    } else {
                        result += "^*";
                    }
                }
                result += "***";

            }
            System.out.println(result);
            return result;
        }

        @JavascriptInterface
        public String test() {
            return "Bridge working";
        }

    }
}
