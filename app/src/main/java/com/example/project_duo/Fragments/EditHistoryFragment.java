package com.example.project_duo.Fragments;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
 * Use the {@link EditHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditHistoryFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("story");

    Button updateData;
    EditText history;
    TextView mark1, mark2, title;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditHistoryFragment newInstance(String param1, String param2) {
        EditHistoryFragment fragment = new EditHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_history, container, false);

        updateData = (Button) view.findViewById(R.id.button16);
        title = (TextView) view.findViewById(R.id.textView22);
        history = (EditText) view.findViewById(R.id.editText26);
        mark1 = (TextView) view.findViewById(R.id.circle1h);
        mark2 = (TextView) view.findViewById(R.id.circle2h);

        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");

        mark1.getBackground().setColorFilter(ContextCompat.getColor(getActivity(),R.color.mb_gray), PorterDuff.Mode.SRC_IN);
        mark2.getBackground().setColorFilter(ContextCompat.getColor(getActivity(),R.color.dark_gray), PorterDuff.Mode.SRC_IN);
        updateData.setTypeface(tf_heavy);
        title.setTypeface(tf_heavy);
        history.setTypeface(tf_roman);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("texto")){
                    history.setText(dataSnapshot.child("texto").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("texto").setValue(history.getText().toString());
                Toast.makeText(getActivity(), "Text updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}