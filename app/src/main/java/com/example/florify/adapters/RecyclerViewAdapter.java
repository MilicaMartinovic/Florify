package com.example.florify.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.florify.db.DBInstance;
import com.example.florify.helpers.DateTimeHelper;
import com.example.florify.models.Post;
import com.example.florify.models.PostType;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

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
        holder.txtViewsNumber.setText(Integer.toString(posts.get(position).getViewsNumber()));
        holder.txtLikesNumber.setText(Integer.toString(posts.get(position).getLikesNumber()));
        holder.imgPostType.setImageResource(getDrawableByPostType(posts.get(position).getPostType()));
        holder.txtAddedBy.setText(posts.get(position).getAddedBy());
        holder.txtDate.setText(DateTimeHelper.getDateFromMiliseconds(posts.get(position).getDate()));
        getProfilePicForUser(posts.get(position).getAddedById(), holder);

        holder.post = posts.get(position);
    }

    private void getProfilePicForUser(String addedById, final ViewHolder holder) {
        DBInstance
                .getCollection("users")
                .document(addedById)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            String url = user.imageUrl;
                            Transformation transformation = new RoundedTransformationBuilder()
                                .borderColor(Color.BLACK)
                                .borderWidthDp(3)
                                .cornerRadiusDp(30).oval(false).build();

                            Picasso.get()
                                    .load(url)
                                    .fit()
                                    .transform(transformation)
                                    .into(holder.imgProfile);
                        }
                    }
                });
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
        TextView txtAddedBy;
        TextView txtDate;
        RoundedImageView imgProfile;
        ImageView imgPostType;

        Post post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.imgCardImage);
            txtPlantName = itemView.findViewById(R.id.txtCardPlantName);
            txtLikesNumber = itemView.findViewById(R.id.txtCardLikesNumber);
            txtViewsNumber = itemView.findViewById(R.id.txtCardViewsNumber);
            imgPostType = itemView.findViewById(R.id.imgCardPostType);
            txtAddedBy = itemView.findViewById(R.id.txtCardViewAddedBy);
            imgProfile = itemView.findViewById(R.id.imgCardProfileImage);
            txtDate = itemView.findViewById(R.id.txtCardDate);
            parentLayout = itemView.findViewById(R.id.card_view);

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
                    intent.putExtra("id",post.getId());
                    context.startActivity(intent);
                }
            });
        }


    }
}
