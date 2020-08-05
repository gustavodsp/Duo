package com.example.project_duo.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.project_duo.MainActivity;
import com.example.project_duo.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiftFragment extends Fragment {

    Spinner spinner;
    EditText edt_nome, edt_numero, edt_mes, edt_ano, edt_cvc;
    ImageButton button;
    TextView help;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GiftFragment newInstance(String param1, String param2) {
        GiftFragment fragment = new GiftFragment();
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
        View view = inflater.inflate(R.layout.fragment_gift, container, false);

        edt_numero = (EditText) view.findViewById(R.id.edt_cardnumber);
        help = (TextView) view.findViewById(R.id.txv_helppay);
        button = (ImageButton) view.findViewById(R.id.imb_proceed);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        button.setColorFilter(ContextCompat.getColor(getActivity(),R.color.dark_gray), PorterDuff.Mode.SRC_ATOP);

        String[] items = new String[]{"Value to be donated", "20.00", "50.00", "80.00", "100.00", "150.00", "200.00", "250.00", "300.00", "350.00", "400.00", "450.00", "500.00", "600.00", "700.00", "800.00", "900.00", "1000.00", "1500.00", "2000.00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, final int i, long l) {

                spinner.post(new Runnable() {
                    @Override
                    public void run() {

                        if (adapterView.getChildCount() > 0) {
                            if (i == 0) {
                                ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
                            } else {
                                ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_gray));
                            }
                        }

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edt_numero.addTextChangedListener(new TextWatcher() {
            private final char space = ' ';
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // Remove spacing char
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_construction))
                        .setGravity(Gravity.CENTER)
                        .create();
                dialogPlus.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.dialog_construction))
                        .setGravity(Gravity.CENTER)
                        .create();
                dialogPlus.show();
            }
        });

        return view;
    }
}