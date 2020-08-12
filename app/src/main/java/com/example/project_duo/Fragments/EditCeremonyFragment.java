package com.example.project_duo.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCeremonyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCeremonyFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("evento");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    EditText nomelocal, endereco, coord1, coord2;
    TextView add;
    ImageView localpic;
    Button atualizar;

    String filepath;
    Matrix matrix;

    int changed=0;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditCeremonyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCeremonyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCeremonyFragment newInstance(String param1, String param2) {
        EditCeremonyFragment fragment = new EditCeremonyFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_ceremony, container, false);

        add = (TextView) view.findViewById(R.id.textView114);
        nomelocal = (EditText) view.findViewById(R.id.editText28);
        endereco = (EditText) view.findViewById(R.id.editText29);
        coord1 = (EditText) view.findViewById(R.id.editText30);
        coord2 = (EditText) view.findViewById(R.id.editText31);
        localpic = (ImageView) view.findViewById(R.id.imageView33);
        atualizar = (Button) view.findViewById(R.id.button20);

        localpic.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = localpic.getMeasuredWidth();
                int height = width/2;

                localpic.setLayoutParams(new LinearLayout.LayoutParams(width,height));

                localpic.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("locationAddress")){
                    endereco.setText(dataSnapshot.child("locationAddress").getValue(String.class));
                }
                if(dataSnapshot.hasChild("locationName")){
                    nomelocal.setText(dataSnapshot.child("locationName").getValue(String.class));
                }
                if(dataSnapshot.hasChild("locationPhoto")){
                    String url = dataSnapshot.child("locationPhoto").getValue(String.class);
                    Uri uri = Uri.parse(url);
                    Glide.with(getActivity()).load(uri).centerCrop().fitCenter().into(localpic);
                }
                if(dataSnapshot.hasChild("coordLat")){
                    coord1.setText(String.valueOf(dataSnapshot.child("coordLat").getValue(Double.class)));
                }
                if(dataSnapshot.hasChild("coordLong")){
                    coord2.setText(String.valueOf(dataSnapshot.child("coordLong").getValue(Double.class)));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeriapermission();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an image"), REQUEST_IMAGE_CAPTURE);
            }
        });

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!endereco.getText().toString().equals("")) {
                    mRef.child("locationAddress").setValue(endereco.getText().toString());
                }
                if(!nomelocal.getText().toString().equals("")) {
                    mRef.child("locationName").setValue(nomelocal.getText().toString());
                }
                if(!coord1.getText().toString().equals("") && !coord2.getText().toString().equals("")) {
                    mRef.child("coordLat").setValue(Double.parseDouble(coord1.getText().toString()));
                    mRef.child("coordLong").setValue(Double.parseDouble(coord2.getText().toString()));
                }
                if(changed==1){

                    Uri file = Uri.fromFile(compressedFoto(new File(getFilepath())));
                    UploadTask uploadTask = storageReference.child(LoginFragment.login_eventID+"/locationPhotos/local").putFile(file);
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
                            storageReference.child(LoginFragment.login_eventID+"/locationPhotos/local").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    mRef.child("locationPhoto").setValue(downloadUrl.toString());
                                    Glide.with(getActivity()).load(downloadUrl).centerCrop().into(localpic);
                                    changed=0;
                                }
                            });
                            new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Well done!")
                                    .setContentText("Version updated")
                                    .setConfirmText("Ok")
                                    .show();
                        }
                    });
                }else{
                    new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Well done!")
                            .setContentText("Version updated")
                            .setConfirmText("Ok")
                            .show();
                }


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

            localpic.setImageBitmap(bitmap);
            changed=1;

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

}