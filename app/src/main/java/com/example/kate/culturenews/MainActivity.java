package com.example.kate.culturenews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final int LOADER_ID = 1;
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?tag=culture/culture&show-fields=byline&api-key=6f5c4081-c8c0-4492-a38d-f326e599f574";

    private ArticleAdapter adapter;
    private TextView emptyView;
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list);

        loadingSpinner = findViewById(R.id.loading_spinner);

        emptyView = findViewById(R.id.empty);
        listView.setEmptyView(emptyView);

        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = adapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            emptyView.setText(R.string.no_internet);
            loadingSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String articleLimit = sharedPrefs.getString(
                getString(R.string.settings_article_limit_key),
                getString(R.string.settings_article_limit_default));

        String sectionFilter = sharedPrefs.getString(
                getString(R.string.settings_section_filter_key),
                getString(R.string.settings_section_filter_default)
        );

        Uri baseUri = Uri.parse(REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("page-size", articleLimit);

        if (sectionFilter != getString(R.string.settings_section_filter_all_value)) {
            uriBuilder.appendQueryParameter("section", sectionFilter);
        }

        return new ArticleLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        loadingSpinner.setVisibility(View.GONE);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else {
            emptyView.setText(getText(R.string.empty_state));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }
}
