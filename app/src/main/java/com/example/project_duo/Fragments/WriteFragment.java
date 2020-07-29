package com.example.project_duo.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.developer.kalert.KAlertDialog;
import com.example.project_duo.MainActivity;
import com.example.project_duo.Others.FileInfoMsg;
import com.example.project_duo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WriteFragment extends Fragment {

    EditText mensagem;
    Button botao;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference().child("Codes").child(HomeFragment.codigo).child("mensagens");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WriteFragment newInstance(String param1, String param2) {
        WriteFragment fragment = new WriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        mensagem = (EditText) view.findViewById(R.id.editText2);
        botao = (Button) view.findViewById(R.id.button8);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Avenir-Light.ttf");
        mensagem.setTypeface(tf);
        botao.setTypeface(tf);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mensagem.getText().toString().equals("")) {
                    new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Please, type a message")
                            .confirmButtonColor(android.R.color.holo_blue_dark)
                            .setConfirmText("OK")
                            .show();
                } else {

                    FileInfoMsg fileinfo = new FileInfoMsg();
                    fileinfo.setMessage(mensagem.getText().toString());
                    fileinfo.setName(LoginFragment.login_nome);
                    fileinfo.setEmail(LoginFragment.login_email);
                    fileinfo.setPhoto(LoginFragment.login_photo);

                    mRef.push().setValue(fileinfo);

                    new KAlertDialog(getActivity(),KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Message sent!")
                            .setContentText("Thank you very much to leave us a message! :)")
                            .setConfirmText("Back")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    kAlertDialog.dismissWithAnimation();
                                    MainActivity mainActivity = (MainActivity) getActivity();
                                    mainActivity.backfragment();
                                }
                            })
                            .show();

                }
            }
        });

        return view;
    }
}