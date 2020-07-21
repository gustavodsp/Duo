package com.example.project_duo.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.project_duo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(HomeFragment.codigo).child("slideshow");
    ViewFlipper viewFlipper;
    Animation fade_in, fade_out;
    ProgressBar progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar3);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ImageView foto = new ImageView(getActivity());
                String url = dataSnapshot.child("foto").getValue(String.class);
                Uri uri = Uri.parse(url);
                Glide.with(getActivity()).load(uri).centerCrop().into(foto);
                viewFlipper.addView(foto);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fade_in = AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_out);

        fade_in.setDuration(1500);
        fade_out.setDuration(1500);

        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);

        viewFlipper.startFlipping();

        return view;
    }
}