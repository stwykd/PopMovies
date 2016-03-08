package com.example.jack.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.Arrays;

public class MainActivityFragment extends Fragment {

    String[] items = new String[]{};
    String[] resulturls = new String[20];
    HttpURLConnection urlConnection = null;
    BufferedReader reader;
    JSONArray movieJsonArray;
    GridView gridView;
    Movie[] movie = new Movie[20];
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private GridViewAdapter mMovieAdapter;
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   new FetchMovieTask().execute();

    }

    @Override
    public void onStart() {
        super.onStart();
        //  FetchMovieTask movieTask = new FetchMovieTask();
        new FetchMovieTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movieGridVw);
        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortingmethod = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default_value));

            String apiKeyValue = "b2acc0277845b231d6f58fdb1488980a";

            try {
                final String FETCH_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(FETCH_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY,sortingmethod)
                        .appendQueryParameter(API_KEY,apiKeyValue)
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null){
                    Log.d(LOG_TAG, "input stream is null");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0){
                    return null;
                }
                JSONObject movielistovject = new JSONObject(buffer.toString());
                movieJsonArray = movielistovject.getJSONArray("results");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader !=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG,"error closing stream",e);
                        e.printStackTrace();
                    }
                }

            }

            return movieJsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray strings) {
            super.onPostExecute(strings);
            if (strings != null){

                movieJsonArray = strings;
                try {
                    getMovieImageUrlFromJson(movieJsonArray);
                    if (movie != null){
                        mMovieAdapter = new GridViewAdapter(getActivity(), Arrays.asList(movie));
                        gridView.setAdapter(mMovieAdapter);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Movie selMovieDetails = (Movie) mMovieAdapter.getItem(position);
                                String Title = selMovieDetails.getmTitle();
                                String ImageUrl = selMovieDetails.getmPoster("w185");
                                String Overview = selMovieDetails.getmOverview();
                                String ReleaseDate = selMovieDetails.getmReleaseDate();
                                String Rating = selMovieDetails.getmRating();

                                Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
                                        .putExtra("Title",Title)
                                        .putExtra("image",ImageUrl)
                                        .putExtra("Overview",Overview)
                                        .putExtra("Release",ReleaseDate)
                                        .putExtra("Rating",Rating);
                                startActivity(detailActivityIntent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void getMovieImageUrlFromJson(JSONArray movieJsonArray) throws JSONException {

        if (movieJsonArray != null){

            for (int i = 0; i < movieJsonArray.length();i++){
                movie[i] = new Movie(movieJsonArray.getJSONObject(i));

            }
        }
    }
}
