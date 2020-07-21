package com.example.project_duo.Others;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.project_duo.Fragments.PadrinhosFragment;
import com.example.project_duo.Fragments.Tab2Fragment;
import com.example.project_duo.MainActivity;
import com.example.project_duo.R;

public class PagerAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer  {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;
    private MainActivity context;
    private float scale;

    public PagerAdapter(MainActivity context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == Tab2Fragment.FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        position = position % Tab2Fragment.PAGES;

        return PadrinhosFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount() {
        return Tab2Fragment.PAGES * Tab2Fragment.LOOPS;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        MyLinearLayout myLinearLayout = (MyLinearLayout) page.findViewById(R.id.root);
        float scale = BIG_SCALE;
        if (position > 0) {
            scale = scale - position * DIFF_SCALE;
        } else {
            scale = scale + position * DIFF_SCALE;
        }
        if (scale < 0) scale = 0;
        myLinearLayout.setScaleBoth(scale);
    }
}
