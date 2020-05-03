package com.example.florify.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.florify.R;
import com.example.florify.models.DateRangeItems;
import com.example.florify.models.PostType;
import com.google.android.material.chip.ChipGroup;

public class FiltersDialog extends Dialog implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private Button btnOk, btnCancel;
    private Spinner spinner;
    private SeekBar seekBar;
    private EditText etPlantName;
    private ChipGroup chipGroup;
    private Context context;
    private OnFiltersSubmitted listener;
    private double radius;

    public FiltersDialog(@NonNull Activity context, OnFiltersSubmitted onFiltersSubmitted) {

        super(context);
        this.context = context;
        this.listener = onFiltersSubmitted;
        radius = 0;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFilterDialogOK : {
                this.listener.OnFiltersSubmitCompleted(
                        this.etPlantName.getText().toString(),
                        getCheckedChipType(),
                        (int)radius,
                        DateRangeItems.LAST_DAY

                );
                break;
            }
            case R.id.btnFilterDialogCancel : {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filters_dialog);
        btnOk = (Button) findViewById(R.id.btnFilterDialogOK);
        btnCancel = (Button) findViewById(R.id.btnFilterDialogCancel);

        spinner = findViewById(R.id.filterDialogSpinner);
        seekBar = findViewById(R.id.filterDialogSeekBar);
        etPlantName = findViewById(R.id.etFilterDialogPlantName);
        chipGroup = findViewById(R.id.chipGroupFilterDialog);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.datetime_range, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private PostType getCheckedChipType() {
        PostType type = PostType.LEAF;
        switch (chipGroup.getCheckedChipId())
        {
            case R.id.filterDialogChipLeaf : {
                type = PostType.LEAF;
                break;
            }
            case R.id.filterDialogChipFlower : {
                type = PostType.FLOWER;
                break;
            }
            case R.id.filterDialogChipStake: {
                type = PostType.SCAPE;
                break;
            }
            case R.id.filterDialogChipPlant : {
                type = PostType.WHOLEPLANT;
                break;
            }
        }
        return type;
    }
}
