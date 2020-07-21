package com.example.project_duo.Others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.ArrayRes;

import com.example.project_duo.R;

public class FAQListAdapter extends BaseAdapter {

    private final CharSequence[] items_perguntas;
    private final CharSequence[] items_respostas;

    public FAQListAdapter(Context context, @ArrayRes int arrayResId1, @ArrayRes int arrayResId2) {
        this(context.getResources().getTextArray(arrayResId1),context.getResources().getTextArray(arrayResId2));
    }

    private FAQListAdapter(CharSequence[] items1,CharSequence[] items2) {
        this.items_perguntas = items1;
        this.items_respostas = items2;
    }

    @Override
    public int getCount() {
        return items_perguntas.length;
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_help,parent,false);

        final TextView pergunta, resposta;
        pergunta = (TextView) view.findViewById(R.id.txv_faq_pergunta);
        resposta = (TextView) view.findViewById(R.id.txv_faq_resposta);

        pergunta.setText(items_perguntas[position]);
        resposta.setText(items_respostas[position]);

        return view;
    }
}
