package com.example.florify.db.services;

import android.os.AsyncTask;

import com.example.florify.listeners.OnDenyConnectionRequest;

public class DenyConnectionRequestService extends AsyncTask<String, Void, Void> {
    private OnDenyConnectionRequest listener;

    public DenyConnectionRequestService(OnDenyConnectionRequest onDenyConnectionRequest) {
        this.listener = onDenyConnectionRequest;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String myUserId = strings[0];
        String connectionUserId = strings[1];

        new ApproveDenyConnectionRequestService(listener)
                .ApproveOrDenyConenctionRequest(myUserId, connectionUserId);

        return null;
    }
}
