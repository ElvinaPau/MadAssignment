package com.example.rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String rewardKey;
    String headerFromDB, desFromDB, pointFromDB;

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
        this.rewardKey = rewardKey;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        // Bind data and pass the key for the current item
        holder.bindData(getRef(position).getKey());
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, description, validity, point;
        Button redeem;

        DatabaseReference reference;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.Reward_name);
            description = (TextView) itemView.findViewById(R.id.Re_Desc);
            point = (TextView) itemView.findViewById(R.id.Reward_point);
            redeem = (Button) itemView.findViewById(R.id.redeem_button);
            reference = FirebaseDatabase.getInstance().getReference("Reward");
        }

        public void bindData (String rewardKey){
            // Use the provided key to fetch data for the current item
            reference.child(rewardKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Retrieve data specific to the current item
                    RewardHelperClass model = snapshot.getValue(RewardHelperClass.class);
                    MainModel model1 = snapshot.getValue(MainModel.class);

                    if (model != null) {
                        name.setText(model.getName());
                        description.setText(model.getDescription());
                        point.setText(model.getPoint());

                        Glide.with(img.getContext())
                                .load(model1.getImage())
                                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                                .into(img);

                        // Add any additional logic related to the redeemed button, if needed
                        redeem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle button click
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors here
                }
            });

        }


    }
}