package com.example.florify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.florify.db.DBInstance;
import com.example.florify.dialogs.OnTagsSubmitted;
import com.example.florify.dialogs.TagsDialog;
import com.example.florify.helpers.FileHelper;
import com.example.florify.helpers.MapResolver;
import com.example.florify.models.Post;
import com.example.florify.models.PostType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;

public class PlantUploadActivity extends AppCompatActivity implements OnTagsSubmitted {

    private ImageView plantView;
    private String currentPhotoPath;
    private FileHelper fileHelper;
    private File photoPath;
    private EditText etPlantName;
    private EditText etPlantDescription;
    private Button btnSubmit;

    private MapResolver mapResolver;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private Location location;
    private Session session;

    private double longitude, latitude;
    private String plantName, plantDesc;
    private ArrayList<String> tags;

    private ProgressDialog progressDialog;
    private ImageButton btnAddTags;
    private TagContainerLayout mTagGroup;
    private TagsDialog tagsDialog;
    private ChipGroup chipGroup;
    private PostType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_upload);

        getSupportActionBar().hide();

        tags = new ArrayList<String>();
        plantView = findViewById(R.id.imgPlantUpload);
        etPlantName = findViewById(R.id.etPlantUploadName);
        etPlantDescription = findViewById(R.id.etPlantUploadDescription);
        btnSubmit = findViewById(R.id.btnPlantUploadSubmit);
        btnAddTags = findViewById(R.id.btnPlantUploadAddTags);
        mTagGroup = findViewById(R.id.tabGroupPlantUploadTags);

        chipGroup = findViewById(R.id.chipGroupPlantUpload);
        session = new Session(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mapResolver = new MapResolver(this);
        fileHelper = new FileHelper();
        currentPhotoPath = getIntent().getStringExtra("path");
        photoPath = getPhoto();
        type = PostType.LEAF;

        btnAddTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 tagsDialog = new TagsDialog(PlantUploadActivity.this,PlantUploadActivity.this);
                tagsDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantName = etPlantName.getText().toString();
                plantDesc = etPlantDescription.getText().toString();
                Location location = mapResolver.getLastKnownLocation();
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                uploadImage(photoPath);
            }
        });

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

    public void uploadImage(File image)
    {
        progressDialog = new ProgressDialog(PlantUploadActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Uri file = Uri.fromFile(image);

        StorageReference imageReference = storageReference.child("plants/" + plantName + "_" + session.getUsername() + "_" + System.currentTimeMillis());
        UploadTask uploadTask = imageReference.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText( getApplicationContext(), "Failed upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        addPost(
                                new Post(0,
                                        0,
                                        session.getUsername(),
                                        session.getUserId(),
                                        plantDesc,
                                        tags,
                                        System.currentTimeMillis(),
                                        plantName,
                                        uri.toString(),
                                        getCheckedChipType()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    public void addPost(Post post)
    {
        DBInstance.getCollection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //progressDialog.dismiss();
                        final String id = documentReference.getId();

                        Map<String, Object> map = new HashMap<String, Object>(){
                            {
                                put("id", id);
                            }
                        };
                        DBInstance.getCollection("posts")
                                .document(id)
                                .update(map);

                        DBInstance
                                .getCollection("users")
                                .document(session.getUserId())
                                .update("posts", FieldValue.arrayUnion(id))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                CollectionReference collectionReference = DBInstance.getCollection("posts");
                                GeoFirestore geoFirestore = new GeoFirestore(collectionReference);

                                geoFirestore.setLocation(id, new GeoPoint(latitude, longitude), new GeoFirestore.CompletionCallback() {
                                            @Override
                                            public void onComplete(Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),
                                                        "Sucessfuly uploaded plant",
                                                        Toast.LENGTH_SHORT).show();
                                                updateUI();
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "didn't save",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "didn't save",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private PostType getCheckedChipType() {
        PostType type = PostType.LEAF;
        switch (chipGroup.getCheckedChipId())
        {
            case R.id.plantUploadChipLeaf : {
                type = PostType.LEAF;
                break;
            }
            case R.id.plantUploadChipFlower : {
                type = PostType.FLOWER;
                break;
            }
            case R.id.plantUploadChipStake: {
                type = PostType.SCAPE;
                break;
            }
            case R.id.plantUploadChipPlant : {
                type = PostType.WHOLEPLANT;
                break;
            }
        }
        return type;
    }

    public void updateUI(){
        Intent intent = new Intent(PlantUploadActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnTagsSubmitCompleted(ArrayList<String> tags) {
        this.tags = tags;
        this.mTagGroup.setTags(tags);
        tagsDialog.dismiss();
    }
}
