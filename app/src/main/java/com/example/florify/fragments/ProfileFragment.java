package com.example.florify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.florify.R;
import com.example.florify.Session;
import com.example.florify.db.services.FetchUserService;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.models.User;

public class ProfileFragment extends Fragment implements OnFetchUserCompleted {

    private TextView txtProfileName;
    private TextView txtProfileLocation;
    private TextView txtProfileLevel;
    private TextView txtProfileContributionScore;
    private TextView txtProfilePosts;
    private TextView txtProfileLikes;
    private TextView txtProfileViews;
    private TextView txtProfileConnections;

    private Session session;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        session = new Session(getActivity());

        getLayoutReferences(view);
        populateUsersData();

        return view;
    }

    private void getLayoutReferences(View view) {
        txtProfileName = view.findViewById(R.id.txtProfileName);
        txtProfileLocation = view.findViewById(R.id.txtProfileLocation);
        txtProfileLevel = view.findViewById(R.id.txtProfileLevel);
        txtProfileContributionScore = view.findViewById(R.id.txtProfileContributionScore);
        txtProfilePosts = view.findViewById(R.id.txtProfilePosts);
        txtProfileLikes = view.findViewById(R.id.txtProfileLikes);
        txtProfileViews = view.findViewById(R.id.txtProfileViews);
        txtProfileConnections = view.findViewById(R.id.txtProfileConnections);
    }

    private void populateUsersData() {
        new FetchUserService(this).execute(session.getUserId());
    }
    @Override
    public void onFetchCompleted(User user) {
        this.user = user;
        populateProfile();
    }

    private void populateProfile() {
        txtProfileName.setText(user.username);
        txtProfileLocation.setText(user.motherland);
        txtProfileConnections.setText(user.connections == null ? "0" : Integer.toString(user.connections.size()));
        txtProfileLevel.setText(user.badge);
        txtProfileConnections.setText(user.connections == null ? "0" : Integer.toString(user.connections.size()));
    }
}
