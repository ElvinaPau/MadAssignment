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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    EditText name, description, point;
    String name1, description1, point1;
    private StorageReference storageReference;
    private LinearProgressIndicator progress;
    private Uri image;
    private MaterialButton selectImage, uploadImage;
    private ImageView imageView;
    DatabaseReference reference;
    FirebaseDatabase database;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        image = result.getData().getData();
                        uploadImage.setEnabled(true);
                        Glide.with(getApplicationContext()).load(image).into(imageView);
                    }
                } else {
                    Toast.makeText(UploadPic.this, "Please select an image", Toast.LENGTH_SHORT).show();
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

        name = findViewById(R.id.input_name);
        description = findViewById(R.id.input_des);
        point = findViewById(R.id.input_point);

        progress = findViewById(R.id.progress);
        imageView = findViewById(R.id.imageView);
        selectImage = findViewById(R.id.selectImage);
        uploadImage = findViewById(R.id.uploadImage);

        selectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });

        uploadImage.setOnClickListener(v -> {
            // Move the EditText value assignments here to get the latest values
            name1 = name.getText().toString().trim();
            description1 = description.getText().toString().trim();
            point1 = point.getText().toString().trim();

            uploadImage(image);
        });
    }

    private void uploadImage(Uri image) {
        StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
        reference.putFile(image).addOnSuccessListener(taskSnapshot -> {
            reference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
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

        // Use the existing storageReference directly
        StorageReference fileReference = storageReference.child("images/" + imageKey + ".jpg");

        fileReference.putFile(imageUrl)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Create a new RewardHelperClass instance
                        RewardHelperClass helperClass = new RewardHelperClass(name1, description1, point1);

                        // Set the properties individually under the imageKey node
                        databaseReference.child(imageKey).child("profileImageUrl").setValue(uri.toString());
                        databaseReference.child(imageKey).child("name").setValue(helperClass.getName());
                        databaseReference.child(imageKey).child("description").setValue(helperClass.getDescription());
                        databaseReference.child(imageKey).child("point").setValue(helperClass.getPoint());

                        // Display a success message
                        Toast.makeText(UploadPic.this, "Upload successful", Toast.LENGTH_LONG).show();

                        // Load the image into the ImageView using Glide
                        Glide.with(UploadPic.this).load(uri).into(imageView);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Toast.makeText(UploadPic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
