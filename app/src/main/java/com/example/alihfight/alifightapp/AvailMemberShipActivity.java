package com.example.alihfight.alifightapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AvailMemberShipActivity extends AppCompatActivity {

    Button btnApply;
    String Id;
    ProgressBar progressBar;
    final Context context = this;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_member_ship);

        final HashMap<String, String> hashMap = (HashMap<String, String>)
                getIntent().getSerializableExtra("usersData");

        btnApply = findViewById(R.id.btnApplyMem);
        progressBar = findViewById(R.id.progressBarRegUser2);

        Id = getIntent().getStringExtra("Id");


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.membership_instruction);

                Button btnconfirm = dialog.findViewById(R.id.btnconfirm);

                btnconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);

                        Toast.makeText(AvailMemberShipActivity.this, "Saving details....", Toast.LENGTH_SHORT).show();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PendingMembers").child(Id);
                        databaseReference.setValue(hashMap);

                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(Id);
                        databaseReference1.setValue(hashMap);


                        dialog.dismiss();
                        startActivity(new Intent(AvailMemberShipActivity.this, MainActivity.class));
                    }
                });

                dialog.show();
            }
        });
    }
}
