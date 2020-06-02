package com.example.florify.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.florify.R;

import java.util.ArrayList;
import java.util.Arrays;

public class TagsDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private Button btnOk, btnCancel;
    private EditText etTags;

    private OnTagsSubmitted listener;
    private ArrayList<String> tags;

    public TagsDialog(@NonNull Activity context, OnTagsSubmitted onTagsSubmitted) {
        super(context);
        this.activity = context;
        this.tags = new ArrayList<>();
        this.listener = onTagsSubmitted;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tags_dialog);
        btnOk = (Button) findViewById(R.id.btnTagsDialogOK);
        btnCancel = (Button) findViewById(R.id.btnTagsDialogCancel);
        etTags = findViewById(R.id.etTagsDialogTags);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTagsDialogOK:
            {
                this.listener.OnTagsSubmitCompleted(new ArrayList<String>(
                        Arrays.asList(splitStringByWhiteSpace(etTags.getText().toString()))));
                break;
            }
            case R.id.btnTagsDialogCancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private String[] splitStringByWhiteSpace(String toSplit){
        return toSplit.split("\\s+");
    }

}
