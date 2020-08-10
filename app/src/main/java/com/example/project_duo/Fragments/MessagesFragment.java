package com.example.project_duo.Fragments;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_duo.MainActivity;
import com.example.project_duo.Others.FileInfoMsg;
import com.example.project_duo.Others.RoundedImageView;
import com.example.project_duo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference().child("Codes").child(HomeFragment.codigo).child("mensagens");

    ImageButton imageButton;

    FirebaseRecyclerAdapter<FileInfoMsg,MessageViewHolder> mAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<FileInfoMsg> options = new FirebaseRecyclerOptions.Builder<FileInfoMsg>()
                .setQuery(mRef,FileInfoMsg.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<FileInfoMsg, MessageViewHolder>(options) {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
                return new MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(MessageViewHolder viewHolder, final int position, final FileInfoMsg model) {

                viewHolder.messageText.setText(model.getMessage());
                viewHolder.nameText.setText(model.getName());

                final String url = model.getPhoto();
                if(url!=null) {
                    Uri uri = Uri.parse(url);
                    Glide.with(getActivity()).load(uri).centerCrop().into(viewHolder.profilePhoto);
                }else{
                    viewHolder.profilePhoto.setImageResource(R.drawable.hint);
                }

                viewHolder.msgLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final EditText input = new EditText(getActivity());

                        final AlertDialog aDialog = new AlertDialog.Builder(getActivity()).create();
                        aDialog.setTitle("Área Reservada");
                        aDialog.setMessage("Área destinada aos noivos. Se deseja remover a mensagem, digite a senha:");
                        aDialog.setView(input);
                        aDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(input.getText().toString().equals("Remove")){

                                    mAdapter.getRef(position).removeValue();
                                    Toast.makeText(getActivity(), "Mensagem deletada com sucesso", Toast.LENGTH_SHORT).show();

                                }
                                else{

                                    Toast.makeText(getActivity(), "Senha incorreta", Toast.LENGTH_SHORT).show();
                                    aDialog.dismiss();

                                }

                            }
                        });
                        aDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                aDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.dark_gray));
                            }
                        });
                        aDialog.show();

                        return true;
                    }
                });
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        imageButton = (ImageButton) view.findViewById(R.id.imb_newmsg);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadWrite();
            }
        });

        return view;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView nameText;
        RoundedImageView profilePhoto;
        LinearLayout msgLayout;

        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.txv_msgtext);
            nameText = (TextView)itemView.findViewById(R.id.txv_msgname);
            profilePhoto = (RoundedImageView) itemView.findViewById(R.id.imv_msgprofile);
            msgLayout = (LinearLayout) itemView.findViewById(R.id.msg_layout);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}