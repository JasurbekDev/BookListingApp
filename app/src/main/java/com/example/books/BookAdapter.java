package com.example.books;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View lv = convertView;

        if(lv == null) {
            lv = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        assert currentBook != null;
        TextView bookTitle = lv.findViewById(R.id.book_title);
        bookTitle.setText(currentBook.getmBookTitle());

        TextView bookAuthor = lv.findViewById(R.id.book_author);
        if(!TextUtils.isEmpty(currentBook.getmBookAuthor())) {
            bookAuthor.setText(currentBook.getmBookAuthor());
        } else {
            bookAuthor.setText("Author is not available");
        }
        ImageView bookThumbnail = lv.findViewById(R.id.book_thumbnail);
        if(currentBook.getmBookThumbnailBitmap() != null) {
            bookThumbnail.setImageBitmap(currentBook.getmBookThumbnailBitmap());
        } else {
            bookThumbnail.setImageResource(R.drawable.no_image);
        }

        List<ImageView> stars = new ArrayList<>();
        ImageView star1 = lv.findViewById(R.id.star1);
        ImageView star2 = lv.findViewById(R.id.star2);
        ImageView star3 = lv.findViewById(R.id.star3);
        ImageView star4 = lv.findViewById(R.id.star4);
        ImageView star5 = lv.findViewById(R.id.star5);
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);

        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setImageResource(R.drawable.ic_star_white_24dp);
        }

        TextView averageRating = lv.findViewById(R.id.average_rating);

        DecimalFormat averageRetingFormat = new DecimalFormat("0.0");
        String formattedRating = averageRetingFormat.format(currentBook.getmAverageRating());

        if(!Double.isNaN(currentBook.getmAverageRating())) {
            averageRating.setText(Double.toString(currentBook.getmAverageRating()));
            boolean isHalf = false;
            if(Math.floor(currentBook.getmAverageRating() * 10) % 10 != 0) {
                isHalf = true;
            }

            double averageRatingFloor = Math.floor(currentBook.getmAverageRating());

            for (int i = 0; i < averageRatingFloor; i++) {
                if(isHalf && i + 1 == averageRatingFloor) {
                    stars.get(i).setImageResource(R.drawable.ic_star_black_24dp);
                    stars.get(i + 1).setImageResource(R.drawable.ic_star_half_black_24dp);
                    break;
                }
                stars.get(i).setImageResource(R.drawable.ic_star_black_24dp);
            }
        } else {
            averageRating.setText("N/A");
        }



        TextView bookPrice = lv.findViewById(R.id.book_price);
        TextView bookCurrencyCode = lv.findViewById(R.id.book_currency_code);
        DecimalFormat formatter = new DecimalFormat("0.00");
        double priceAmount = currentBook.getmPriceAmount();
        if(currentBook.getmPriceAmount() != 0) {
            bookCurrencyCode.setVisibility(View.VISIBLE);
            bookPrice.setText(formatter.format(priceAmount));
            bookCurrencyCode.setText(currentBook.getmCurrencyCode());
        } else {
            bookPrice.setText("Price is not available");
            bookCurrencyCode.setVisibility(View.GONE);
        }

        return lv;
    }
}
