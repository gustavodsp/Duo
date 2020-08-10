package com.example.project_duo.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_duo.MainActivity;
import com.example.project_duo.Others.CustomImageView;
import com.example.project_duo.Others.FileInfoPhoto;
import com.example.project_duo.Others.RoundedImageView;
import com.example.project_duo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {

    public static final int REQUEST_EXTERNAL_STORAGE = 0;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference().child("Codes").child(HomeFragment.codigo).child("fotos");

    ImageButton imageButton;

    FirebaseRecyclerAdapter<FileInfoPhoto,PhotoViewHolder> adapter;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
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
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        galeriapermission();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_rv2);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<FileInfoPhoto> options = new FirebaseRecyclerOptions.Builder<FileInfoPhoto>()
                .setQuery(mRef,FileInfoPhoto.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<FileInfoPhoto, PhotoViewHolder>(options) {
            @NonNull
            @Override
            public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
                return new PhotoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final PhotoViewHolder viewHolder, final int position, final FileInfoPhoto model) {

                viewHolder.imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int width = viewHolder.imageView.getMeasuredWidth();
                        int height = width*3/4;

                        viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

                        viewHolder.imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });

                viewHolder.nome.setText(model.getName());

                final String url = model.getProfilePhoto();
                if(url!=null) {
                    Uri uri = Uri.parse(url);
                    Glide.with(getActivity()).load(uri).centerCrop().into(viewHolder.profile);
                }else{
                    viewHolder.profile.setImageResource(R.drawable.hint);
                }

                if(model.getCaption()!=null) {

                    viewHolder.legenda.setVisibility(View.VISIBLE);
                    viewHolder.legenda.setText(model.getCaption());
//                    viewHolder.legenda.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            view.getParent().requestDisallowInterceptTouchEvent(true);
//                            return false;
//                        }
//                    });
//                    viewHolder.legenda.setMovementMethod(new ScrollingMovementMethod());

                } else{
                    viewHolder.legenda.setVisibility(View.INVISIBLE);
                }

                final String url2 = model.getImageUrl();
                Uri uri2 = Uri.parse(url2);
                Glide.with(getActivity()).load(uri2).centerCrop().into(viewHolder.imageView);



                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        zoomImageFromThumb(viewHolder.imageView, url2);

                    }
                });

                viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final EditText input = new EditText(getActivity());

                        final AlertDialog aDialog = new AlertDialog.Builder(getActivity()).create();
                        aDialog.setTitle("Área Reservada");
                        aDialog.setMessage("Área destinada aos noivos. Se deseja remover a foto, digite a senha:");
                        aDialog.setView(input);
                        aDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(input.getText().toString().equals("Remove")){

                                    adapter.getRef(position).removeValue();
                                    Toast.makeText(getActivity(), "Foto deletada com sucesso", Toast.LENGTH_SHORT).show();

                                }
                                else{

                                    Toast.makeText(getActivity(), "Senha incorreta", Toast.LENGTH_SHORT).show();
                                    aDialog.dismiss();

                                }

                            }
                        });
                        aDialog.show();

                        return true;
                    }
                });

                viewHolder.salvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadFile(model.getImageUrl());
                    }
                });

            }
        };

        mRecyclerView.setAdapter(adapter);

        imageButton = (ImageButton) view.findViewById(R.id.imb_newpic);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadCapture();
            }
        });

        return view;
    }

    private void galeriapermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void downloadFile(String uRl) {
//        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Duo");
//
//        if (!direct.exists()) {
//            direct.mkdirs();
//        }

        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Duo")
                .setDescription("Download from Duo App.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "duoImage.jpg");

        mgr.enqueue(request);

        Toast.makeText(getActivity(),"Photo saved successfully!", Toast.LENGTH_SHORT).show();

    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        CustomImageView imageView;
        TextView legenda, nome;
        RoundedImageView profile;
        ImageButton salvar;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            profile = (RoundedImageView) itemView.findViewById(R.id.imv_photoprofile);
            nome = (TextView) itemView.findViewById(R.id.txv_photoname);
            imageView = (CustomImageView) itemView.findViewById(R.id.imv_picture);
            legenda = (TextView) itemView.findViewById(R.id.txv_photocaption);
            salvar = (ImageButton) itemView.findViewById(R.id.imb_photosave);

        }
    }

    private void zoomImageFromThumb(final View thumbView, String imageUrl) {

        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) getView().findViewById(
                R.id.expanded_image);

        Uri uri = Uri.parse(imageUrl);
        Glide.with(getActivity()).load(uri).into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        getView().findViewById(R.id.container_rv)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
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