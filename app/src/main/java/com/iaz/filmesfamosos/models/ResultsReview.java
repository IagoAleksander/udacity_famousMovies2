package com.iaz.filmesfamosos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultsReview implements Parcelable {

    private String author;

    private String content;

    private String url;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private ResultsReview(Parcel source) {

        this.author = source.readString();
        this.content = source.readString();
        this.url = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);

    }

    public static final Creator<ResultsReview> CREATOR = new Creator<ResultsReview>() {
        @Override
        public ResultsReview createFromParcel(Parcel source) {
            return new ResultsReview(source);
        }

        @Override
        public ResultsReview[] newArray(int size) {
            return new ResultsReview[size];
        }
    };
}