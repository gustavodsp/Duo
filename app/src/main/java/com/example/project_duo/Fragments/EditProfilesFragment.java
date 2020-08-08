package com.example.project_duo.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.project_duo.Others.RoundedImageView;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfilesFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("story");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    TextView noivaName, noivoName, noivaT, noivoT;
    ImageButton editNoiva, editNoivo;
    RoundedImageView photoNoiva, photoNoivo;

    TextView d_titulo, mark1, mark2;
    ImageButton d_edit;
    EditText d_nome;
    Button d_enviar;
    ImageView d_foto;

    String filepath;
    Matrix matrix;

    int changed;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfilesFragment newInstance(String param1, String param2) {
        EditProfilesFragment fragment = new EditProfilesFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profiles, container, false);

        noivaT = (TextView) view.findViewById(R.id.textView94);
        noivoT = (TextView) view.findViewById(R.id.textView95);
        noivaName = (TextView) view.findViewById(R.id.textView96);
        noivoName = (TextView) view.findViewById(R.id.textView97);
        editNoiva = (ImageButton) view.findViewById(R.id.imageButton6);
        editNoivo = (ImageButton) view.findViewById(R.id.imageButton7);
        photoNoiva = (RoundedImageView) view.findViewById(R.id.imageView25);
        photoNoivo = (RoundedImageView) view.findViewById(R.id.imageView26);
        mark1 = (TextView) view.findViewById(R.id.circle1n);
        mark2 = (TextView) view.findViewById(R.id.circle2n);

        mark1.getBackground().setColorFilter(ContextCompat.getColor(getActivity(),R.color.dark_gray), PorterDuff.Mode.SRC_IN);
        mark2.getBackground().setColorFilter(ContextCompat.getColor(getActivity(),R.color.mb_gray), PorterDuff.Mode.SRC_IN);

        final Typeface tf_roman = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Roman.otf");
        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");

        noivaT.setTypeface(tf_heavy);
        noivoT.setTypeface(tf_heavy);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("noivo").hasChild("nome")){
                    noivoName.setText(dataSnapshot.child("noivo").child("nome").getValue(String.class));
                }
                if(dataSnapshot.child("noivo").hasChild("foto")){
                    String url = dataSnapshot.child("noivo").child("foto").getValue(String.class);
                    Uri uri = Uri.parse(url);
                    Glide.with(getActivity()).load(uri).centerCrop().into(photoNoivo);
                }

                if(dataSnapshot.child("noiva").hasChild("nome")){
                    noivaName.setText(dataSnapshot.child("noiva").child("nome").getValue(String.class));
                }
                if(dataSnapshot.child("noiva").hasChild("foto")){
                    String url = dataSnapshot.child("noiva").child("foto").getValue(String.class);
                    Uri uri = Uri.parse(url);
                    Glide.with(getActivity()).load(uri).centerCrop().into(photoNoiva);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editNoivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_profiles))
                        .setContentBackgroundResource(android.R.color.white)
                        .setGravity(Gravity.CENTER)
                        .create();

                changed = 0;

                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_noivos);
                d_titulo = (TextView) dialog.findViewById(R.id.textView99);
                d_edit = (ImageButton) dialog.findViewById(R.id.imageButton9);
                d_nome = (EditText) dialog.findViewById(R.id.textView101);
                d_foto = (ImageView) dialog.findViewById(R.id.imageView28);
                d_enviar = (Button) dialog.findViewById(R.id.button13);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                linearLayout.setMinimumHeight(height);
                linearLayout.setMinimumWidth(width);

                d_titulo.setText("Noivo");
                d_titulo.setTypeface(tf_heavy);
                d_nome.setText(noivoName.getText());
                d_nome.setTypeface(tf_roman);
                d_foto.setImageDrawable(photoNoivo.getDrawable());

                d_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        galeriapermission();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), REQUEST_IMAGE_CAPTURE);
                    }
                });

                d_enviar.setTypeface(tf_heavy);
                d_enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (d_foto.getDrawable() != null) {
                            if (changed == 1) {
                                if(!d_nome.getText().toString().equals("")){
                                    mRef.child("noivo").child("nome").setValue(d_nome.getText().toString());
                                    noivoName.setText(d_nome.getText().toString());
                                    Uri file = Uri.fromFile(compressedFoto(new File(getFilepath())));
                                    UploadTask uploadTask = storageReference.child(LoginFragment.login_eventID + "/casalPhotos/noivo").putFile(file);
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
                                            storageReference.child(LoginFragment.login_eventID + "/casalPhotos/noivo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri downloadUrl) {
                                                    mRef.child("noivo").child("foto").setValue(downloadUrl.toString());
                                                    Glide.with(getActivity()).load(downloadUrl).centerCrop().into(photoNoivo);
                                                    new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Well done!")
                                                            .setContentText("Your version is updated")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog kAlertDialog) {
                                                                    kAlertDialog.dismiss();
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                }
                                            });
                                        }
                                    });
                                } else{
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Oops...")
                                            .setMessage("Don't forget to pick a profile picture and write a name!")
                                            .setPositiveButton("OK",null)
                                            .show();
                                }
                            } else{
                                if(!d_nome.getText().toString().equals("")){
                                    mRef.child("noiva").child("nome").setValue(d_nome.getText().toString());
                                    noivaName.setText(d_nome.getText().toString());
                                    new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Well done!")
                                            .setContentText("Your version is updated")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else{
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Oops...")
                                            .setMessage("Don't forget to pick a profile picture and write a name!")
                                            .setPositiveButton("OK",null)
                                            .show();
                                }
                            }
                        } else{
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Oops...")
                                    .setMessage("Don't forget to pick a profile picture and write a name!")
                                    .setPositiveButton("OK",null)
                                    .show();
                        }
                    }
                });

                dialog.show();

            }
        });

        editNoiva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_profiles))
                        .setContentBackgroundResource(android.R.color.white)
                        .setGravity(Gravity.CENTER)
                        .create();

                changed = 0;

                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_noivos);
                d_titulo = (TextView) dialog.findViewById(R.id.textView99);
                d_edit = (ImageButton) dialog.findViewById(R.id.imageButton9);
                d_nome = (EditText) dialog.findViewById(R.id.textView101);
                d_foto = (ImageView) dialog.findViewById(R.id.imageView28);
                d_enviar = (Button) dialog.findViewById(R.id.button13);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                linearLayout.setMinimumHeight(height);
                linearLayout.setMinimumWidth(width);
                d_titulo.setText("Noiva");
                d_titulo.setTypeface(tf_heavy);
                d_nome.setText(noivaName.getText());
                d_nome.setTypeface(tf_roman);
                d_foto.setImageDrawable(photoNoiva.getDrawable());

                d_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        galeriapermission();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), REQUEST_IMAGE_CAPTURE);
                    }
                });

                d_enviar.setTypeface(tf_heavy);
                d_enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (d_foto.getDrawable() != null) {
                            if (changed == 1) {
                                if(!d_nome.getText().toString().equals("")){
                                    mRef.child("noiva").child("nome").setValue(d_nome.getText().toString());
                                    noivaName.setText(d_nome.getText().toString());
                                    Uri file = Uri.fromFile(compressedFoto(new File(getFilepath())));
                                    UploadTask uploadTask = storageReference.child(LoginFragment.login_eventID + "/casalPhotos/noiva").putFile(file);
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
                                            storageReference.child(LoginFragment.login_eventID + "/casalPhotos/noiva").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri downloadUrl) {
                                                    mRef.child("noiva").child("foto").setValue(downloadUrl.toString());
                                                    Glide.with(getActivity()).load(downloadUrl).centerCrop().into(photoNoivo);
                                                    new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Well done!")
                                                            .setContentText("Your version is updated")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog kAlertDialog) {
                                                                    kAlertDialog.dismiss();
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                }
                                            });
                                        }
                                    });
                                } else{
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Oops...")
                                            .setMessage("Don't forget to pick a profile picture and write a name!")
                                            .setPositiveButton("OK",null)
                                            .show();
                                }
                            } else{
                                if(!d_nome.getText().toString().equals("")){
                                    mRef.child("noiva").child("nome").setValue(d_nome.getText().toString());
                                    noivaName.setText(d_nome.getText().toString());
                                    new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Well done!")
                                            .setContentText("Your version is updated")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else{
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Oops...")
                                            .setMessage("Don't forget to pick a profile picture and write a name!")
                                            .setPositiveButton("OK",null)
                                            .show();
                                }
                            }
                        } else{
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Oops...")
                                    .setMessage("Don't forget to pick a profile picture and write a name!")
                                    .setPositiveButton("OK",null)
                                    .show();
                        }
                    }
                });

                dialog.show();

            }
        });

        return view;
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

            Bitmap bitmap = BitmapFactory.decodeFile(getFilepath());

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

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), getMatrix(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }

            d_foto.setImageBitmap(bitmap);
            changed = 1;

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

    private void galeriapermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
        }
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
}