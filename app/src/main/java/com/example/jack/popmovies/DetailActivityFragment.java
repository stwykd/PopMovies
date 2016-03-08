package com.example.jack.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {

    private TextView tvTitle,tvReldt,tvRate,tvDesp;
    private ImageView ivMovie;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvReldt = (TextView) view.findViewById(R.id.tvRelDt);
        tvRate = (TextView) view.findViewById(R.id.tvRating);
        tvDesp = (TextView) view.findViewById(R.id.tvDes);
        ivMovie = (ImageView) view.findViewById(R.id.imgMovie);

        Intent intent = getActivity().getIntent();

        if (intent != null){


            tvTitle.setText(intent.getExtras().getString("Title"));
            tvDesp.setText(intent.getExtras().getString("Overview"));
            tvRate.setText(intent.getExtras().getString("Release") );
            tvReldt.setText(intent.getExtras().getString("Rating") + "/10");

            Picasso.with(getContext())
                    .load(intent.getExtras().getString("image"))
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(ivMovie);


        }

        return view;
    }
}