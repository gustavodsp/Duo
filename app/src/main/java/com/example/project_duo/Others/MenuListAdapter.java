package com.example.project_duo.Others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.ArrayRes;

import com.example.project_duo.R;

public class MenuListAdapter extends BaseAdapter {

    private final CharSequence[] items_menu;

    public MenuListAdapter(Context context, @ArrayRes int arrayResId1) {
        this(context.getResources().getTextArray(arrayResId1));
    }

    private MenuListAdapter(CharSequence[] items1) {
        this.items_menu = items1;
    }

    @Override
    public int getCount() {
        return items_menu.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_menu,parent,false);

        final TextView items;

        items = (TextView) view.findViewById(R.id.txv_menuitem);
        items.setText(items_menu[position]);

        return view;
    }
}
