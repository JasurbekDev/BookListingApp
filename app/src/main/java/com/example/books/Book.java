package com.example.books;

import android.graphics.Bitmap;

public class Book {
    private String mBookTitle;
    private String mBookAuthor;
    private Bitmap mBookThumbnailBitmap;
    private double mPriceAmount;
    private String mCurrencyCode;
    private String mInfoLink;
    private double mAverageRating;

    public Book(String mBookTitle, String mBookAuthor, Bitmap mBookThumbnailBitmap, double mPriceAmount, String mCurrencyCode, String mInfoLink, double mAverageRating) {
        this.mBookTitle = mBookTitle;
        this.mBookAuthor = mBookAuthor;
        this.mBookThumbnailBitmap = mBookThumbnailBitmap;
        this.mPriceAmount = mPriceAmount;
        this.mCurrencyCode = mCurrencyCode;
        this.mInfoLink = mInfoLink;
        this.mAverageRating = mAverageRating;
    }

    public Book(String mBookTitle, String mBookAuthor, Bitmap mBookThumbnailBitmap, String mInfoLink, double mAverageRating) {
        this.mBookTitle = mBookTitle;
        this.mBookAuthor = mBookAuthor;
        this.mBookThumbnailBitmap = mBookThumbnailBitmap;
        this.mInfoLink = mInfoLink;
        this.mAverageRating = mAverageRating;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }

    public Bitmap getmBookThumbnailBitmap() {
        return mBookThumbnailBitmap;
    }

    public double getmPriceAmount() {
        return mPriceAmount;
    }

    public String getmCurrencyCode() {
        return mCurrencyCode;
    }

    public String getmInfoLink() {
        return mInfoLink;
    }

    public double getmAverageRating() {
        return mAverageRating;
    }
}
