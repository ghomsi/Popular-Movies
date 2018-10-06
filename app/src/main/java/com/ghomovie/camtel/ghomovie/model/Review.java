package com.ghomovie.camtel.ghomovie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private Long id;
    private String author;
    private String url;

    private Review(){}

    public Review(Long id, String author, String url) {
        this.id = id;
        this.author = author;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Review(Parcel in){
        Long hashid = in.readLong();
        if (hashid == 1) {
            this.id = in.readLong();
        }
        else {
            this.id = null;
        }
        this.author = in.readString();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(this.id==null){
            dest.writeLong(0);
        }else{
            dest.writeLong(1);
            dest.writeLong(this.id);
        }
        dest.writeString(this.author);
        dest.writeString(this.url);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>(){
        @Override
        public Review createFromParcel(Parcel parcel){
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
