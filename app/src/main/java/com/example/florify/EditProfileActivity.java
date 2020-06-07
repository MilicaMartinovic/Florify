package com.example.florify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.florify.helpers.ListOfCountries;
import com.makeramen.roundedimageview.RoundedImageView;

public class EditProfileActivity extends AppCompatActivity {

    private int PICK_FROM_GALLERY = 10;
    private RoundedImageView imgProfile;
    private TextView txtEditProfilePicture;
    private EditText etUsername;
    private AutoCompleteTextView etCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imgProfile = findViewById(R.id.imgEditProfilePicture);
        txtEditProfilePicture=findViewById(R.id.txtEditProfileProfilePictureEDIT);
        etUsername = findViewById(R.id.etEdtProfileUsername);
        etCountry = findViewById(R.id.etEditProfileCountry);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit profile info");

        ListOfCountries listOfCountries = new ListOfCountries();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.custom_autocompleteitem,
                R.id.autoCompleteItem,
                listOfCountries.getCounties().toArray(new String[listOfCountries.getCounties().size()])
        );
        etCountry.setAdapter(adapter);

        txtEditProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(
                                EditProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        );
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PICK_FROM_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            } else {
                Toast.makeText(getApplicationContext()
                , "Don't have permissions to view gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_CANCELED) {
           if(requestCode == PICK_FROM_GALLERY) {
               if (resultCode == RESULT_OK && data != null) {
                   Uri selectedImage =  data.getData();
                   String[] filePathColumn = {MediaStore.Images.Media.DATA};
                   if (selectedImage != null) {
                       Cursor cursor = getContentResolver().query(selectedImage,
                               filePathColumn, null, null, null);
                       if (cursor != null) {
                           cursor.moveToFirst();

                           int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                           String picturePath = cursor.getString(columnIndex);
                           imgProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                           cursor.close();
                       }
                   }
               }
           }
        }
    }
}
