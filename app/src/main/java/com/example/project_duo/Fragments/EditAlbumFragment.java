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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_duo.Others.CustomImageView;
import com.example.project_duo.Others.FileInfoAlbum;
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
 * Use the {@link EditAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAlbumFragment extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID).child("slideshow");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    TextView add, del;
    LinearLayout hint;

    ImageButton d_gallery;
    ImageView d_preview;
    LinearLayout d_line;
    Button d_send;

    Boolean showX=false, vazio=true;
    String filepath;
    Matrix matrix;

    FirebaseRecyclerAdapter<FileInfoAlbum,AlbumViewHolder> albumViewHolderFirebaseRecyclerAdapter;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAlbumFragment newInstance(String param1, String param2) {
        EditAlbumFragment fragment = new EditAlbumFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_album, container, false);

        final Typeface tf_heavy = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Heavy.otf");

        add = (TextView) view.findViewById(R.id.textView102);
        del = (TextView) view.findViewById(R.id.textView103);
        hint = (LinearLayout) view.findViewById(R.id.layout_hint);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_album);
        recyclerView.setHasFixedSize(true);

        add.setTypeface(tf_heavy);
        del.setTypeface(tf_heavy);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseRecyclerOptions<FileInfoAlbum> options = new FirebaseRecyclerOptions.Builder<FileInfoAlbum>()
                .setQuery(mRef,FileInfoAlbum.class)
                .build();

        albumViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileInfoAlbum, AlbumViewHolder>(options) {
            @NonNull
            @Override
            public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
                return new AlbumViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(final AlbumViewHolder viewHolder, final int position, final FileInfoAlbum model) {

                viewHolder.imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;
                        float ratio = ((float)width)/height;

                        int measuredWidth = viewHolder.imageView.getMeasuredWidth();
                        int novoHeight = Math.round(measuredWidth/ratio);
                        viewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(measuredWidth,novoHeight));

                        viewHolder.imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });

                if(model.getFoto()!=null){
                    Uri uri = Uri.parse(model.getFoto());
                    Glide.with(getActivity()).load(uri).fitCenter().into(viewHolder.imageView);
                    vazio=false;
                }
                if(vazio){
                    hint.setVisibility(View.VISIBLE);
                    Log.i("lala", "vazio: ");
                }else{
                    hint.setVisibility(View.INVISIBLE);
                    Log.i("lala", "cheio: ");
                }

                if(showX){
                    viewHolder.delete.setVisibility(View.VISIBLE);
                } else{
                    viewHolder.delete.setVisibility(View.INVISIBLE);
                }

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(albumViewHolderFirebaseRecyclerAdapter.getItemCount()==1){
                            Log.i("lala", "esvaziou");
                            vazio=true;
                        }
                        albumViewHolderFirebaseRecyclerAdapter.getRef(position).removeValue();
                        Toast.makeText(getActivity(), "Photo deleted successfully", Toast.LENGTH_SHORT).show();
                        showX=false;
                        albumViewHolderFirebaseRecyclerAdapter.notifyDataSetChanged();
                    }
                });

            }
        };

        recyclerView.setAdapter(albumViewHolderFirebaseRecyclerAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_capture_album))
                        .setContentBackgroundResource(android.R.color.white)
                        .setGravity(Gravity.CENTER)
                        .create();

                d_gallery = (ImageButton) dialog.findViewById(R.id.imageButton8);
                d_preview = (ImageView) dialog.findViewById(R.id.imageView29);
                d_line = (LinearLayout) dialog.findViewById(R.id.white_bg);
                d_send = (Button) dialog.findViewById(R.id.button18);

                d_send.setTypeface(tf_heavy);

                d_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        galeriapermission();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select an picture"), REQUEST_IMAGE_CAPTURE);
                    }
                });

                d_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri file = Uri.fromFile(compressedFoto(new File(getFilepath())));
                        String uniqueID = UUID.randomUUID().toString();
                        UploadTask uploadTask = storageReference.child(LoginFragment.login_eventID+"/albumPhotos/"+uniqueID).putFile(file);
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
                                storageReference.child(LoginFragment.login_eventID+"/albumPhotos/"+uniqueID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUrl) {
                                        mRef.push().child("foto").setValue(downloadUrl.toString());
                                        Toast.makeText(getActivity(), "Photo added successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                                vazio=false;
                            }
                        });
                    }
                });

                dialog.show();

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showX=!showX;
                albumViewHolderFirebaseRecyclerAdapter.notifyDataSetChanged();

            }
        });

        return view;
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {

        CustomImageView imageView;
        ImageButton delete;

        public AlbumViewHolder(View itemView) {
            super(itemView);

            imageView = (CustomImageView) itemView.findViewById(R.id.iv_albumitem);
            delete = (ImageButton) itemView.findViewById(R.id.imageButton18);

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

            d_preview.setImageBitmap(bitmap);
            d_line.setVisibility(View.VISIBLE);

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

    private void galeriapermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        albumViewHolderFirebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        albumViewHolderFirebaseRecyclerAdapter.stopListening();
    }

}