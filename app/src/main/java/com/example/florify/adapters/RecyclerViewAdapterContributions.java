package com.example.florify.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.florify.PlantDetailsActivity;
import com.example.florify.R;
import com.example.florify.models.Post;
import com.example.florify.models.PostType;

import java.util.ArrayList;

public class RecyclerViewAdapterContributions extends RecyclerView.Adapter<RecyclerViewAdapterContributions.ViewHolder> {

    private ArrayList<Post> posts = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterContributions(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_contributions,
                parent,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterContributions.ViewHolder holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(posts.get(position).getImageUrl())
                .into(holder.plantImage);

        holder.txtPlantName.setText(posts.get(position).getPlantName());
        holder.txtViewsNumber.setText(Integer.toString(posts.get(position).getViewsNumber()));
        holder.txtLikesNumber.setText(Integer.toString(posts.get(position).getLikesNumber()));
        holder.imgPostType.setImageResource(getDrawableByPostType(posts.get(position).getPostType()));
        holder.post = posts.get(position);
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    private int getDrawableByPostType(PostType postType) {
        switch (postType){
            case WHOLEPLANT: return R.drawable.ic_whole_plant;
            case FLOWER: return R.drawable.ic_flower;
            case SCAPE: return R.drawable.ic_stake;
            case LEAF: return R.drawable.ic_leaf;
            default: return  R.drawable.ic_whole_plant;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView parentLayout;
        ImageView plantImage;
        TextView txtPlantName;
        TextView txtLikesNumber;
        TextView txtViewsNumber;
        ImageView imgPostType;

        Post post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.imgCardContributionsImage);
            txtPlantName = itemView.findViewById(R.id.txtCardContributionsPlantName);
            txtLikesNumber = itemView.findViewById(R.id.txtCardContributionsLikesNumber);
            txtViewsNumber = itemView.findViewById(R.id.txtCardContributionsViewsNumber);
            imgPostType = itemView.findViewById(R.id.imgCardContributionsPostType);


            parentLayout = itemView.findViewById(R.id.cardview_contributions);

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, PlantDetailsActivity.class);
                    intent.putExtra("plantName", post.getPlantName());
                    intent.putExtra("pictureUrl", post.getImageUrl());
                    intent.putExtra("description", post.getDescription());
                    intent.putExtra("addedBy", post.getAddedBy());
                    intent.putExtra("addedById", post.getAddedById());
                    intent.putExtra("likesNumber", post.getLikesNumber());
                    intent.putExtra("viewsNumber", post.getViewsNumber());
                    intent.putExtra("latitude", post.getL().getLatitude());
                    intent.putExtra("longitude", post.getL().getLongitude());
                    intent.putExtra("date", post.getDate());
                    intent.putExtra("tags", post.getTags());
                    context.startActivity(intent);
                }
            });
        }
}
}
