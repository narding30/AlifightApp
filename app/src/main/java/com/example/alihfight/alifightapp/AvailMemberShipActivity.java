package com.example.alihfight.alifightapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alihfight.alifightapp.Admin.Activities.AddScheduleActivity;
import com.example.alihfight.alifightapp.User.Activities.UserHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AvailMemberShipActivity extends AppCompatActivity {

    Button btnApply;
    String Id;
    ProgressBar progressBar;
    final Context context = this;
    EditText ETPackage;
    EditText ETSession;
    String Identifier;

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
        ETPackage = findViewById(R.id.ETPackage);
        ETSession = findViewById(R.id.ETSession);

        Id = getIntent().getStringExtra("Id");
        Identifier = getIntent().getStringExtra("Identifier");



        ETPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items2 = {
                        "8 Sessions", "10 Sessions","12 Sessions"
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(AvailMemberShipActivity.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        ETPackage.setText(items2[item]);
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
            }
        });

        ETSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items2 = {
                        "Judo", "Muay Thai","Fitness","Boxing"
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(AvailMemberShipActivity.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        ETSession.setText(items2[item]);
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
            }
        });


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String getPackage = ETPackage.getText().toString();
                final String getSession = ETSession.getText().toString();

                if (TextUtils.isEmpty(getPackage) ||
                        TextUtils.isEmpty(getSession)){
                    Toast.makeText(context, "Please, Don't leave any blank fields", Toast.LENGTH_SHORT).show();
                }else {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.membership_instruction);

                    Button btnconfirm = dialog.findViewById(R.id.btnconfirm);
                    final TextView total1 = dialog.findViewById(R.id.TVTotal);
                    TextView sessionfee = dialog.findViewById(R.id.TVsessionFee);

                    final int CountSession;
                    final double packagepayment;
                    final double total;
                    if (getPackage.equals("8 Sessions")){

                        CountSession = 8;
                        packagepayment = 1280.00;
                        total = packagepayment + 1000.00;
                        total1.setText(Double.toString(total));
                        sessionfee.setText(Double.toString(packagepayment));

                    }else if (getPackage.equals("10 Sessions")){

                        CountSession = 10;
                        packagepayment = 1500.00;
                        total = packagepayment + 1000.00;
                        total1.setText(Double.toString(total));
                        sessionfee.setText(Double.toString(packagepayment));

                    }else {

                        CountSession = 12;
                        packagepayment = 1680.00;
                        total = packagepayment + 1000.00;
                        total1.setText(Double.toString(total));
                        sessionfee.setText(Double.toString(packagepayment));

                    }

                    btnconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressBar.setVisibility(View.VISIBLE);

                            Toast.makeText(AvailMemberShipActivity.this, "Saving details....", Toast.LENGTH_SHORT).show();

                            if (Identifier.equals("New")){
                                HashMap<String, String> HashString = new HashMap<String, String>();
                                HashString.put("Package", getPackage);
                                HashString.put("SessionName", getSession);
                                HashString.put("PackagePayment", String.valueOf(packagepayment));
                                HashString.put("TotalPayment", String.valueOf(total));
                                HashString.put("SessionCount", String.valueOf(CountSession));

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PendingMembers").child(Id);
                                databaseReference.setValue(hashMap);

                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(Id);
                                databaseReference1.setValue(hashMap);

                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("PaymentDetails").child(Id);
                                databaseReference2.setValue(HashString);


                                dialog.dismiss();
                                startActivity(new Intent(AvailMemberShipActivity.this, MainActivity.class));
                            }else {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PaymentDetails");

                                databaseReference.child(Id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            String SessionCount = dataSnapshot.child("SessionCount").getValue().toString();


                                            int getTotalSession = CountSession + Integer.valueOf(SessionCount);

                                            DatabaseReference saveNewValue = FirebaseDatabase.getInstance().getReference("PaymentDetails");

                                            saveNewValue.child(Id)
                                                    .child("SessionCount")
                                                    .setValue(getTotalSession)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(AvailMemberShipActivity.this, "Sucessfully Updated", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AvailMemberShipActivity.this, UserHomeActivity.class));
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
}
