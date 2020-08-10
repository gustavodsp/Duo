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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.project_duo.Others.FileInfoPadrinhos;
import com.example.project_duo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTab2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTab2Fragment extends Fragment {

    RecyclerView rv_padrinhos;
    LinearLayoutManager mLayoutManager;
    TextView add;

    TextView add_photo, edit_photo;
    ImageView add_preview, edit_preview;
    EditText add_msg, edit_msg;
    Button add_enviar, edit_enviar;

    String filepath;
    Matrix matrix;

    int changed;

    FirebaseRecyclerAdapter<FileInfoPadrinhos,PadrinhosViewHolder> adapter;

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("padrinhos");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static final int REQUEST_IMAGE_CAPTURE_ADD = 1;
    public static final int REQUEST_IMAGE_CAPTURE_EDIT = 2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditTab2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTab2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTab2Fragment newInstance(String param1, String param2) {
        EditTab2Fragment fragment = new EditTab2Fragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_tab2, container, false);

        add = (TextView) view.findViewById(R.id.textView106);
        rv_padrinhos = (RecyclerView) view.findViewById(R.id.rv_padrinhos);
        rv_padrinhos.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_padrinhos.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<FileInfoPadrinhos> options = new FirebaseRecyclerOptions.Builder<FileInfoPadrinhos>()
                .setQuery(mRef,FileInfoPadrinhos.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<FileInfoPadrinhos, PadrinhosViewHolder>(options) {

            @NonNull
            @Override
            public PadrinhosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.padrinhos_item, parent, false);
                return new PadrinhosViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final PadrinhosViewHolder viewHolder, final int position, final FileInfoPadrinhos model) {

                viewHolder.pFoto.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int width = viewHolder.pFoto.getMeasuredWidth();
                        int height = viewHolder.pFoto.getMeasuredWidth();

                        viewHolder.pFoto.setLayoutParams(new LinearLayout.LayoutParams(width, height));

                        viewHolder.pFoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });

                viewHolder.pTexto.setText(model.getTexto());
                Uri uri = Uri.parse(model.getFoto());
                Glide.with(getActivity()).load(uri).centerCrop().into(viewHolder.pFoto);

                viewHolder.pDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new KAlertDialog(getActivity(), KAlertDialog.WARNING_TYPE)
                                .setTitleText("Attention...")
                                .setContentText("Are you sure do you want to delete it?")
                                .setConfirmText("Confirm")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        adapter.getRef(position).removeValue();
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .setCancelText("Cancel")
                                .show();
                    }
                });

                viewHolder.pEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                                .setContentHolder(new ViewHolder(R.layout.dialog_padrinhos))
                                .setContentBackgroundResource(android.R.color.white)
                                .setGravity(Gravity.CENTER)
                                .create();

                        changed = 0;

                        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_padrinhos);
                        edit_photo = (TextView) dialog.findViewById(R.id.textView111);
                        edit_preview = (ImageView) dialog.findViewById(R.id.imageView32);
                        edit_msg = (EditText) dialog.findViewById(R.id.editText27);
                        edit_enviar = (Button) dialog.findViewById(R.id.button19);

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;
                        linearLayout.setMinimumHeight(height);
                        linearLayout.setMinimumWidth(width);

                        edit_msg.setText(model.getTexto());
                        Uri uri = Uri.parse(model.getFoto());
                        Glide.with(getActivity()).load(uri).centerCrop().into(edit_preview);
                        edit_photo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                galeriapermission();
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select an image"), REQUEST_IMAGE_CAPTURE_EDIT);
                            }
                        });
                        edit_enviar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter.getRef(position).child("texto").setValue(edit_msg.getText().toString());
//                                viewHolder.pTexto.setText(d_msg.getText().toString());
                                if (changed == 1){
                                    Uri file = Uri.fromFile(compressedFoto(new File(getFilepath())));
                                    String uniqueID = UUID.randomUUID().toString();
                                    UploadTask uploadTask = storageReference.child(LoginFragment.login_eventID+"/padrinhosPhotos/"+uniqueID).putFile(file);
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
                                            storageReference.child(LoginFragment.login_eventID+"/padrinhosPhotos/"+uniqueID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri downloadUrl) {
                                                    adapter.getRef(position).child("foto").setValue(downloadUrl.toString());
                                                    Toast.makeText(getActivity(), "Data updated successfully!", Toast.LENGTH_SHORT).show();
//                                                    Glide.with(getActivity()).load(downloadUrl).centerCrop().into(viewHolder.pFoto);
                                                    dialog.dismiss();
                                                }
                                            });

                                        }
                                    });
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.show();
                    }
                });

            }
        };

        rv_padrinhos.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rv_padrinhos);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_padrinhos))
                        .setContentBackgroundResource(android.R.color.white)
                        .setGravity(Gravity.CENTER)
                        .create();

                changed = 0;

                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.layout_padrinhos);
                add_photo = (TextView) dialog.findViewById(R.id.textView111);
                add_preview = (ImageView) dialog.findViewById(R.id.imageView32);
                add_msg = (EditText) dialog.findViewById(R.id.editText27);
                add_enviar = (Button) dialog.findViewById(R.id.button19);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                linearLayout.setMinimumHeight(height);
                linearLayout.setMinimumWidth(width);

                add_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        galeriapermission();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select an image"), REQUEST_IMAGE_CAPTURE_ADD);
                    }
                });
                add_enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if ((changed == 1) && !(add_msg.getText().toString().equals(""))){

                            final FileInfoPadrinhos fileInfoPadrinhos = new FileInfoPadrinhos();
                            fileInfoPadrinhos.setTexto(add_msg.getText().toString());

                            Uri file = Uri.fromFile(compressedFoto(new File(getFilepath())));
                            String uniqueID = UUID.randomUUID().toString();
                            UploadTask uploadTask = storageReference.child(LoginFragment.login_eventID+"/padrinhosPhotos/"+uniqueID).putFile(file);
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
                                    storageReference.child(LoginFragment.login_eventID+"/padrinhosPhotos/"+uniqueID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUrl) {
                                            fileInfoPadrinhos.setFoto(downloadUrl.toString());
                                            mRef.push().setValue(fileInfoPadrinhos);
                                            Toast.makeText(getActivity(), "Added successfully!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });

                                }
                            });
                        }else{
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Oops...")
                                    .setMessage("Please, fill all the information")
                                    .setPositiveButton("Ok",null)
                                    .show();
                        }
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    public static class PadrinhosViewHolder extends RecyclerView.ViewHolder {

        ImageView pFoto;
        TextView pTexto,pDel;
        Button pEdit;

        public PadrinhosViewHolder(View itemView) {
            super(itemView);

            pDel = (TextView) itemView.findViewById(R.id.textView108);
            pTexto = (TextView) itemView.findViewById(R.id.textView109);
            pFoto = (ImageView) itemView.findViewById(R.id.imageView31);
            pEdit = (Button) itemView.findViewById(R.id.button17);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
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
            if(requestCode==REQUEST_IMAGE_CAPTURE_ADD){
                add_preview.setImageBitmap(bitmap);}
            if(requestCode==REQUEST_IMAGE_CAPTURE_EDIT){
                edit_preview.setImageBitmap(bitmap);}
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE_ADD);
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