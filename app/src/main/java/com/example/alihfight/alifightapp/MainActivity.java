package com.example.alihfight.alifightapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.alihfight.alifightapp.Admin.Activities.AdminHomeActivity;
import com.example.alihfight.alifightapp.Admin.Datas.DataUser;
import com.example.alihfight.alifightapp.User.UserHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnlogin;
    TextView signup;
    EditText email;
    EditText password;
    ImageButton btnQr;
    private IntentIntegrator qrScan;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBar;
    DatabaseReference databaseReference;
    public String userID;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        qrScan = new IntentIntegrator(this);

        btnlogin = findViewById(R.id.btn_login);
        signup = findViewById(R.id.tvNoAccount);
        email = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        mProgressBar =  findViewById(R.id.progressBarLogin);
        btnQr = findViewById(R.id.btnQr);

        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Registration.class));
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString();
                String getPass = password.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (!TextUtils.isEmpty(getPass) && !isPasswordValid(getPass)) {
                    Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                    focusView = email;
                    cancel = true;
                }

                if (TextUtils.isEmpty(getEmail)) {
                    Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_LONG).show();
                    focusView = email;
                    cancel = true;
                } else if (!isEmailValid(getEmail)) {
                    Toast.makeText(MainActivity.this, "Please enter a valid email", Toast.LENGTH_LONG).show();
                    focusView = email;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(getEmail,getPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        getUserData();

                                    }else {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(MainActivity.this, ""+task.getException(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }

            }
        });
    }

    boolean twice;
    @Override
    public void onBackPressed() {
        if (twice == true){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 3000);
        twice = true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }


    private void getUserData() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID =  currentUser.getUid();

        databaseReference.child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataUser user = dataSnapshot.getValue(DataUser.class);
                if(dataSnapshot.exists()){

                    String usertype = (user.getUsertype());

                    if (firebaseAuth.getCurrentUser() != null) {
                        if (usertype.equals("admin")){
                            startActivity(new Intent(MainActivity.this, AdminHomeActivity.class));
                        }else if (usertype.equals("personnel")){
                            /*startActivity(new Intent(MainActivity.this, PCGHomeActivity.class));*/
                        }else if (usertype.equals("user")){
                            startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
                        }else if (usertype.equals("pcgstation")){
                            /*startActivity(new Intent(MainActivity.this, PcgStationAdminHome.class));*/
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Please, check your internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProgressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    String container;
                    container = (obj.getString(""));

                    if (!container.equals("login")){
                        startActivity(new Intent(MainActivity.this, RegistrationPersonnel.class));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Invalid QR Code" , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    String var = result.getContents();

                    if (var.equals("login")){
                        startActivity(new Intent(MainActivity.this, RegistrationPersonnel.class));
                        finish();
                    }else {
                        Toast.makeText(this, "Invalid QR Code" , Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
