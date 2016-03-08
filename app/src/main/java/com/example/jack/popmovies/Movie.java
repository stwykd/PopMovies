package com.example.jack.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {

    private String mTitle,mPoster,mOverview,mReleaseDate,mRating;
    private JSONObject movieJson;

    public Movie(JSONObject movies) {
        movieJson = movies;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mPoster = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mRating = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getmTitle() {

        try {
            mTitle = movieJson.getString("original_title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPoster(String size) {

        try {
            mPoster = movieJson.getString("poster_path");
            mPoster = "http://image.tmdb.org/t/p/"+size+"/"+ mPoster;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getmOverview() {

        try {
            mOverview = movieJson.getString("overview");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        try {
            mReleaseDate = movieJson.getString("release_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmRating() {
        try {
            mRating = movieJson.getString("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // dest.writeString(movieJson.toString());
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mRating);
    }
}
