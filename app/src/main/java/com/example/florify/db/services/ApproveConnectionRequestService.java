package com.example.florify.db.services;

import android.os.AsyncTask;

import com.example.florify.listeners.OnApproveConnectionRequest;

public class ApproveConnectionRequestService extends AsyncTask<String, Void, Void> {

    private OnApproveConnectionRequest listener;
    public ApproveConnectionRequestService(OnApproveConnectionRequest onApproveConnectionRequest) {
        this.listener = onApproveConnectionRequest;
    }
    @Override
    protected Void doInBackground(String... strings) {
        final String myUserId = strings[0];
        final String connectionUserId = strings[1];

        new ApproveDenyConnectionRequestService(listener)
                .ApproveOrDenyConenctionRequest(myUserId, connectionUserId);


        return null;
    }


}
