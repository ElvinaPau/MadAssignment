package com.example.rewards;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UploadPic extends AppCompatActivity {

    private StorageReference storageReference;
    private LinearProgressIndicator progress;
    private Uri image;
    private MaterialButton selectImage, uploadImage;
    private ImageView imageView;
    DatabaseReference reference;
    FirebaseDatabase database;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            image = result.getData().getData();
                            uploadImage.setEnabled(true);
                            Glide.with(getApplicationContext()).load(image).into(imageView);
                        }
                    } else {
                        Toast.makeText(UploadPic.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Reward");

        FirebaseApp.initializeApp(UploadPic.this);
        storageReference = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = findViewById(R.id.progress);
        imageView = findViewById(R.id.imageView);
        selectImage = findViewById(R.id.selectImage);
        uploadImage = findViewById(R.id.uploadImage);


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(image);
            }
        });
    }

    private void uploadImage(Uri image) {
        StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
        reference.putFile(image).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL
            reference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                // Save the download URL to the Realtime Database
                saveImageUrlToDatabase(Uri.parse(downloadUri.toString()));
            });

            Toast.makeText(UploadPic.this, "Image successfully uploaded!", Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(snapshot -> {
            progress.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
            progress.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
        });
    }

    private void saveImageUrlToDatabase(Uri imageUrl) {
        DatabaseReference databaseReference = database.getReference("Reward");
        String imageKey = databaseReference.push().getKey();

        databaseReference.child(imageKey).setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UploadPic.this, "Image URL saved to database", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UploadPic.this, "Failed to save image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
