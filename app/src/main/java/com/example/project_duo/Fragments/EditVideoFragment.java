package com.example.project_duo.Fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.example.project_duo.MainActivity;
import com.example.project_duo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditVideoFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("video");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    ImageView videoOk, musicaOk;
    TextView addVideo, addMusica, txt_video, txt_musica, txt_data;
    EditText data;
    Button atualizar;
    LinearLayout vSquare, mSquare;

    TextView d_nameplaying;
    ImageView d_play, d_pause;
    LinearLayout d_music1, d_music2, d_music3;
    Button d_enviar;

    String filepath, musicChoosed, dataString;

    public static final int REQUEST_VIDEO_STORAGE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditVideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditVideoFragment newInstance(String param1, String param2) {
        EditVideoFragment fragment = new EditVideoFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_video, container, false);

        videoOk = (ImageView) view.findViewById(R.id.imageView24);
        musicaOk = (ImageView) view.findViewById(R.id.imageView38);
        addVideo = (TextView) view.findViewById(R.id.textView92);
        addMusica = (TextView) view.findViewById(R.id.textView140);
        txt_video = (TextView) view.findViewById(R.id.textView90);
        txt_musica = (TextView) view.findViewById(R.id.textView138);
        txt_data = (TextView) view.findViewById(R.id.textView93);
        data = (EditText) view.findViewById(R.id.editText25);
        atualizar = (Button) view.findViewById(R.id.button15);
        vSquare = (LinearLayout) view.findViewById(R.id.video_square);
        mSquare = (LinearLayout) view.findViewById(R.id.music_square);

        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");

        txt_video.setTypeface(tf_heavy);
        txt_musica.setTypeface(tf_heavy);
        txt_data.setTypeface(tf_heavy);
        data.setTypeface(tf_roman);
        atualizar.setTypeface(tf_heavy);

        vSquare.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = vSquare.getMeasuredWidth();
                vSquare.setLayoutParams(new LinearLayout.LayoutParams(width,width));

                vSquare.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mSquare.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mSquare.getMeasuredWidth();
                mSquare.setLayoutParams(new LinearLayout.LayoutParams(width,width));

                mSquare.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("videoUrl")){
                    videoOk.setImageResource(R.drawable.tick);
                }

                if(dataSnapshot.hasChild("songUrl")){
                    musicaOk.setImageResource(R.drawable.tick);
                }

                if(dataSnapshot.hasChild("data")){
                    data.setText(dataSnapshot.child("data").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeriapermission();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent,"Choose a video"),REQUEST_VIDEO_STORAGE);
            }
        });

        addMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_music))
                        .setContentBackgroundResource(android.R.color.white)
                        .setGravity(Gravity.CENTER)
                        .create();

                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_musica);
                d_nameplaying = (TextView) dialog.findViewById(R.id.textView16);
                d_play = (ImageView) dialog.findViewById(R.id.imageView49);
                d_pause = (ImageView) dialog.findViewById(R.id.imageView50);
                d_music1 = (LinearLayout) dialog.findViewById(R.id.layout_music1);
                d_music2 = (LinearLayout) dialog.findViewById(R.id.layout_music2);
                d_music3 = (LinearLayout) dialog.findViewById(R.id.layout_music3);
                d_enviar = (Button) dialog.findViewById(R.id.button24);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                linearLayout.setMinimumHeight(height);
                linearLayout.setMinimumWidth(width);

                d_music1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!d_nameplaying.getText().toString().equals("The nest")) {
                            d_nameplaying.setText("The nest");
                            d_music1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.mb_gray));
                            d_music2.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                            d_music3.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                            musicChoosed = "https://firebasestorage.googleapis.com/v0/b/dueapp-e70bd.appspot.com/o/0Music%2FThe%20nest%20-%20Josh%20Woodward.mp3?alt=media&token=028931bc-3495-43d6-b82f-2628881665f4";
                            loadMusic(musicChoosed);
                            d_play.setVisibility(View.INVISIBLE);
                            d_pause.setVisibility(View.VISIBLE);
                        }
                    }
                });

                d_music2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!d_nameplaying.getText().toString().equals("Don't close your eyes")) {
                            d_nameplaying.setText("Don't close your eyes");
                            d_music1.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                            d_music2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.mb_gray));
                            d_music3.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                            musicChoosed = "https://firebasestorage.googleapis.com/v0/b/dueapp-e70bd.appspot.com/o/0Music%2FDon't%20close%20your%20eyes%20-%20Josh%20Woodward.mp3?alt=media&token=d0e65f19-20f2-4e6b-824e-b64985fed1f2";
                            loadMusic(musicChoosed);
                            d_play.setVisibility(View.INVISIBLE);
                            d_pause.setVisibility(View.VISIBLE);
                        }
                    }
                });

                d_music3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!d_nameplaying.getText().toString().equals("Bloom")) {
                            d_nameplaying.setText("Bloom");
                            d_music1.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                            d_music2.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                            d_music3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.mb_gray));
                            musicChoosed = "https://firebasestorage.googleapis.com/v0/b/dueapp-e70bd.appspot.com/o/0Music%2FBloom%20-%20Josh%20Woodward.mp3?alt=media&token=1a66078a-ad8d-48eb-92db-6e046309ab3f";
                            loadMusic(musicChoosed);
                            d_play.setVisibility(View.INVISIBLE);
                            d_pause.setVisibility(View.VISIBLE);
                        }
                    }
                });

                d_pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity) getActivity()).pausemusic();
                        d_play.setVisibility(View.VISIBLE);
                        d_pause.setVisibility(View.INVISIBLE);
                    }
                });

                d_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(MainActivity.mediaPlayer!=null){
                            ((MainActivity) getActivity()).startmusic();
                            d_play.setVisibility(View.INVISIBLE);
                            d_pause.setVisibility(View.VISIBLE);
                        }
                    }
                });

                d_enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(musicChoosed!=null){
                            musicaOk.setImageResource(R.drawable.tick);
                            dialog.dismiss();
                            MainActivity.mediaPlayer.release();
                        }
                    }
                });

                dialog.show();
            }
        });

        data.setInputType(InputType.TYPE_NULL);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int dia=c.get(Calendar.DAY_OF_MONTH);
                int mes=c.get(Calendar.MONTH);
                int ano=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        data.setText(d+"/"+(m+1)+"/"+y);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                        dataString = dateFormat.format(new Date(y-1900,m,d));
                        Log.i("lala", "onDateSet: "+dataString);

                    }
                },ano,mes,dia);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!data.getText().toString().equals("")){
                    mRef.child("data").setValue(data.getText().toString());
                    mRef.child("dataString").setValue(dataString);
//                    Toast.makeText(getActivity(), "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                }
                if(musicChoosed!=null){
                    mRef.child("songUrl").setValue(musicChoosed);
                }
                if(filepath!=null) {
                    Uri file = Uri.fromFile(new File(filepath));
                    UploadTask uploadTask = storageReference.child(LoginFragment.login_eventID + "/video").putFile(file);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            Log.i("lala", "onProgress: " + progress);
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child(LoginFragment.login_eventID + "/video").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    mRef.child("videoUrl").setValue(downloadUrl.toString());
                                    new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Well done!")
                                            .setContentText("Your version is updated")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            });
                        }
                    });
                }else{
                    new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Well done!")
                            .setContentText("Your version is updated")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    kAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_VIDEO_STORAGE && resultCode == RESULT_OK){

            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filepath = cursor.getString(columnIndex);

            cursor.close();

            videoOk.setImageResource(R.drawable.tick);

        }
    }

    private void galeriapermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_VIDEO_STORAGE);
        }
    }

    public void loadMusic (String songUrl){
        try {
            Uri uri = Uri.parse(songUrl);
            if (MainActivity.mediaPlayer != null) {
                MainActivity.mediaPlayer.release();
            }
            MainActivity.mediaPlayer = new MediaPlayer();
            MainActivity.mediaPlayer.setDataSource(getActivity(), uri);
            MainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            MainActivity.mediaPlayer.setLooping(true);
            MainActivity.mediaPlayer.prepare();
            ((MainActivity) getActivity()).startmusic();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}