package com.example.project_duo.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.example.project_duo.MainActivity;
import com.example.project_duo.Others.FAQListAdapter;
import com.example.project_duo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users");

    String provider;

    public static String login_email, login_nome, login_user, login_versionName=null, login_eventID, login_photo=null;

    Button entrar, created;
    TextView cadastrar, back, help, redefinir;
    ImageButton gmail, face;
    EditText c_nome, c_email, c_senha, l_email, l_senha;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        l_email = (EditText) view.findViewById(R.id.edt_email);
        l_senha = (EditText) view.findViewById(R.id.edt_senha);

        entrar = (Button) view.findViewById(R.id.btn_entrar);
        cadastrar = (TextView) view.findViewById(R.id.txv_cadastrar);
        help = (TextView) view.findViewById(R.id.txv_help);
        redefinir = (TextView) view.findViewById(R.id.txv_redefinir);

        gmail = (ImageButton) view.findViewById(R.id.imb_google);
        face = (ImageButton) view.findViewById(R.id.imb_facebook);

        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");

        l_email.setTypeface(tf_roman);
        l_senha.setTypeface(tf_roman);
        entrar.setTypeface(tf_heavy);
        cadastrar.setTypeface(tf_heavy);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FAQListAdapter adapter = new FAQListAdapter(getActivity(),R.array.perguntas,R.array.respostas);

                DialogPlus.newDialog(getActivity())
                        .setAdapter(adapter)
                        .setGravity(Gravity.CENTER)
                        .create()
                        .show();

            }
        });

        redefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!l_email.getText().toString().equals("")) {
                    mAuth.sendPasswordResetEmail(l_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                new KAlertDialog(getActivity(),KAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Email has been sent")
                                        .setContentText("Please check the email we sent you with the step by step to change your password.")
                                        .setConfirmText("Ok")
                                        .confirmButtonColor(android.R.color.holo_blue_dark)
                                        .show();
                            } else {
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops")
                                                .setContentText(e.getLocalizedMessage())
                                                .setConfirmText("Ok")
                                                .confirmButtonColor(android.R.color.holo_blue_dark)
                                                .show();
                                    }
                                });
                            }
                        }
                    });
                } else{
                    new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops")
                            .setContentText("Fill the email field, so we can send you to the password redefinition page.")
                            .setConfirmText("Ok")
                            .confirmButtonColor(android.R.color.holo_blue_dark)
                            .show();
                }
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setGravity(Gravity.CENTER)
                        .setContentHolder(new ViewHolder(R.layout.dialog_cadastrar))
                        .create();

                c_nome = (EditText) dialog.getHolderView().findViewById(R.id.edt_register_name);
                c_email = (EditText) dialog.getHolderView().findViewById(R.id.edt_register_email);
                c_senha = (EditText) dialog.getHolderView().findViewById(R.id.edt_register_senha);

                c_nome.setTypeface(tf_roman);
                c_email.setTypeface(tf_roman);
                c_senha.setTypeface(tf_roman);

                created = (Button) dialog.getHolderView().findViewById(R.id.btn_register);
                created.setTypeface(tf_heavy);
                created.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAuth.createUserWithEmailAndPassword(c_email.getText().toString(),c_senha.getText().toString())
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(e.getLocalizedMessage())
                                        .confirmButtonColor(android.R.color.holo_blue_dark)
                                        .setConfirmText("OK")
                                        .show();
                            }
                        });

                    }
                });

                back = (TextView) dialog.getHolderView().findViewById(R.id.txt_voltar);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(l_email.getText().toString().equals("") || l_email.getText().toString().equals("")){
                    new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Fill correctly the fields email and password")
                            .setConfirmText("Ok")
                            .confirmButtonColor(android.R.color.holo_blue_dark)
                            .show();
                }else {

                    mAuth.signInWithEmailAndPassword(l_email.getText().toString(), l_senha.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Hmm")
                                                .setContentText("Email or password wrong. Try again!")
                                                .setConfirmText("Ok")
                                                .confirmButtonColor(android.R.color.holo_blue_dark)
                                                .show();
                                    }
                                }
                            });

                }

            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_construction))
                        .setGravity(Gravity.CENTER)
                        .create();
                dialogPlus.show();
            }
        });

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_construction))
                        .setGravity(Gravity.CENTER)
                        .create();
                dialogPlus.show();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null) {

                    for (UserInfo usuario: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                        if(!usuario.getProviderId().equals("firebase")){
                            provider = usuario.getProviderId();
                        }
                    }

                    login_versionName=null;
                    login_eventID=null;

                    login_user = user.getUid();

                    if(provider.equals("password")) {

                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(user.getUid())) {

                                    login_nome = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                                    login_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                                    login_photo = dataSnapshot.child(user.getUid()).child("photo").getValue(String.class);
                                    if(dataSnapshot.child(user.getUid()).hasChild("eventName")){
                                        login_versionName = dataSnapshot.child(user.getUid()).child("eventName").getValue(String.class);
                                        login_eventID = dataSnapshot.child(user.getUid()).child("eventID").getValue(String.class);
                                    }

                                } else{

                                    mRef.child(user.getUid()).child("name").setValue(c_nome.getText().toString());
                                    mRef.child(user.getUid()).child("email").setValue(c_email.getText().toString());
                                    login_nome = c_nome.getText().toString();
                                    login_email = c_email.getText().toString();

                                }


                                MainActivity mainactivity = (MainActivity) getActivity();
                                mainactivity.loadHome();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else{

                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(user.getUid())) {

                                    login_nome = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                                    login_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                                    login_photo = dataSnapshot.child(user.getUid()).child("photo").getValue(String.class);
                                    if(dataSnapshot.child(user.getUid()).hasChild("eventName")){
                                        login_versionName = dataSnapshot.child(user.getUid()).child("eventName").getValue(String.class);
                                        login_eventID = dataSnapshot.child(user.getUid()).child("eventID").getValue(String.class);
                                    }

                                } else{

                                    mRef.child(user.getUid()).child("name").setValue(user.getDisplayName());
                                    mRef.child(user.getUid()).child("email").setValue(user.getEmail());
                                    mRef.child(user.getUid()).child("photo").setValue(user.getPhotoUrl());
                                    login_nome = user.getDisplayName();
                                    login_email = user.getEmail();
                                    login_photo = user.getPhotoUrl().toString();

                                }

                                MainActivity mainactivity = (MainActivity) getActivity();
                                mainactivity.loadHome();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }
        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}