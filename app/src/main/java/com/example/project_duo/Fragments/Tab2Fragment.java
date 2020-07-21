package com.example.project_duo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_duo.MainActivity;
import com.example.project_duo.Others.PagerAdapter;
import com.example.project_duo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2Fragment extends Fragment {

    public static int PAGES;
    public static int LOOPS = 1000;
    public static int FIRST_PAGE;

    public PagerAdapter adapter;
    public ViewPager pager;

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(HomeFragment.codigo).child("padrinhos");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Tab2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2Fragment newInstance(String param1, String param2) {
        Tab2Fragment fragment = new Tab2Fragment();
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
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        pager = (ViewPager) view.findViewById(R.id.vpager_tab2);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PAGES = (int) dataSnapshot.getChildrenCount();
                FIRST_PAGE = PAGES * LOOPS / 2;

                adapter = new PagerAdapter((MainActivity) getActivity(), getChildFragmentManager());
                pager.setAdapter(adapter);
                pager.setPageTransformer(false,adapter);

                pager.setCurrentItem(FIRST_PAGE);
                pager.setOffscreenPageLimit(3);

                DisplayMetrics dM = getResources().getDisplayMetrics();
                int widthOfScreen = dM.widthPixels;
                int widthOfView = 250; //in DP
                int spaceBetweenViews = 5; // in DP
                float offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthOfView+spaceBetweenViews, dM);

                pager.setPageMargin((int) (-widthOfScreen+offset));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}