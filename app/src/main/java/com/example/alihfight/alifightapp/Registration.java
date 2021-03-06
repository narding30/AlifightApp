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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class Registration extends AppCompatActivity {

    EditText lastname;
    EditText firstname;
    EditText age;
    EditText middlename;
    EditText Address;
    EditText occupation;
    EditText password;
    EditText email;
    ImageButton back;
    Button btnreg;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;
    String mGender = null;
    RadioGroup rgGender;
    EditText contact;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        lastname = findViewById(R.id.tvLastNameUser);
        firstname = findViewById(R.id.tvFirstNameUser);
        age = findViewById(R.id.tvage);
        middlename = findViewById(R.id.tvMiddleNameUser);
        Address = findViewById(R.id.tvaddress);
        occupation = findViewById(R.id.Occupation);
        password = findViewById(R.id.etpass);
        email = findViewById(R.id.etemail);
        rgGender =  findViewById(R.id.rgGenderUser);
        contact = findViewById(R.id.tvcontact);

        back = findViewById(R.id.btnBackRegUser);
        btnreg = findViewById(R.id.btnRegisterUser);
        mProgressBar = findViewById(R.id.progressBarRegUser);


        selectGender();

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Registration.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(Registration.this, MainActivity.class));
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    private void selectGender() {

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rbMaleUser:
                        mGender = "Male";
                        break;
                    case R.id.rbFemaleUser:
                        mGender = "Female";
                        break;
                }

            }

        });
    }

    private void registerUser() {

        final String getlastname = lastname.getText().toString().trim();
        final String getfirstname = firstname.getText().toString().trim();
        final String getmiddlename = middlename.getText().toString().trim();
        final String getage = age.getText().toString().trim();
        final String getAddress = Address.getText().toString().trim();
        final String getOccupation = occupation.getText().toString().trim();
        final String getpassword = password.getText().toString().trim();
        final String getEmail = email.getText().toString().trim();
        final String getContact = contact.getText().toString().trim();


        if (TextUtils.isEmpty(getlastname) ||
                TextUtils.isEmpty(getfirstname) ||
                TextUtils.isEmpty(getmiddlename) ||
                TextUtils.isEmpty(getage) ||
                TextUtils.isEmpty(getAddress) ||
                TextUtils.isEmpty(getOccupation) ||
                TextUtils.isEmpty(getpassword) ||
                TextUtils.isEmpty(getEmail) ||
                TextUtils.isEmpty(getContact)){

            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
        } else if (mGender.isEmpty()){
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            mAuth.createUserWithEmailAndPassword(getEmail, getpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String user_id = currentUser.getUid();

                                DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                final String date1 = df1.format(Calendar.getInstance().getTime());


                                HashMap<String, String> User = new HashMap<String, String>();
                                User.put("FirstName", getfirstname);
                                User.put("LastName", getlastname);
                                User.put("MiddleName", getmiddlename);
                                User.put("Address", getAddress);
                                User.put("Age", getage);
                                User.put("Occupation", getOccupation);
                                User.put("Gender", mGender);
                                User.put("Email", getEmail);
                                User.put("Password", getpassword);
                                User.put("usertype", "user");
                                User.put("Time", date1);
                                User.put("Id", user_id);
                                User.put("Status", "Pending");
                                User.put("MembershipFee", "1000.00");
                                User.put("Contact", getContact);




                                mProgressBar.setVisibility(View.INVISIBLE);

                                mAuth.signOut();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Intent intent = new Intent(Registration.this, AvailMemberShipActivity.class);
                                intent.putExtra("usersData",User);
                                intent.putExtra("Id",user_id);
                                intent.putExtra("Identifier", "New");
                                startActivity(intent);

                            }else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(Registration.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }
}
