package com.example.anhnen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anhnen.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    Context context;
    List<Photo> list;

    public PhotoAdapter(Context context, List<Photo> list) {
        this.context = context;
        this.list = list;
    }

    public PhotoAdapter(MainActivity context, List<Photo> photoList) {
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);

        return new PhotoHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder holder, int position) {
        final Photo photo = list.get(position);
        try {

            Glide.with(context).load(photo.getUrlL()).centerCrop().into(holder.imgPost);
            holder.tvView.setText(list.get(position).getViews());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Intent intent = new Intent(context,ImageDetail.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA", photo);
                intent.putExtras(bundle);
                context.startActivity(intent);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
//        Photo photo = list.get(position);
//        holder.tvView.setText(photo.getViews());
//
//
////        holder.tvTitle.setText(photo.getTitle());
//        Picasso.get()
//                .load(photo.getUrlN())
//                .into(holder.imgPost);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//
//
//                holder.rltView.setVisibility(View.VISIBLE);
//                holder.rltTitle.setVisibility(View.VISIBLE);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        holder.rltView.setVisibility(View.INVISIBLE);
//                        holder.rltTitle.setVisibility(View.INVISIBLE);
//
//                    }
//                }, 2000);
//
//            }
//        });

//    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
//        private ImageView img;
//        private TextView tvView;
//        private RelativeLayout rltView;
//        private RelativeLayout rltTitle;
//        private TextView tvTitle;

        private CardView cardView;
        private ImageView imgPost;
        private TextView tvView;




        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
//            img = (ImageView) itemView.findViewById(R.id.img);
//            tvView = (TextView) itemView.findViewById(R.id.tvView);
//            rltView = (RelativeLayout) itemView.findViewById(R.id.rltView);
//            rltTitle = (RelativeLayout) itemView.findViewById(R.id.rltTitle);
//            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
            tvView = (TextView) itemView.findViewById(R.id.tvView);

        }

    }

}
