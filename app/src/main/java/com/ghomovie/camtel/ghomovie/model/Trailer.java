package com.ghomovie.camtel.ghomovie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    private Long id;
    private String name;
    private String key;
    private String site;

    public Trailer(){}

    public Trailer(Long id, String name, String key, String site) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.site = site;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSite(String site) {
        this.site = site;
    }

    private Trailer(Parcel in){
        Long hashid = in.readLong();
        if (hashid == 1) {
            this.id = in.readLong();
        }
        else {
            this.id = null;
        }
        this.name = in.readString();
        this.key = in.readString();
        this.site = in.readString();
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
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.site);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>(){
        @Override
        public Trailer createFromParcel(Parcel parcel){
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getVideo(){
        return "https://www.youtube.com/watch?v="+this.key;
    }
}
