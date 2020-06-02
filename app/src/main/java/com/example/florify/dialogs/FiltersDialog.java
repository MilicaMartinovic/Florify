package com.example.florify.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.florify.R;
import com.example.florify.models.DateRangeItems;
import com.example.florify.models.PostFilters;
import com.example.florify.models.PostType;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class FiltersDialog extends Dialog implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private Button btnOk, btnCancel;
    private Button btnPlants, btnUsers;
    private Spinner spinner;
    private SeekBar seekBar;
    private EditText etPlantName;
    private ChipGroup chipGroup;
    private Chip chipFlower, chipLeaf, chipStake, chipWholePlant;
    private TextView txtRadius, txtRadisValue;
    private LinearLayout linearLayout;

    private Context context;
    private OnFiltersSubmitted listener;
    private int radius;
    private boolean plantsChecked;
    private DateRangeItems selectedDateRange;
    private Switch switchPlantName, switchRadius, switchDateTime;
    private boolean plantsNameEnabled, radisuEnabled, dateTimeSpinnerEnabled;

    public FiltersDialog(@NonNull Context context, OnFiltersSubmitted onFiltersSubmitted) {

        super(context);
        this.context = context;
        this.listener = onFiltersSubmitted;
        radius = 0;
        plantsChecked = true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFilterDialogOK : {
                PostFilters postFilters = new PostFilters();
                if(plantsNameEnabled)
                    postFilters.setPlantName(etPlantName.getText().toString());
                if(radisuEnabled)
                    postFilters.setRadius(radius);
                if(dateTimeSpinnerEnabled)
                    postFilters.setDateRange(selectedDateRange);
                postFilters.setSearchPlants(plantsChecked);
                postFilters.setPostTypes(getCheckedChipTypes());
                this.listener.OnFiltersSubmitCompleted(
                        plantsNameEnabled,
                        radisuEnabled,
                        dateTimeSpinnerEnabled,
                        postFilters
                );
                break;
            }
            case R.id.btnFilterDialogCancel : {
                dismiss();
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
        txtRadius = findViewById(R.id.txtRadiusFiltersDialog);
        txtRadisValue = findViewById(R.id.txtRadiusValueFiltersDialog);
        btnPlants = findViewById(R.id.btnFilterDialogPlants);
        btnUsers = findViewById(R.id.btnFilterDialogUsers);
        linearLayout = findViewById(R.id.linearLayoutSpinner);
        switchDateTime = findViewById(R.id.switchDateTimeFilter);
        switchPlantName = findViewById(R.id.switchPlantNameFilter);
        switchRadius = findViewById(R.id.switchRadiusFilter);

        chipFlower = findViewById(R.id.filterDialogChipFlower);
        chipLeaf = findViewById(R.id.filterDialogChipLeaf);
        chipStake = findViewById(R.id.filterDialogChipStake);
        chipWholePlant = findViewById(R.id.filterDialogChipPlant);

        switchDateTime.setChecked(true);
        switchPlantName.setChecked(true);
        switchRadius.setChecked(true);
        plantsNameEnabled = radisuEnabled = dateTimeSpinnerEnabled = true;

        selectedDateRange = DateRangeItems.LAST_HOUR;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.datetime_range, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDateRange = getSelectedDateRangeByPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;
                txtRadisValue.setText(Integer.toString(radius));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnPlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantsChecked = true;
                btnPlants.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.lighterGreen), PorterDuff.Mode.MULTIPLY);
                //btnPlants.setBackgroundColor(context.getResources().getColor(R.color.lighterGreen, null));
                btnPlants.setTextColor(context.getResources().getColor(R.color.darkerBlueGreen, null));
                btnUsers.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.darkerBlueGreen), PorterDuff.Mode.MULTIPLY);
                //btnUsers.setBackgroundColor(context.getResources().getColor(R.color.darkerBlueGreen, null));
                btnUsers.setTextColor(context.getResources().getColor(R.color.lighterGreen, null));

                etPlantName.setHint("enter plant name");
                chipGroup.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantsChecked = false;
                btnPlants.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.darkerBlueGreen), PorterDuff.Mode.MULTIPLY);
                //btnPlants.setBackgroundColor(context.getResources().getColor(R.color.darkerBlueGreen, null));
                btnPlants.setTextColor(context.getResources().getColor(R.color.lighterGreen, null));

                btnUsers.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.lighterGreen), PorterDuff.Mode.MULTIPLY);
                //btnUsers.setBackgroundColor(context.getResources().getColor(R.color.lighterGreen, null));
                btnUsers.setTextColor(context.getResources().getColor(R.color.darkerBlueGreen, null));

                etPlantName.setHint("enter user name");
                chipGroup.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);

            }
        });

        switchPlantName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                plantsNameEnabled = !plantsNameEnabled;
                etPlantName.setEnabled(plantsNameEnabled);
            }
        });
        switchRadius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radisuEnabled = !radisuEnabled;
                seekBar.setEnabled(radisuEnabled);
            }
        });
        switchDateTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dateTimeSpinnerEnabled = !dateTimeSpinnerEnabled;
                spinner.setEnabled(dateTimeSpinnerEnabled);
            }
        });
    }

    private DateRangeItems getSelectedDateRangeByPosition(int position) {
        switch (position){
            case 0: return DateRangeItems.LAST_HOUR;
            case 1: return DateRangeItems.LAST_DAY;
            case 2: return DateRangeItems.LAST_WEEK;
            case 3: return DateRangeItems.LAST_MONTH;
            case 4: return DateRangeItems.LAST_YEAR;
            case 5: return DateRangeItems.ANYTIME;
            default: return DateRangeItems.ANYTIME;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private ArrayList<PostType> getCheckedChipTypes() {
        ArrayList<PostType> types = new ArrayList<>();
        if(chipFlower.isChecked())
            types.add(PostType.FLOWER);
        if(chipStake.isChecked())
            types.add(PostType.SCAPE);
        if(chipWholePlant.isChecked())
            types.add(PostType.WHOLEPLANT);
        if(chipLeaf.isChecked())
            types.add(PostType.LEAF);

        return types;
    }
}
