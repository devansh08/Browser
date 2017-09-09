package com.example.devansh.browser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class InputActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_URL = "com.example.devansh.browser.URL";

    String urlString, prefix;

	boolean isQuery;

    EditText url;
    Button openUrl;
    Spinner urlType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

		urlString = "";
		prefix = "";

		isQuery = false;

        url = (EditText) findViewById(R.id.editText);
        openUrl = (Button) findViewById(R.id.button);
        urlType = (Spinner) findViewById(R.id.spinner);

        urlType.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		                R.array.select_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       	urlType.setAdapter(adapter);

        openUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				updateUrlString();
                if (urlString.isEmpty()) {
                    url.setError("URL Empty");
                } else {
                    goToWebPage(urlString);
                }
            }
        });
    }

    public void goToWebPage(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);

        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String type = String.valueOf(parent.getItemAtPosition(position));

        if (type.equals("https")) {
            prefix = "https://www.";
        } else if (type.equals("http")) {
            prefix = "http://www.";
        } else if (type.equals("Google")) {
            prefix = "https://www.google.co.in/search?q=";
			isQuery = true;
        } else if (type.equals("DuckDuckGo")) {
            prefix = "https://www.duckduckgo.com/?q=";
            isQuery = true;
        } else {
            prefix = "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateUrlString() {
		if (isQuery) {
			String query = "";

			for (char ch : url.getText().toString().toCharArray()) {
				if (ch == ' ') {
					query += '+';
				} else {
					query += ch;
				}
			}

			urlString = prefix + query;
		} else {
			urlString = prefix + url.getText().toString();
		}
    }
}
