package com.example.project_duo.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_duo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTab4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTab4Fragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("presentes");

    Button updateData;
    EditText text, sign;
    TextView title;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditTab4Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTab4Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTab4Fragment newInstance(String param1, String param2) {
        EditTab4Fragment fragment = new EditTab4Fragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_tab4, container, false);

        updateData = (Button) view.findViewById(R.id.btn_update_gift);
        title = (TextView) view.findViewById(R.id.txt_gift_title);
        text = (EditText) view.findViewById(R.id.edt_gift_text);
        sign = (EditText) view.findViewById(R.id.edt_gift_sign);

        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");

        updateData.setTypeface(tf_heavy);
        title.setTypeface(tf_heavy);
        text.setTypeface(tf_roman);
        sign.setTypeface(tf_roman);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("agradecimento")) {
                    text.setText(dataSnapshot.child("agradecimento").getValue(String.class));
                }
                if(dataSnapshot.hasChild("ass")) {
                    sign.setText(dataSnapshot.child("ass").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("agradecimento").setValue(text.getText().toString());
                mRef.child("ass").setValue(sign.getText().toString());
                Toast.makeText(getActivity(), "Information updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}