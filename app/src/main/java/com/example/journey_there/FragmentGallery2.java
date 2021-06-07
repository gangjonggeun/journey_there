package com.example.journey_there;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


public class FragmentGallery2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView fragment2_iv;

    public FragmentGallery2() {
        // Required empty public constructor
    }


    public static FragmentGallery2 newInstance(String param1, String param2) {
        FragmentGallery2 fragment = new FragmentGallery2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_gallery2, container, false);
        fragment2_iv  = (ImageView)rootview.findViewById(R.id.fragment2_iv);

        fragment2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), galleryActivity.class);
                intent.putExtra("gallery", 1);
                startActivityForResult(intent, 1);
            }
        });

        return  rootview;
    }
}