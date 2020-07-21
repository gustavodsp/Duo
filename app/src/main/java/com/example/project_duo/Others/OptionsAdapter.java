package com.example.project_duo.Others;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_duo.R;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {

    private List<FileInfoMenu> infoMenuList;
    private LayoutInflater my_inflater;
    private AuxiliarRVClickListener auxClickListener;
    private Context context;

    public OptionsAdapter(Context c, List<FileInfoMenu> l){
        infoMenuList=l;
        context=c;
        my_inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = my_inflater.inflate(R.layout.menu_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.ivIcon.setImageResource(infoMenuList.get(position).getIcone());
//        holder.tvTitle.setText(infoMenuList.get(position).getTitulo());
//        holder.tvDescription.setText(infoMenuList.get(position).getDescricao());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        ViewGroup.LayoutParams params = holder.llCelula.getLayoutParams();
        params.width=(2*width/3);
        holder.llCelula.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setAuxiliarRVClickListener(AuxiliarRVClickListener r){
        auxClickListener = r;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView ivIcon;
        //        TextView tvTitle;
//        TextView tvDescription;
        LinearLayout llCelula;

        MyViewHolder(final View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.imv_menu);
//            tvTitle = (TextView) itemView.findViewById(R.id.textView75);
//            tvDescription = (TextView) itemView.findViewById(R.id.textView76);
            llCelula = (LinearLayout) itemView.findViewById(R.id.layout_menu);

            itemView.setOnClickListener(new View.OnClickListener() {
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
