package com.example.florify.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.florify.EditProfileActivity;
import com.example.florify.R;
import com.example.florify.Session;
import com.example.florify.db.services.FetchMyPostsViewsAndLIkes;
import com.example.florify.db.services.FetchPostsService;
import com.example.florify.db.services.FetchUserService;
import com.example.florify.dialogs.ContributionsPopup;
import com.example.florify.dialogs.OnContributionsClose;
import com.example.florify.helpers.ContributionScoreCalc;
import com.example.florify.listeners.OnFetchMyPostsViewsAndLikesComplted;
import com.example.florify.listeners.OnFetchPostsCompleted;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.models.Post;
import com.example.florify.models.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements OnFetchUserCompleted, OnFetchPostsCompleted, OnContributionsClose, OnFetchMyPostsViewsAndLikesComplted {

    private static int EDIT_PROFILE = 1;

    private TextView txtProfileName;
    private TextView txtProfileLocation;
    private TextView txtProfileLevel;
    private TextView txtProfileContributionScore;
    private TextView txtProfilePosts;
    private TextView txtProfileLikes;
    private TextView txtProfileViews;
    private ImageButton btnViewContributions;
    private TextView txtProfileConnections;
    private RoundedImageView imgProfile;
    private ContributionsPopup contributionsPopup;
    private Session session;
    private User user;
    private ImageButton btnEditProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        session = new Session(getActivity());

        getLayoutReferences(view);
        populateUsersData();

        btnViewContributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchPostsService(ProfileFragment.this)
                        .execute();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivityForResult(intent,EDIT_PROFILE);
            }
        });
        return view;
    }

    private void getLayoutReferences(View view) {
        imgProfile = view.findViewById(R.id.imgProfileImage);
        txtProfileName = view.findViewById(R.id.txtProfileName);
        txtProfileLocation = view.findViewById(R.id.txtProfileLocation);
        txtProfileLevel = view.findViewById(R.id.txtProfileLevel);
        txtProfileContributionScore = view.findViewById(R.id.txtProfileContributionScore);
        txtProfilePosts = view.findViewById(R.id.txtProfilePosts);
        txtProfileLikes = view.findViewById(R.id.txtProfileLikes);
        txtProfileViews = view.findViewById(R.id.txtProfileViews);
        txtProfileConnections = view.findViewById(R.id.txtProfileConnections);
        btnViewContributions = view.findViewById(R.id.btnProfileViewContributions);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
    }

    private void populateUsersData() {
        new FetchUserService(this).execute(session.getUserId());
    }
    @Override
    public void onFetchCompleted(User user) {
        this.user = user;
        populateProfile();
        new FetchMyPostsViewsAndLIkes(this, user)
                .execute(user.id);
    }

    private void populateProfile() {
        txtProfileName.setText(user.username);
        txtProfileLocation.setText(user.motherland);
        txtProfileConnections.setText(
                (user.connections == null || user.connections.size() == 0) ?
                        "0" : Integer.toString(user.connections.size())
        );
        txtProfilePosts.setText(
                (user.posts == null || user.posts.size() ==0) ?
                        "0" : Integer.toString(user.posts.size())
        );



        txtProfileLevel.setText(user.badge);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(R.color.darkerBlueGreen)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false).build();

        Picasso.get()
                .load(user.imageUrl)
                .fit()
                .transform(transformation)
                .into(imgProfile);
    }

    @Override
    public void OnContributionsClose() {
        this.contributionsPopup.dismiss();
    }

    @Override
    public void onFetchCompleted(ArrayList<Post> posts) {
        contributionsPopup = new ContributionsPopup(getContext(),
                R.style.Theme_Dialog,
                posts,
                ProfileFragment.this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(contributionsPopup.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        contributionsPopup.show();
        contributionsPopup.getWindow().setAttributes(lp);
    }

    @Override
    public void OnFetchMyPostsViewsAndLikesComplted(int views, int likes, User user) {
        int score = ContributionScoreCalc.calculateScore(likes,views,user.posts.size());
        txtProfileLikes.setText(Integer.toString(likes));
        txtProfileViews.setText(Integer.toString(views));
        txtProfileContributionScore.setText(Integer.toString(score));
    }
}
