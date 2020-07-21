package com.example.project_duo.Others;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewAvenir extends AppCompatTextView {
    public TextViewAvenir(Context context) {
        super(context);
        font();
    }

    public TextViewAvenir(Context context, AttributeSet attrs) {
        super(context, attrs);
        font();
    }

    public TextViewAvenir(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        font();
    }

    private void font(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Roman.otf");
        setTypeface(tf);
    }
}
