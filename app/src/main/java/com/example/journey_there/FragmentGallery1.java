package com.example.journey_there;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGallery1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGallery1 extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView fragment1_iv;

    public FragmentGallery1() {
        // Required empty public constructor
    }


    public static FragmentGallery1 newInstance(String param1, String param2) {
        FragmentGallery1 fragment = new FragmentGallery1();
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

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_gallery1, container, false);
        fragment1_iv  = (ImageView)rootview.findViewById(R.id.fragment1_iv);

        fragment1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), galleryActivity.class);
                intent.putExtra("gallery", 0);
                startActivityForResult(intent, 0);
            }
        });

        return  rootview;
    }
}