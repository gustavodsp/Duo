package com.example.project_duo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.developer.kalert.KAlertDialog;
import com.example.project_duo.Fragments.AlbumFragment;
import com.example.project_duo.Fragments.CaptureFragment;
import com.example.project_duo.Fragments.CerimonyFragment;
import com.example.project_duo.Fragments.CheckoutFragment;
import com.example.project_duo.Fragments.EditTab1Fragment;
import com.example.project_duo.Fragments.EditorFragment;
import com.example.project_duo.Fragments.GiftFragment;
import com.example.project_duo.Fragments.HistoryFragment;
import com.example.project_duo.Fragments.LoginFragment;
import com.example.project_duo.Fragments.HomeFragment;
import com.example.project_duo.Fragments.MapsFragment;
import com.example.project_duo.Fragments.MessagesFragment;
import com.example.project_duo.Fragments.PhotosFragment;
import com.example.project_duo.Fragments.PromotionFragment;
import com.example.project_duo.Fragments.Tab1Fragment;
import com.example.project_duo.Fragments.TabBarFragment;
import com.example.project_duo.Fragments.WriteFragment;

public class MainActivity extends FragmentActivity{

    public static MediaPlayer mediaPlayer;
    private int length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawable(new ColorDrawable(0xffffffff));

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.FragmentContainer);

        if (fragment == null) {

            fragment = new LoginFragment();
            manager.beginTransaction().add(R.id.FragmentContainer, fragment, "login").commit();

        }

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            KAlertDialog alertDialog = new KAlertDialog(this);
            alertDialog.setTitleText("Sair");
            alertDialog.setContentText("Você deseja sair do aplicativo?");
            alertDialog.setConfirmText("Sim");
            alertDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog kAlertDialog) {
                    MainActivity.super.onBackPressed();
                }
            });
            alertDialog.setCancelText("Nao");
            alertDialog.show();

        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        pausemusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.FragmentContainer);
        if(f instanceof HomeFragment || f instanceof LoginFragment) {

        }
        else{
            if(Tab1Fragment.speaking == 1) {
                startmusic();
            }
        }
    }

    public void startmusic(){
        if(mediaPlayer!=null){
            if(!mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(length);
                mediaPlayer.start();
            }
        }
    }

    public void pausemusic(){
        if(mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                length = mediaPlayer.getCurrentPosition();
            }
        }
    }

    public void loadLogin() {

        LoginFragment login = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, login).commit();

    }

    public void loadHome() {

        HomeFragment home = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, home).commit();

    }

    public void loadTabBar() {

        TabBarFragment tabbar = new TabBarFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, tabbar).addToBackStack(null).commit();

    }

    public void loadHistory(){

        HistoryFragment history = new HistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, history).addToBackStack(null).commit();

    }

    public void loadAlbum(){

        AlbumFragment album = new AlbumFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, album).addToBackStack(null).commit();

    }

    public void loadCerimony(){

        CerimonyFragment cerimony = new CerimonyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, cerimony).addToBackStack(null).commit();

    }

    public void loadMaps(){

        MapsFragment maps = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, maps).addToBackStack(null).commit();

    }

    public void loadMessages(){

        MessagesFragment messages = new MessagesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, messages).addToBackStack(null).commit();

    }

    public void loadWrite(){

        WriteFragment write = new WriteFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, write).addToBackStack(null).commit();

    }

    public void loadPhotos(){

        PhotosFragment photos = new PhotosFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, photos).addToBackStack(null).commit();

    }

    public void loadCapture(){

        CaptureFragment capture = new CaptureFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, capture).addToBackStack(null).commit();

    }

    public void loadGift(){

        GiftFragment gift = new GiftFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, gift).addToBackStack(null).commit();

    }

    public void loadPromotion(){

        PromotionFragment promotion = new PromotionFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, promotion).addToBackStack(null).commit();

    }

    public void loadCheckout(){

        CheckoutFragment checkout = new CheckoutFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, checkout).commit();

    }

    public void loadEditor(){

        EditorFragment editor = new EditorFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, editor).addToBackStack(null).commit();

    }

    public void loadEditTab1(){

        EditTab1Fragment edt_tab1 = new EditTab1Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, edt_tab1).addToBackStack(null).commit();

    }

    public void backfragment(){
        getSupportFragmentManager().popBackStack();
    }

}