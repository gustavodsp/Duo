package com.example.project_duo.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.project_duo.MainActivity;
import com.example.project_duo.Others.AuxiliarRVClickListener;
import com.example.project_duo.Others.FAQListAdapter;
import com.example.project_duo.Others.FileInfoMenu;
import com.example.project_duo.Others.MenuListAdapter;
import com.example.project_duo.Others.OptionsAdapter;
import com.example.project_duo.Others.RoundedImageView;
import com.example.project_duo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AuxiliarRVClickListener {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    String filepath;
    Matrix matrix;

    public static String codigo;
    private FirebaseAuth mAuth;

    RecyclerView mRv;
    List<FileInfoMenu> celulas_menu;

    Button btn_codigo;
    EditText code;
    TextView nome, email, demo, sign;
    LinearLayout menu;
    RoundedImageView photo;
    ImageButton editPhoto;
    RelativeLayout diagonalLayout;

    int auxiliar=0;

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes");
    DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Users");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        mRv = (RecyclerView) view.findViewById(R.id.rv_menu);
        menu = (LinearLayout) view.findViewById(R.id.linear_menu);
        nome = (TextView) view.findViewById(R.id.txv_name);
        email = (TextView) view.findViewById(R.id.txv_email);
        photo = (RoundedImageView) view.findViewById(R.id.photo_login);
        editPhoto = (ImageButton) view.findViewById(R.id.imb_edit);
        sign = (TextView) view.findViewById(R.id.txv_signature);
        diagonalLayout = (RelativeLayout) view.findViewById(R.id.diag_layout);

        diagonalLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = diagonalLayout.getMeasuredHeight();
                LayerDrawable layers = (LayerDrawable) diagonalLayout.getBackground();
                layers.setLayerInset(1,0,height,(height*-1),(height*-1));
                diagonalLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");

        nome.setText(LoginFragment.login_nome);
        email.setText(LoginFragment.login_email);

        sign.setTypeface(tf_heavy);

        if(LoginFragment.login_photo!=null) {
            Uri uri = Uri.parse(LoginFragment.login_photo);
            Glide.with(getActivity()).load(uri).into(photo);
        }else {
            photo.setImageResource(R.drawable.hint);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm.scrollToPositionWithOffset(1,100);
        mRv.setLayoutManager(llm);

//        String[] titulos = new String[]{"Produto","Acesso","Portal"};
//        String[] descricoes = new String[]{
//                "Clique aqui para adquirir uma versão Due para o seu casamento",
//                "Clique aqui para acessar um evento",
//                "Clique aqui para editar sua versão Due" };
        int[] icones = new int[]{R.drawable.productcell,R.drawable.eventcell,R.drawable.portalcell};
        celulas_menu = new ArrayList<>();
        for(int i=0;i<3;i++){
            FileInfoMenu celula = new FileInfoMenu(icones[i]);
            celulas_menu.add(celula);
        }
        OptionsAdapter adapter = new OptionsAdapter(getActivity(),celulas_menu);
        adapter.setAuxiliarRVClickListener(this);
        mRv.setAdapter(adapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("dateEnd")){
                    String dateString = dataSnapshot.child("dateEnd").getValue(String.class);
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                    Date today=null;
                    Date dateEnd=null;

                    try{
                        dateEnd = df.parse(dateString);
                        today = Calendar.getInstance().getTime();
                    } catch (ParseException e){
                        e.printStackTrace();
                    }

                    if(dateEnd.after(today)){
                        dataSnapshot.getRef().removeValue();
                        String dono = dataSnapshot.child("user").getValue(String.class);
                        delRef.child(dono).child("eventName").removeValue();
                        delRef.child(dono).child("firstEvent").removeValue();
                        delRef.child(dono).child("hasDue").removeValue();
                        delRef.child(dono).child("nameSet").removeValue();
                    }
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

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeriapermission();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select a new photo"), REQUEST_IMAGE_CAPTURE);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final MenuListAdapter adapter = new MenuListAdapter(getActivity(),R.array.menu);

                DialogPlus.newDialog(getActivity())
                        .setAdapter(adapter)
                        .setGravity(Gravity.CENTER)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                                MainActivity mainactivity = (MainActivity) getActivity();

                                switch (position){
                                    case 0:
                                        dialog.dismiss();
                                        final FAQListAdapter adapter2 = new FAQListAdapter(getActivity(),R.array.perguntas,R.array.respostas);

                                        DialogPlus.newDialog(getActivity())
                                                .setAdapter(adapter2)
                                                .setGravity(Gravity.CENTER)
                                                .create()
                                                .show();

                                        break;
                                    case 1:

                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                                "mailto","gust.eng92@gmail.com", null));
                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                        startActivity(Intent.createChooser(emailIntent, "Entrar em contato"));

                                        break;
                                    case 2:

                                        mAuth.signOut();
//                                        LoginManager.getInstance().logOut();
                                        dialog.dismiss();
                                        mainactivity.loadLogin();

                                        break;
                                }
                            }
                        })
                        .create()
                        .show();

            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        MainActivity mainactivity = (MainActivity) getActivity();
        mainactivity.startmusic();
        if(MainActivity.mediaPlayer!=null) {
            MainActivity.mediaPlayer.setVolume(0, 0);
        }

    }

    private void galeriapermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
        }
    }

    public String getFilepath(){
        return filepath;
    }
    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    public Matrix getMatrix(){
        return matrix;
    }
    public void setMatrix(Matrix matrix){
        this.matrix = matrix;
    }

    public File compressedFoto (File file){

        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            selectedBitmap = Bitmap.createBitmap(selectedBitmap,0,0,selectedBitmap.getWidth(),selectedBitmap.getHeight(),getMatrix(),true);

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;

        } catch (Exception e) {
            return null;
        }
    }

    public void loadMusic (Uri newUri){
        try {
            MainActivity.mediaPlayer = new MediaPlayer();
            MainActivity.mediaPlayer.setDataSource(getActivity(), newUri);
            MainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            MainActivity.mediaPlayer.setLooping(true);
            MainActivity.mediaPlayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            setFilepath(cursor.getString(columnIndex));

            cursor.close();

            try {
                ExifInterface exif = new ExifInterface(getFilepath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                setMatrix(new Matrix());
                if (orientation == 6) {
                    getMatrix().postRotate(90);
                } else if (orientation == 3) {
                    getMatrix().postRotate(180);
                } else if (orientation == 8) {
                    getMatrix().postRotate(270);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri file = Uri.fromFile(compressedFoto(new File(getFilepath())));
            UploadTask uploadTask = storageReference.child("ProfilePhotos/"+LoginFragment.login_email).putFile(file);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Log.i("lala", "onProgress: "+progress);
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.child("ProfilePhotos/"+LoginFragment.login_email).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            delRef.child(LoginFragment.login_user).child("photo").setValue(downloadUrl.toString());
                            Glide.with(getActivity()).load(downloadUrl).centerCrop().into(photo);
                            LoginFragment.login_photo = downloadUrl.toString();
                            Toast.makeText(getActivity(), "Foto atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }
    }

    @Override
    public void onClickListener(View view, int position) {
        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");
        switch (position){
            case 0:

                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(LoginFragment.login_user).hasChild("hasDue")){
                            new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("You already have an active Duo Version. To access and edit it, click on Editor.")
                                    .setConfirmText("Ok")
                                    .show();
                        }else{
                            MainActivity mainactivity = (MainActivity) getActivity();
                            mainactivity.loadPromotion();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case 1:

                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_codigo))
                        .setGravity(Gravity.CENTER)
                        .create();

                code = (EditText) dialog.findViewById(R.id.edt_code);
                btn_codigo = (Button) dialog.findViewById(R.id.btn_access);
                btn_codigo.setTypeface(tf_heavy);
                btn_codigo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(code.getText().toString().equals("")){
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Oops...")
                                    .setMessage("Before you continue, type the name of the version you want to access.")
                                    .setPositiveButton("Ok",null)
                                    .show();
                        } else{
                            auxiliar=0;
                            mRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    if(dataSnapshot.child("eventName").getValue(String.class).equals(code.getText().toString())){
                                        auxiliar=1;
                                        codigo = dataSnapshot.getKey();
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
                                    if(auxiliar==0){
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Hmm...")
                                                .setMessage("It looks like it doesn't exist a version with this name.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        code.setText(null);
                                                    }
                                                })
                                                .show();
                                    } else{
                                        if(dataSnapshot.child(codigo).hasChild("dateStart")){
                                            Uri uri = Uri.parse(dataSnapshot.child(codigo).child("video").child("songUrl").getValue(String.class));
                                            loadMusic(uri);
                                            MainActivity mainactivity = (MainActivity) getActivity();
                                            mainactivity.loadTabBar();
                                            dialog.dismiss();
                                        } else{

                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Hmm...")
                                                    .setMessage("The version you are trying to access is not online yet. Wait until the engaged couple set up their version.")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            code.setText(null);
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }
                });

                demo = (TextView) dialog.findViewById(R.id.txv_demo);
                demo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        codigo = "sample";
                        mRef.child(codigo).child("video").child("songUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Uri uri = Uri.parse(dataSnapshot.getValue(String.class));
                                loadMusic(uri);
                                MainActivity mainactivity = (MainActivity) getActivity();
                                mainactivity.loadTabBar();
                                dialog.dismiss();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                dialog.show();

                break;
            case 2:

                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(LoginFragment.login_user).hasChild("hasDue")){
                            MainActivity mainactivity = (MainActivity) getActivity();
                            mainactivity.loadEditor();
                        }else{
                            new KAlertDialog(getActivity(),KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("You haven't gotten your Duo version to create your event yet! Would you like to get it now?")
                                    .setConfirmText("Yes, of course")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            kAlertDialog.dismiss();
                                            MainActivity mainactivity = (MainActivity) getActivity();
                                            mainactivity.loadPromotion();
                                        }
                                    })
                                    .showCancelButton(true)
                                    .setCancelText("No, thanks")
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
        }
    }

}