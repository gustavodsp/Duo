package com.example.project_duo.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_duo.MainActivity;
import com.example.project_duo.Others.AuxiliarRVClickListener;
import com.example.project_duo.Others.FileInfoPortal;
import com.example.project_duo.Others.PortalAdapter;
import com.example.project_duo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorFragment extends Fragment implements AuxiliarRVClickListener {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    RecyclerView rvPortal;
    List<FileInfoPortal> edit_portal;
    TextView eventName, status, portal;
    Button startDue;
    ImageButton editName, share;

    int tela;
    int hasName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditorFragment newInstance(String param1, String param2) {
        EditorFragment fragment = new EditorFragment();
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
        View view = inflater.inflate(R.layout.fragment_editor, container, false);

        rvPortal = (RecyclerView) view.findViewById(R.id.rv_portal);
        eventName = (TextView) view.findViewById(R.id.textView38);
        status = (TextView) view.findViewById(R.id.textView78);
        portal = (TextView) view.findViewById(R.id.textView77);
        editName = (ImageButton) view.findViewById(R.id.imageButton5);
        share = (ImageButton) view.findViewById(R.id.imageButton11);
        startDue = (Button) view.findViewById(R.id.button14);

        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");
        final Typeface tf_black = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Black.otf");

        startDue.setTypeface(tf_heavy);
        eventName.setTypeface(tf_black);
        portal.setTypeface(tf_black);

        if(TextUtils.isEmpty(LoginFragment.login_versionName)){
            status.setText("Offline");
            final EditText input = new EditText(getActivity());
            input.setHint("Write the name");
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Version's name")
                    .setMessage("It's time to choose a name for the version you have acquired")
                    .setView(input)
                    .setPositiveButton("Create", null)
                    .setCancelable(false)
                    .create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(input.getText().toString().equals("")){
                                Toast.makeText(getActivity(),"Please, choose a name before you click to create!",Toast.LENGTH_LONG).show();
                            }else {
                                hasName=0;
                                mRef.child("Codes").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if(dataSnapshot.child("eventName").getValue(String.class).equals(input.getText().toString())){
                                            hasName=1;
                                        }
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
                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(hasName==0) {
                                            String uniqueID = UUID.randomUUID().toString();
                                            LoginFragment.login_versionName = input.getText().toString();
                                            LoginFragment.login_eventID = uniqueID;
                                            eventName.setText(input.getText().toString());
                                            startDue.setVisibility(View.VISIBLE);

                                            mRef.child("Users").child(LoginFragment.login_user).child("eventName").setValue(input.getText().toString());
                                            mRef.child("Users").child(LoginFragment.login_user).child("eventID").setValue(uniqueID);
                                            mRef.child("Users").child(LoginFragment.login_user).child("nameSet").setValue(true);

                                            mRef.child("Codes").child(uniqueID).child("eventName").setValue(input.getText().toString());
                                            mRef.child("Codes").child(uniqueID).child("user").setValue(LoginFragment.login_user);

                                            loadCelulas();
                                            dialog.dismiss();
                                        }else{
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Oops...")
                                                    .setMessage("This name has been used on another event. Please, try a different name!")
                                                    .setPositiveButton("Ok",null)
                                                    .show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                }
            });
            dialog.show();
        }else{
            eventName.setText(LoginFragment.login_versionName);
            mRef.child("Codes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(LoginFragment.login_eventID).hasChild("dateStart")){
                        status.setText("Online");
                        startDue.setVisibility(View.INVISIBLE);
                    }else{
                        status.setText("Offline");
                        startDue.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            loadCelulas();
        }

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasName=0;
                final EditText input = new EditText(getActivity());
                input.setHint("Write the new name");
                new AlertDialog.Builder(getActivity())
                        .setTitle("Version's name")
                        .setMessage("What is going to be the new name of your version?")
                        .setView(input)
                        .setNeutralButton("Cancel", null)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRef.child("Codes").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if(dataSnapshot.child("eventName").getValue(String.class).equals(input.getText().toString())){
                                            hasName=1;
                                        }
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
                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(hasName==0) {
                                            mRef.child("Users").child(LoginFragment.login_user).child("eventName").setValue(input.getText().toString());
                                            mRef.child("Codes").child(LoginFragment.login_eventID).child("eventName").setValue(input.getText().toString());
                                            eventName.setText(input.getText().toString());
                                            LoginFragment.login_versionName = input.getText().toString();
                                        }else{
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Oops...")
                                                    .setMessage("This name has been used on another event. Please, try a different name!")
                                                    .setPositiveButton("Ok",null)
                                                    .show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download 'Duo' app on Google Play and access the version using the code "+LoginFragment.login_versionName);

                startActivity(Intent.createChooser(shareIntent, "Choose where you want to share"));
            }
        });

        startDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int dia=c.get(Calendar.DAY_OF_MONTH);
                int mes=c.get(Calendar.MONTH);
                int ano=c.get(Calendar.YEAR);
                String todayDate = (dia+"."+(mes+1)+"."+ano);

                mRef.child("Codes").child(LoginFragment.login_eventID).child("dateStart").setValue(todayDate);

                status.setText("Online");
                startDue.setVisibility(View.INVISIBLE);

            }
        });

        return view;
    }

    public void loadCelulas(){

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPortal.setLayoutManager(llm);

        String[] titles = new String[]{"Couple screen","Groomsman/Bridesmaid","Event", "Gifts"};
        int[] icons = new int[]{R.drawable.video, R.drawable.padrinhos, R.drawable.map, R.drawable.cart};
        String[] num_itens = new String[]{"7 items", "1 item", "4 items", "1 item"};

        edit_portal = new ArrayList<>();
        for(int i=0;i<4;i++){
            FileInfoPortal celula = new FileInfoPortal(titles[i],num_itens[i],icons[i]);
            edit_portal.add(celula);
        }
        PortalAdapter adapter = new PortalAdapter(getActivity(),edit_portal);
        adapter.setAuxiliarRVClickListener(this);
        rvPortal.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvPortal);

    }

    @Override
    public void onClickListener(View view, int position) {

        MainActivity mainActivity = (MainActivity) getActivity();

        switch (position){
            case 0:

                mainActivity.loadEditTab1();

                break;
            case 1:

//                mainActivity.loadEditTab2();

                break;
            case 2:

//                mainActivity.loadEditTab3();

                break;
            case 3:

//                mainActivity.loadEditTab4();

                break;
        }

    }
}