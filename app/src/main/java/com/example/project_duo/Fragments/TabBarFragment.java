package com.example.project_duo.Fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_duo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabBarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabBarFragment newInstance(String param1, String param2) {
        TabBarFragment fragment = new TabBarFragment();
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

        View view = inflater.inflate(R.layout.fragment_tab_bar, container, false);

        FragmentTabHost tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(),getChildFragmentManager(),R.id.realtabcontent);

        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("", ContextCompat.getDrawable(getActivity(),R.drawable.infinite)),Tab1Fragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("", ContextCompat.getDrawable(getActivity(),R.drawable.people)),Tab2Fragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("", ContextCompat.getDrawable(getActivity(),R.drawable.business)),Tab3Fragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("Tab4").setIndicator("", ContextCompat.getDrawable(getActivity(),R.drawable.christmas)),Tab4Fragment.class,null);

        tabHost.setCurrentTab(0);

        return view;
    }
}