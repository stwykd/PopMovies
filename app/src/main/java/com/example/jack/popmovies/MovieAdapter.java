package com.example.jack.popmovies;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends BaseAdapter {
    Context mContext;
    private List<Movie> items;

    public MovieAdapter(Context context, List<Movie> items) {
        mContext = context;
        this.items = items;
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView==null)
            image = new ImageView(mContext);
        else
            image = (ImageView) convertView;

        Picasso.with(mContext)
                .load(items.get(position).getmPoster("w500"))
                .error(android.R.drawable.sym_def_app_icon)
                .into(image);

        return image;
    }
}
