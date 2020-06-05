package com.example.florify.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.florify.R;
import com.example.florify.UserProfileActivity;
import com.example.florify.models.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class RecyclerViewAdapterPendingConnections  extends RecyclerView.Adapter<RecyclerViewAdapterPendingConnections.ViewHolderConnectionRequests> {
    private ArrayList<User> users = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterPendingConnections(ArrayList<User> users, Context context)
    {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterPendingConnections.ViewHolderConnectionRequests onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_card_view, parent, false);
        RecyclerViewAdapterPendingConnections.ViewHolderConnectionRequests holder = new RecyclerViewAdapterPendingConnections.ViewHolderConnectionRequests(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPendingConnections.ViewHolderConnectionRequests holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(users.get(position).imageUrl)
                .into(holder.roundedImageView);

        holder.txtUsername.setText(users.get(position).username);
        holder.txtmotherland.setText(users.get(position).motherland);
        holder.txtBadge.setText(users.get(position).badge);
        holder.user = users.get(position);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }


    public class ViewHolderConnectionRequests extends RecyclerView.ViewHolder {

        public RoundedImageView roundedImageView;
        public TextView txtUsername;
        public TextView txtmotherland;
        public TextView txtBadge;
        public LinearLayout parent;

        public User user;


        public ViewHolderConnectionRequests(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.connectionRequestsLayoutParent);
            roundedImageView = itemView.findViewById(R.id.imgProfileConnectionRequests);
            txtmotherland = itemView.findViewById(R.id.txtMotherlandConnectionRequests);
            txtUsername = itemView.findViewById(R.id.txtUsernameConnectionRequests);
            txtBadge = itemView.findViewById(R.id.txtBadgeConnectionRequests);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra("userId", user.id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
