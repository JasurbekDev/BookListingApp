package com.example.books;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {}

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractItemsFromJson(jsonResponse);
    }

    private static List<Book> extractItemsFromJson(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject arrayElement = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = arrayElement.getJSONObject("volumeInfo");
                JSONObject saleInfo = arrayElement.optJSONObject("saleInfo");
                JSONObject listPrice = saleInfo.optJSONObject("listPrice");
                JSONObject imageLinks;
                String imageLink;
                Bitmap imageBitmap;
                if(volumeInfo.has("imageLinks")) {
                    imageLinks = volumeInfo.optJSONObject("imageLinks");
                    imageLink = imageLinks.optString("thumbnail");
                    imageBitmap = getImageBitmap(imageLink);
                } else {
                    imageBitmap = null;
                }
                String title = volumeInfo.optString("title");
                JSONArray authors = volumeInfo.optJSONArray("authors");
                String author = "";
                if(authors != null) {
                    for (int i1 = 0; i1 < authors.length(); i1++) {
                        author += authors.getString(i1) + ((authors.length() <= 1 || i1 == authors.length() - 1) ? "" : ", ");
                    }
                }



                double priceAmount = 0;
                String currencyCode = "";

                if(saleInfo != null && listPrice != null) {
                    priceAmount = listPrice.optDouble("amount");
                    currencyCode = listPrice.optString("currencyCode");
                }


                String infoLink = volumeInfo.optString("infoLink");
                double averageRating = volumeInfo.optDouble("averageRating");

                Book book = new Book(title, author, imageBitmap, priceAmount, currencyCode, infoLink, averageRating);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    private static Bitmap getImageBitmap(String imageLink) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(imageLink).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null) {
            return jsonResponse;
        }
        HttpURLConnection connection = null;
        InputStream inputStream;

        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if(connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            assert connection != null;
            Log.e(LOG_TAG, "Error response code: " + connection.getResponseCode());
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String row;
            while((row = reader.readLine()) != null) {
                builder.append(row);
            }
        }
        return builder.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
