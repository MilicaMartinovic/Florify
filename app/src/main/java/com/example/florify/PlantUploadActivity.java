package com.example.florify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.florify.helpers.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlantUploadActivity extends AppCompatActivity {

    private ImageView plantView;
    private String currentPhotoPath;
    private FileHelper fileHelper;
    private File photoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_upload);

        plantView = findViewById(R.id.imgPlantUpload);
        fileHelper = new FileHelper();
        currentPhotoPath = getIntent().getStringExtra("path");
        photoPath = getPhoto();

    }

    public File getPhoto() {

        Bitmap bitmap;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        File f = new File(currentPhotoPath);
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        try {

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, bmOptions);
            fileHelper.setBitmap(bitmap);
            this.plantView.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e) {

            e.printStackTrace();
        };

        return f;
    }
}
