package com.iaz.filmesfamosos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultsVideo implements Parcelable {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private ResultsVideo(Parcel source) {
        this.key = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
    }

    public static final Creator<ResultsVideo> CREATOR = new Creator<ResultsVideo>() {
        @Override
        public ResultsVideo createFromParcel(Parcel source) {
            return new ResultsVideo(source);
        }

        @Override
        public ResultsVideo[] newArray(int size) {
            return new ResultsVideo[size];
        }
    };
}