package com.example.project_duo.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_duo.Others.FileInfoInformacoes;
import com.example.project_duo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInfoFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("evento").child("info");

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView add;
    LinearLayout hint;

    EditText d_topico, d_conteudo;
    Button d_refresh;

    FirebaseRecyclerAdapter<FileInfoInformacoes,InfoViewHolder> adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditInfoFragment newInstance(String param1, String param2) {
        EditInfoFragment fragment = new EditInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);

        add = (TextView) view.findViewById(R.id.textView116);
        hint = (LinearLayout) view.findViewById(R.id.info_hint);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_info);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<FileInfoInformacoes> options = new FirebaseRecyclerOptions.Builder<FileInfoInformacoes>()
                .setQuery(mRef,FileInfoInformacoes.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<FileInfoInformacoes, InfoViewHolder>(options) {
            @NonNull
            @Override
            public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent, false);
                return new InfoViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(InfoViewHolder viewHolder, final int position, FileInfoInformacoes model) {
                if(model!=null){

                    hint.setVisibility(View.INVISIBLE);
                    viewHolder.titulo.setText(model.getTitle());
                    viewHolder.informacao.setText(model.getMessage());
                    viewHolder.deletar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapter.getRef(position).removeValue();
                            Toast.makeText(getActivity(), "Information deleted successfully", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    });

                } else{
                    hint.setVisibility(View.VISIBLE);
                }
            }
        };

        recyclerView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_info))
                        .setContentBackgroundResource(android.R.color.white)
                        .setGravity(Gravity.CENTER)
                        .create();

                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_info);
                d_topico = (EditText) dialog.findViewById(R.id.editText32);
                d_conteudo = (EditText) dialog.findViewById(R.id.editText33);
                d_refresh = (Button) dialog.findViewById(R.id.button21);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                linearLayout.setMinimumHeight(height);
                linearLayout.setMinimumWidth(width);

                d_refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(d_topico.getText().toString().equals("") || d_conteudo.getText().toString().equals("")){
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Oops...")
                                    .setMessage("Please, fill all the information.")
                                    .setPositiveButton("Ok",null)
                                    .show();
                        } else{
                            FileInfoInformacoes fileInfoInformacoes = new FileInfoInformacoes();
                            fileInfoInformacoes.setTitle(d_topico.getText().toString());
                            fileInfoInformacoes.setMessage(d_conteudo.getText().toString());
                            mRef.push().setValue(fileInfoInformacoes);
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, informacao, deletar;

        public InfoViewHolder(View itemView) {
            super(itemView);


            titulo = (TextView) itemView.findViewById(R.id.textView121);
            informacao = (TextView) itemView.findViewById(R.id.textView122);
            deletar = (TextView) itemView.findViewById(R.id.textView123);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}