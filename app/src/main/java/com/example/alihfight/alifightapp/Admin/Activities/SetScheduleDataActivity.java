package com.example.alihfight.alifightapp.Admin.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alihfight.alifightapp.MainActivity;
import com.example.alihfight.alifightapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SetScheduleDataActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    Button btnNext;
    EditText ETSessionName;
    EditText ETSessionCap;
    EditText ETSessionWeekly;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule_data);

        btnBack = findViewById(R.id.btnBackRegVessel);
        btnNext = findViewById(R.id.btnnextRegSession);

        ETSessionName = findViewById(R.id.ETSessionTitle);
        ETSessionCap = findViewById(R.id.ETSessionCapacity);
        ETSessionWeekly = findViewById(R.id.ETCountDays);


        ETSessionName.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ETSessionTitle:
                final CharSequence[] items2 = {
                        "Boxing", "Muay Thai","Judo","Fitness"
                };

                AlertDialog.Builder builder2 = new AlertDialog.Builder(SetScheduleDataActivity.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        ETSessionName.setText(items2[item]);
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;

            case  R.id.btnBackRegVessel:
                AlertDialog.Builder builder = new AlertDialog.Builder(SetScheduleDataActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you want to go back?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                       startActivity(new Intent(SetScheduleDataActivity.this, AdminHomeActivity.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


                break;

            case R.id.btnnextRegSession:

                final String getTitle = ETSessionName.getText().toString();
                final String getCap = ETSessionCap.getText().toString();
                final String getDays = ETSessionWeekly.getText().toString();

                if (TextUtils.isEmpty(getTitle) ||
                        TextUtils.isEmpty(getCap) ||
                        TextUtils.isEmpty(getDays)){
                    Toast.makeText(this, "Please don't leave any blank fields!", Toast.LENGTH_SHORT).show();
                }else {

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Sessions");

                    databaseReference2.orderByChild("SessionName").equalTo(getTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Toast.makeText(SetScheduleDataActivity.this, getTitle+" Session is already Registered!", Toast.LENGTH_SHORT).show();

                            }else {

                                DateFormat df = new SimpleDateFormat("h:mm a");
                                String date = df.format(Calendar.getInstance().getTime());

                                HashMap<String, String> HashString = new HashMap<String, String>();
                                HashString.put("SessionName", getTitle);
                                HashString.put("SessionCapacity", getCap);
                                HashString.put("SessionsPerWeek", getDays);

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Sessions");
                                databaseReference.child(getTitle)
                                        .setValue(HashString);


                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("GroupChatHeader");
                                String key = databaseReference1.child(getTitle).push().getKey();

                                HashMap<String, String> HashString1 = new HashMap<String, String>();
                                HashString1.put("Time", date);
                                HashString1.put("SessionName", getTitle);
                                HashString1.put("MessageStatus", "unread");
                                HashString1.put("Key", key);

                                databaseReference1.child(getTitle)
                                        .child(key)
                                        .setValue(HashString1);

                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("AdminGroupChatHeader");

                                databaseReference3.child(getTitle)
                                        .setValue(HashString1);

                                Intent myIntent = new Intent(SetScheduleDataActivity.this, AddScheduleActivity.class);
                                myIntent.putExtra("SessionName", getTitle);
                                myIntent.putExtra("SessionCapacity", getCap);
                                myIntent.putExtra("SessionsPerWeek", getDays);
                                startActivity(myIntent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetScheduleDataActivity.this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to go back?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                startActivity(new Intent(SetScheduleDataActivity.this, AdminHomeActivity.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
