package com.example.project_duo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.project_duo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTab3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTab3Fragment extends Fragment {

    Button next, prev;
    TextView titulo;
    FragmentTabHost tabHost;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditTab3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTab3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTab3Fragment newInstance(String param1, String param2) {
        EditTab3Fragment fragment = new EditTab3Fragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_tab3, container, false);

        titulo = (TextView) view.findViewById(R.id.txt_pag3);
        next = (Button) view.findViewById(R.id.btn_prox3);
        prev = (Button) view.findViewById(R.id.btn_ante3);
        tabHost = (FragmentTabHost) view.findViewById(R.id.tabhost3);
        tabHost.setup(getActivity(),getChildFragmentManager(),R.id.realtabcontent3);

        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator(""),EditCeremonyFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator(""),EditInfoFragment.class,null);
        tabHost.getTabWidget().setVisibility(View.INVISIBLE);
        tabHost.setCurrentTab(0);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s){
                    case "Tab1":
                        next.setVisibility(View.VISIBLE);
                        prev.setVisibility(View.INVISIBLE);
                        titulo.setText("Ceremony location");
                        break;
                    case "Tab2":
                        next.setVisibility(View.INVISIBLE);
                        prev.setVisibility(View.VISIBLE);
                        titulo.setText("Ceremony information");
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTab(tabHost.getCurrentTab()+1);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTab(tabHost.getCurrentTab()-1);
            }
        });

        return view;
    }
}