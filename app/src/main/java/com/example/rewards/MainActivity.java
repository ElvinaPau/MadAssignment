package com.example.rewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Voucher");
        String name="5% off Zus Coffee";
        String description = "Enjoy 5% off when you treat yourself to a cup of Zus Coffee! Savor the rich flavors and aromatic blends while indulging in a special offer. Simply make your purchase and relish the savings. Cheers to a delightful coffee experience with Zus!";
        String point = "50";

        String name1="RM5 off Jaya Grocer";
        String description1 = "Get RM5 off your purchase at Jaya Grocer. Treat yourself and simply make your way to Jaya Grocer and savor the extra savings on your shopping. Don't miss out on this opportunity to enjoy more for less!";
        String point1 = "100";

        String name2="RM20 off Watsons";
        String description2 = "Indulge in a shopping spree and enjoy a fabulous RM20 off your purchase. Whether you're stocking up on beauty essentials, wellness products, or personal care items, this exclusive discount is your ticket to more for less.  Don’t miss the boat!";
        String point2 = "400";

        String name3="25% off Seoul Garden Hotpot";
        String description3 = "Enjoy a fantastic 25% off at Seoul Garden Hotpot. Dive into a world of delectable hotpot delights while relishing the savings. Gather your friends and family for a memorable dining experience without breaking the bank. Hurry, let the feast begin!";
        String point3 = "1000";

        RewardHelperClass helperClass = new RewardHelperClass(name, description, point);
        reference.child(name).setValue(helperClass);


        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Reward"), MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}