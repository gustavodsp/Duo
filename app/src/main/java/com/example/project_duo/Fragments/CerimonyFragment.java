package com.example.project_duo.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_duo.MainActivity;
import com.example.project_duo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CerimonyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CerimonyFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(HomeFragment.codigo).child("evento");

    ImageView local;
    TextView endereco, nome;
    LinearLayout my_layout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CerimonyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CerimonyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CerimonyFragment newInstance(String param1, String param2) {
        CerimonyFragment fragment = new CerimonyFragment();
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
        View view = inflater.inflate(R.layout.fragment_cerimony, container, false);

        local = (ImageView) view.findViewById(R.id.imv_place);
        nome = (TextView) view.findViewById(R.id.txv_place);
        endereco = (TextView) view.findViewById(R.id.txv_address);
        my_layout = (LinearLayout) view.findViewById(R.id.my_layout);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String url = dataSnapshot.child("locationPhoto").getValue(String.class);
                Uri uri = Uri.parse(url);
                Glide.with(getActivity()).load(uri).centerCrop().into(local);

                endereco.setText(dataSnapshot.child("locationAddress").getValue(String.class));
                nome.setText(dataSnapshot.child("locationName").getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRef.child("info").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = li.inflate(R.layout.event_item,null);

                TextView titulo = (TextView) v.findViewById(R.id.title_info);
                TextView texto = (TextView) v.findViewById(R.id.text_info);

                titulo.setText(dataSnapshot.child("title").getValue(String.class));
                texto.setText(dataSnapshot.child("message").getValue(String.class));

                my_layout.addView(v);

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

        ImageButton botao = (ImageButton) view.findViewById(R.id.imb_maps);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity mainactivity = (MainActivity) getActivity();
                mainactivity.loadMaps();

            }
        });

        return view;
    }
}