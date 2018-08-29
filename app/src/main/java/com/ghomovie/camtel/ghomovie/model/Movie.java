package com.ghomovie.camtel.ghomovie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String URL= "http://image.tmdb.org/t/p/w185//";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLg() {
        return lg;
    }

    public void setLg(String lg) {
        this.lg = lg;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    private Long id;
    private String title;
    private String lg;
    private String overview;
    private String release_date;
    private Double vote_average;
    private String image;

    public  Movie(){}

    public Movie(Long id,
                 String title,
                 String lg,
                 String overview,
                 String release_date,
                 Double vote_average,
                 String image){

        this.id=id;
        this.title = title;
        this.lg= lg;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.image = URL+image;

    }
    /*Todo( to be removed)*/
    public Movie(String image){
        this.image = image;

    }

    private Movie(Parcel in){
        Long hashid = in.readLong();
        if (hashid == 1) {
            this.id = in.readLong();
        }
        else {
            this.id = null;
        }
        this.title = in.readString();
        this.lg = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        Double hashrate = in.readDouble();
        if(hashrate==1){
            this.vote_average = in.readDouble();
        }else{
            this.vote_average = null;
        }

        this.image = in.readString();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = URL+image;
    }

    @Override
    public String toString() {
        return image;
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
        dest.writeString(this.title);
        dest.writeString(this.lg);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        if(this.vote_average==null){
            dest.writeDouble(0);
        }else{
            dest.writeDouble(1);
            dest.writeDouble(this.vote_average);
        }

        dest.writeString(this.image);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel){
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
