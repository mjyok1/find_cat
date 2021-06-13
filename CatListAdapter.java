package com.example.find_cat_info;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.CatViewHolder> {

    private OnClickCatInfo onClickCatInfo;
    private ArrayList<CatData> catDataArrayList;
    private Context context;

    public CatListAdapter(Context context, OnClickCatInfo onClickCatInfo, ArrayList<CatData> catDataArrayList) {
        this.context = context;
        this.onClickCatInfo = onClickCatInfo;
        this.catDataArrayList = catDataArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat_preview, parent, false);

        CatViewHolder catViewHolder = new CatViewHolder(view);


        view.setOnClickListener(view1 -> {
            int position = catViewHolder.getLayoutPosition();
            onClickCatInfo.onClick(position, catDataArrayList.get(position));
        });

        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CatListAdapter.CatViewHolder holder, int position) {

        CatData catData = catDataArrayList.get(position);

        Bitmap bitmap = ImageUtil.getDataImage(context, catData.image);

        holder.preview.setImageBitmap(bitmap);
        holder.name.setText(catData.name);
    }

    @Override
    public int getItemCount() {
        return catDataArrayList.size();
    }

    class CatViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView preview;

        public CatViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cat_name);
            preview = itemView.findViewById(R.id.cat_preview);
        }
    }

    interface OnClickCatInfo {
        void onClick(int position, CatData catData);
    }
}
