package com.example.project_duo.Others;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_duo.Fragments.LoginFragment;
import com.example.project_duo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PortalAdapter extends RecyclerView.Adapter<PortalAdapter.MyViewHolder> {

    private List<FileInfoPortal> infoPortalList;
    private LayoutInflater my_inflater;
    private AuxiliarRVClickListener auxClickListener;
    private Context context;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Codes").child(LoginFragment.login_eventID);

    public PortalAdapter(Context c, List<FileInfoPortal> l){
        infoPortalList=l;
        context=c;
        my_inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = my_inflater.inflate(R.layout.progress_editor_item,parent,false);
        PortalAdapter.MyViewHolder viewHolder = new PortalAdapter.MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Typeface tf_heavy = Typeface.createFromAsset(context.getAssets(),"fonts/AvenirLTStd-Heavy.otf");
        holder.btnEdit.setTypeface(tf_heavy);

        holder.tvTitulo.setText(infoPortalList.get(position).getTitulo());
        holder.ivIcone.setImageResource(infoPortalList.get(position).getIcone());
        holder.tvQtd.setText(infoPortalList.get(position).getQtd_itens());
        holder.progressBar.setMax(Character.getNumericValue(infoPortalList.get(position).getQtd_itens().charAt(0)));
        holder.progressBar.setProgress(0);

        switch (position){
            case 0:
                holder.dot1.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.dark_gray), PorterDuff.Mode.SRC_IN);
                holder.dot2.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot3.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot4.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    int count=0;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("video").hasChild("data")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso1 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso1)+"%");
                        }
                        if(dataSnapshot.child("video").hasChild("songUrl")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso1 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso1)+"%");
                        }
                        if(dataSnapshot.child("video").hasChild("videoUrl")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso1 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso1)+"%");
                        }
                        if(dataSnapshot.child("story").hasChild("noivo")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso1 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso1)+"%");
                        }
                        if(dataSnapshot.child("story").hasChild("noiva")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso1 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso1)+"%");
                        }
                        if(dataSnapshot.child("story").hasChild("texto")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso1 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso1)+"%");
                        }
                        if(dataSnapshot.hasChild("slideshow")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso1 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso1)+"%");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case 1:
                holder.dot1.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot2.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.dark_gray), PorterDuff.Mode.SRC_IN);
                holder.dot3.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot4.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("padrinhos")){
                            holder.progressBar.setProgress(2);
                            double progresso2 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso2)+"%");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                break;
            case 2:
                holder.dot1.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot2.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot3.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.dark_gray), PorterDuff.Mode.SRC_IN);
                holder.dot4.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);

                mRef.child("evento").addListenerForSingleValueEvent(new ValueEventListener() {
                    int count=0;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("locationAddress")) {
                            if (!dataSnapshot.child("locationAddress").getValue(String.class).isEmpty()) {
                                count++;
                                holder.progressBar.setProgress(count);
                                double progresso3 = (double) holder.progressBar.getProgress() / holder.progressBar.getMax() * 100;
                                holder.tvProgresso.setText(String.format("%.0f", progresso3) + "%");
                            }
                        }
                        if(dataSnapshot.hasChild("locationPhoto")) {
                            if (!dataSnapshot.child("locationPhoto").getValue(String.class).isEmpty()) {
                                count++;
                                holder.progressBar.setProgress(count);
                                double progresso3 = (double) holder.progressBar.getProgress() / holder.progressBar.getMax() * 100;
                                holder.tvProgresso.setText(String.format("%.0f", progresso3) + "%");
                            }
                        }
                        if(dataSnapshot.hasChild("coordLat")) {
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso3 = (double) holder.progressBar.getProgress() / holder.progressBar.getMax() * 100;
                            holder.tvProgresso.setText(String.format("%.0f", progresso3) + "%");
                        }
                        if(dataSnapshot.hasChild("info")) {

                                count++;
                                holder.progressBar.setProgress(count);
                                double progresso3 = (double) holder.progressBar.getProgress() / holder.progressBar.getMax() * 100;
                                holder.tvProgresso.setText(String.format("%.0f", progresso3) + "%");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                break;
            case 3:
                holder.dot1.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot2.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot3.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.mb_gray), PorterDuff.Mode.SRC_IN);
                holder.dot4.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.dark_gray), PorterDuff.Mode.SRC_IN);

                mRef.child("presentes").addListenerForSingleValueEvent(new ValueEventListener() {
                    int count=0;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("agradecimento")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso4 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso4)+"%");
                        }
                        if(dataSnapshot.hasChild("ass")){
                            count++;
                            holder.progressBar.setProgress(count);
                            double progresso4 = (double) holder.progressBar.getProgress()/holder.progressBar.getMax()*100;
                            holder.tvProgresso.setText(String.format("%.0f",progresso4)+"%");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public void setAuxiliarRVClickListener(AuxiliarRVClickListener r){
        auxClickListener = r;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitulo, tvQtd, tvProgresso;
        TextView dot1, dot2, dot3, dot4;
        ImageView ivIcone;
        Button btnEdit;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitulo = (TextView) itemView.findViewById(R.id.txv_edt_title);
            tvQtd = (TextView) itemView.findViewById(R.id.txv_edt_qtd);
            tvProgresso = (TextView) itemView.findViewById(R.id.txv_edt_prog);
            ivIcone = (ImageView) itemView.findViewById(R.id.imv_edt_icon);
            dot1 = (TextView) itemView.findViewById(R.id.dot1);
            dot2 = (TextView) itemView.findViewById(R.id.dot2);
            dot3 = (TextView) itemView.findViewById(R.id.dot3);
            dot4 = (TextView) itemView.findViewById(R.id.dot4);
            btnEdit = (Button) itemView.findViewById(R.id.btn_edt);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_edt);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(auxClickListener!=null){
                        auxClickListener.onClickListener(view,getLayoutPosition());
                    }
                }
            });

        }
    }

}
