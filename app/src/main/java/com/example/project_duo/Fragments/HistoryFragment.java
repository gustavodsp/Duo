package com.example.project_duo.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_duo.Others.RoundedImageView;
import com.example.project_duo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(HomeFragment.codigo).child("story");
    TextView texto, noivo, noiva;
    RoundedImageView fpnoivo, fpnoiva;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        fpnoivo = (RoundedImageView) view.findViewById(R.id.imv_noivo);
        fpnoiva = (RoundedImageView) view.findViewById(R.id.imv_noiva);
        noivo = (TextView) view.findViewById(R.id.txv_nome_noivo);
        noiva = (TextView) view.findViewById(R.id.txv_nome_noiva);
        texto = (TextView) view.findViewById(R.id.txv_history);
        texto.setMovementMethod(new ScrollingMovementMethod());

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String url = dataSnapshot.child("noivo").child("foto").getValue(String.class);
                Uri uri = Uri.parse(url);
                Glide.with(getActivity()).load(uri).centerCrop().into(fpnoivo);
                String url2 = dataSnapshot.child("noiva").child("foto").getValue(String.class);
                Uri uri2 = Uri.parse(url2);
                Glide.with(getActivity()).load(uri2).centerCrop().into(fpnoiva);

                noivo.setText(dataSnapshot.child("noivo").child("nome").getValue(String.class));
                noiva.setText(dataSnapshot.child("noiva").child("nome").getValue(String.class));

                texto.setText(dataSnapshot.child("texto").getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}