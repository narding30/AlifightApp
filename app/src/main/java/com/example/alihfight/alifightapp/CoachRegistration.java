package com.example.alihfight.alifightapp;

import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alihfight.alifightapp.Admin.Activities.SetScheduleDataActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CoachRegistration extends AppCompatActivity {


    EditText ETFirstname, ETLastname,ETSessionInstructor,ETEmail,ETPassword;
    Button btnRegcoach;
    ImageButton btnbackCoach;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_registration);

        mAuth = FirebaseAuth.getInstance();

        ETFirstname = findViewById(R.id.ETFirstnameCoach);
        ETLastname = findViewById(R.id.ETLastNameCoach);
        ETSessionInstructor = findViewById(R.id.ETSessionInstructor);
        ETEmail = findViewById(R.id.ETEmailCoach);
        ETPassword = findViewById(R.id.ETPasswordCoach);
        btnRegcoach = findViewById(R.id.btnRegisterCoach);
        btnbackCoach = findViewById(R.id.btnBackRegCoach);
        mProgressBar = findViewById(R.id.progressBarRegCoach);

        ETSessionInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items2 = {
                        "Boxing", "Muay Thai","Judo","Fitness"
                };

                AlertDialog.Builder builder2 = new AlertDialog.Builder(CoachRegistration.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        ETSessionInstructor.setText(items2[item]);
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
            }
        });

        btnRegcoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regCoach();
            }
        });


        btnbackCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CoachRegistration.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(CoachRegistration.this, MainActivity.class));
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void regCoach(){
        final String getFirstname = ETFirstname.getText().toString();
        final String getLastname  = ETLastname.getText().toString();
        final String getInstructor = ETSessionInstructor.getText().toString();
        final String getEmail = ETEmail.getText().toString();
        final String getPass = ETPassword.getText().toString();


        if (TextUtils.isEmpty(getFirstname) ||
                TextUtils.isEmpty(getLastname) ||
                TextUtils.isEmpty(getInstructor) ||
                TextUtils.isEmpty(getEmail) ||
                TextUtils.isEmpty(getPass) ){
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
        }else {
            mProgressBar.setVisibility(View.VISIBLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            mAuth.createUserWithEmailAndPassword(getEmail, getPass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String user_id = currentUser.getUid();

                                DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                final String date1 = df1.format(Calendar.getInstance().getTime());

                                HashMap<String, String> User = new HashMap<String, String>();
                                User.put("FirstName", getFirstname);
                                User.put("LastName", getLastname);
                                User.put("FullName", getFirstname+" " + getLastname);
                                User.put("SessionInstructor", getInstructor);
                                User.put("Email", getEmail);
                                User.put("Password", getPass);
                                User.put("usertype", "personnel");
                                User.put("Time", date1);
                                User.put("Id", user_id);
                                User.put("Status", "Approved");

                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(user_id);
                                databaseReference1.setValue(User);

                                DatabaseReference databaseReferenc2 = FirebaseDatabase.getInstance().getReference("Coaches").child(user_id);
                                databaseReferenc2.setValue(User);


                                mProgressBar.setVisibility(View.INVISIBLE);

                                mAuth.signOut();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                startActivity(new Intent(CoachRegistration.this, MainActivity.class));

                            }else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(CoachRegistration.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
