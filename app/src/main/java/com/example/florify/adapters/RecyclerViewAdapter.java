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

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Post> posts = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(posts.get(position).getImageUrl())
                .into(holder.plantImage);

        holder.txtPlantName.setText(posts.get(position).getPlantName());
        holder.txtDescription.setText(posts.get(position).getDescription());
        holder.txtViewsNumber.setText(Integer.toString(posts.get(position).getViewsNumber()));
        holder.txtLikesNumber.setText(Integer.toString(posts.get(position).getLikesNumber()));
        holder.txtAddedBy.setText(posts.get(position).getAddedBy());
        holder.post = posts.get(position);
     //   holder.txtDate.setText(new DateTimeHelper().getDateFromMiliseconds(posts.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView parentLayout;
        ImageView plantImage;
        TextView txtPlantName;
        TextView txtDescription;
        TextView txtAddedBy;
        TextView txtLikesNumber;
        TextView txtViewsNumber;

        Post post;
    //    TextView txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.imgCardImage);
            txtPlantName = itemView.findViewById(R.id.txtCardPlantName);
            txtDescription = itemView.findViewById(R.id.txtCardDescrpition);
            txtAddedBy = itemView.findViewById(R.id.txtCardAddedBy);
            txtLikesNumber = itemView.findViewById(R.id.txtCardLikesNumber);
            txtViewsNumber = itemView.findViewById(R.id.txtCardViewsNumber);
     //       txtDate = itemView.findViewById(R.id.txtCardDate);

            parentLayout = itemView.findViewById(R.id.card_view);

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, PlantDetailsActivity.class);
                    intent.putExtra("plantName", post.getPlantName());
                    intent.putExtra("pictureUrl", post.getImageUrl());
                    intent.putExtra("description", post.getDescription());
                    intent.putExtra("addedBy", post.getAddedBy());
                    intent.putExtra("likesNumber", post.getLikesNumber());
                    intent.putExtra("viewsNumber", post.getViewsNumber());
                    intent.putExtra("latitude", post.getLocation().getLatitude());
                    intent.putExtra("longitude", post.getLocation().getLongitude());
                    intent.putExtra("date", post.getDate());
                    intent.putExtra("tags", post.getTags());
                    context.startActivity(intent);
                }
            });
        }
    }
}
