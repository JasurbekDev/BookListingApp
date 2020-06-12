package com.example.books;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    private BookAdapter mAdapter;
    private static int loaderId = 1;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;
    private static String testUrl = "";
    private static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final EditText searchBar = findViewById(R.id.search_bar);
//        Button searchButton = findViewById(R.id.search_button);

        ListView bookList = findViewById(R.id.book_list);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        mEmptyStateTextView.setText(R.string.search_books);
        mProgressBar = findViewById(R.id.progress_bar);
        bookList.setEmptyView(mEmptyStateTextView);
        if(isFirst) {
            mProgressBar.setVisibility(View.GONE);
            isFirst = false;
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mEmptyStateTextView.setText(R.string.search_books);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(loaderId, null, MainActivity.this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookList.setAdapter(mAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = mAdapter.getItem(position);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedBook.getmInfoLink()));
                startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BookLoader(this, testUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            mEmptyStateTextView.setText(R.string.no_books);
        if(networkInfo == null) {
            mEmptyStateTextView.setText(R.string.no_internet);
        } else {
            if(books == null) {
                mEmptyStateTextView.setText(R.string.search_books);
            }
        }

//        if(!isFirst) {
//            mEmptyStateTextView.setVisibility(View.GONE);
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
        mProgressBar.setVisibility(View.GONE);
        if(books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.toolbar_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.clear();
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert connMgr != null;
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()) {
                    searchView.clearFocus();
//                    InputMethodManager imm = (InputMethodManager)getSystemService(
//                            Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    String[] split = query.split(" ");
                    String join = "";
                    for (int i = 0; i < split.length; i++) {
                        join += split[i] + ((i < split.length - 1) ? "+" : "");
                    }
                    testUrl = "https://www.googleapis.com/books/v1/volumes?q=" + join + "&maxResults=10";
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(++loaderId, null, MainActivity.this);
                    return true;
                } else {
                    mEmptyStateTextView.setText(R.string.no_internet);
                    return false;
                }

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
