package com.example.alihfight.alifightapp.Admin.Activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alihfight.alifightapp.Admin.Datas.DataSchedule;
import com.example.alihfight.alifightapp.Admin.ViewHolders.SchedViewHolder;
import com.example.alihfight.alifightapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerViewScheds;
    Button btnExit;
    Button btnEditImage;
    Button btnAddSched;
    ImageView IVSessionImage;
    TextView TVSessionTitle,TVSessionCap,TVSessionDays;
    private DatabaseReference mUserDatabase;
    public String SessionName, SessionCapacity, SessionsPerWeek;
    private static final int GALLERY_PICK = 1;
    private StorageReference mImageStorage;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        TVSessionCap = findViewById(R.id.TVSessionCap);
        TVSessionTitle = findViewById(R.id.TVSessionName);
        TVSessionDays = findViewById(R.id.TVSessionsPerWeek);

        btnEditImage = findViewById(R.id.btnEditSched);
        btnExit = findViewById(R.id.btnExitSched);
        btnAddSched = findViewById(R.id.BTNAddSchedule);

        IVSessionImage = findViewById(R.id.IVImageSession);
        recyclerViewScheds = findViewById(R.id.recyclerSetSched);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerViewScheds.setLayoutManager(linearLayoutManager);

        SessionName = getIntent().getStringExtra("SessionName");
        SessionCapacity = getIntent().getStringExtra("SessionCapacity");
        SessionsPerWeek = getIntent().getStringExtra("SessionsPerWeek");

        TVSessionTitle.setText(SessionName);
        TVSessionCap.setText(SessionCapacity);
        TVSessionDays.setText(SessionsPerWeek);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("SessionImage").child(SessionName);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        childRef = FirebaseDatabase.getInstance().getReference(SessionName+"DaysSched");

        btnExit.setOnClickListener(this);
        btnEditImage.setOnClickListener(this);
        btnAddSched.setOnClickListener(this);



        populateImage();
    }

    public void populateImage(){
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String image = dataSnapshot.child("image").getValue().toString();

                    if (!image.equals("default")){
                        Picasso.with(AddScheduleActivity.this)
                                .load(image)
                                .fit().centerCrop()
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.zz)
                                .into(IVSessionImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(AddScheduleActivity.this)
                                                .load(image)
                                                .placeholder(R.drawable.zz)
                                                .into(IVSessionImage);
                                    }
                                });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataSchedule, SchedViewHolder>  firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataSchedule, SchedViewHolder>(
                        DataSchedule.class,
                        R.layout.days_schedule_listrow,
                        SchedViewHolder.class,
                        childRef
                ) {
                    @Override
                    protected void populateViewHolder(SchedViewHolder viewHolder, final DataSchedule model, int position) {
                        viewHolder.tvday.setText(model.getSessionDay()+"s");
                        viewHolder.tvLocation.setText(model.getInstructor());
                        viewHolder.tvTime.setText("Start "+model.getTimeStart()+" - End "+model.getTimeEnd());


                        viewHolder.LLdelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SessionSchedule");

                                databaseReference.child(model.getSessionDay())
                                        .child(model.getKey())
                                        .removeValue();

                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(model.getSessionName()+"DaysSched");

                                databaseReference1.child(model.getKey())
                                        .removeValue();

                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("SessionDashBoardAdmin");

                                databaseReference2.child(model.getSessionDay())
                                        .child(model.getKey())
                                        .removeValue();

                            }
                        });

                    }
                };
        recyclerViewScheds.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageURI = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageURI)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                //optional
                //compressor of image
                File thumb_filepath = new File(resultUri.getPath());


                //optional
                //compressor
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //optional
                ByteArrayOutputStream baos  = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("session_images").child(SessionName + ".jpg");

                //compress image to make thumbnaile
                final StorageReference thumb_file = mImageStorage.child("session_images").child("thumbs").child(SessionName + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            UploadTask uploadTask = thumb_file.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    @SuppressWarnings("VisibleForTests")
                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()){

                                        Map updateHashMap = new HashMap<String, String>();
                                        updateHashMap.put("image", thumb_downloadUrl);
                                        updateHashMap.put("thumbimage", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                    Toast.makeText(AddScheduleActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    } else {

                                        Toast.makeText(AddScheduleActivity.this, "Error uploading on Thumbnail", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });

                        } else {

                        Toast.makeText(AddScheduleActivity.this, "Error", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEditSched:
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_PICK);
                break;

            case R.id.btnExitSched:
            AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you want to go back?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        startActivity(new Intent(AddScheduleActivity.this, AdminHomeActivity.class));
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

            case R.id.BTNAddSchedule:
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_addsched, null);
                dialogBuilder.setView(dialogView);

                final EditText ETDay = dialogView.findViewById(R.id.ETDay);
                final EditText ETInstructorName = dialogView.findViewById(R.id.ETInstructor);
                final EditText ETTimeStart = dialogView.findViewById(R.id.ETStartTime);
                final EditText ETTimeEnd = dialogView.findViewById(R.id.ETEndTime);

                Button btnAddSched = dialogView.findViewById(R.id.btnSaveSched);



                final AlertDialog dialog = dialogBuilder.create();

                ETInstructorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coaches");

                        databaseReference.orderByChild("SessionInstructor").equalTo(SessionName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> areas = new ArrayList<String>();

                                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                                    String areaName = areaSnapshot.child("FullName").getValue(String.class);
                                    areas.add(areaName);
                                }
                                final CharSequence[] itemsz = areas.toArray(new CharSequence[areas.size()]);
                                AlertDialog.Builder builderz = new AlertDialog.Builder(AddScheduleActivity.this);
                                builderz.setTitle("Select");
                                builderz.setItems(itemsz, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ETInstructorName.setText(itemsz[i]);
                                    }
                                });
                                AlertDialog alertDialogz = builderz.create();
                                alertDialogz.show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                ETDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final CharSequence[] items2 = {
                                "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddScheduleActivity.this);
                        builder2.setTitle("Make your selection");
                        builder2.setItems(items2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                ETDay.setText(items2[item]);
                            }
                        });
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                    }
                });

                ETTimeStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                        CalendarMinute = calendar.get(Calendar.MINUTE);
                        timepickerdialog = new TimePickerDialog(AddScheduleActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        if (hourOfDay == 0) {
                                            hourOfDay += 12;
                                            format = "AM";
                                        }
                                        else if (hourOfDay == 12) {

                                            format = "PM";
                                        }
                                        else if (hourOfDay > 12) {
                                            hourOfDay -= 12;
                                            format = "PM";
                                        }
                                        else {

                                            format = "AM";
                                        }
                                        if (minute < 10){
                                            ETTimeStart.setText(hourOfDay + ":0" + minute + " " + format);
                                        }else {
                                            ETTimeStart.setText(hourOfDay + ":" + minute + " " + format);
                                        }

                                    }
                                }, CalendarHour, CalendarMinute, false);
                        timepickerdialog.show();
                    }
                });

                ETTimeEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                        CalendarMinute = calendar.get(Calendar.MINUTE);
                        timepickerdialog = new TimePickerDialog(AddScheduleActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        if (hourOfDay == 0) {
                                            hourOfDay += 12;
                                            format = "AM";
                                        }
                                        else if (hourOfDay == 12) {

                                            format = "PM";
                                        }
                                        else if (hourOfDay > 12) {
                                            hourOfDay -= 12;
                                            format = "PM";
                                        }
                                        else {

                                            format = "AM";
                                        }
                                        if (minute < 10){
                                            ETTimeEnd.setText(hourOfDay + ":0" + minute + " " + format);
                                        }else {
                                            ETTimeEnd.setText(hourOfDay + ":" + minute + " " + format);
                                        }

                                    }
                                }, CalendarHour, CalendarMinute, false);
                        timepickerdialog.show();
                    }
                });

                btnAddSched.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String getday = ETDay.getText().toString().trim();
                        String getInstructor = ETInstructorName.getText().toString().trim();
                        String getStartTime = ETTimeStart.getText().toString().trim();
                        String getEndTime = ETTimeEnd.getText().toString().trim();

                        if (TextUtils.isEmpty(getday) ||
                                TextUtils.isEmpty(getInstructor) ||
                                TextUtils.isEmpty(getStartTime) ||
                                TextUtils.isEmpty(getEndTime)){
                            Toast.makeText(AddScheduleActivity.this, "Please, don't leave a field blank.", Toast.LENGTH_SHORT).show();

                        }else {

                            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(("SessionSchedule"));
                            String key = databaseReference2.child(getday).push().getKey();

                            HashMap<String, String> HashString = new HashMap<String, String>();
                            HashString.put("SessionDay", getday);
                            HashString.put("SessionName", SessionName);
                            HashString.put("SessionCapacity", SessionCapacity);
                            HashString.put("SessionsPerWeek", SessionsPerWeek);
                            HashString.put("Instructor", getInstructor);
                            HashString.put("TimeStart", getStartTime);
                            HashString.put("TimeEnd", getEndTime);
                            HashString.put("Key", key);

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SessionName+"DaysSched");
                            databaseReference.child(key).setValue(HashString);

                            DatabaseReference databaseReference1 = firebaseDatabase1.getReference("SessionDetails").child(SessionName);
                            databaseReference1.setValue(HashString);

                            databaseReference2.child(getday)
                                    .child(key)
                                    .setValue(HashString);

                            DatabaseReference databaseReference3 = firebaseDatabase1.getReference("SessionDashBoardAdmin");

                            databaseReference3.child(getday)
                                    .child(key)
                                    .setValue(HashString);

                            Toast.makeText(AddScheduleActivity.this, "Schedule Saved", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to go back?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                startActivity(new Intent(AddScheduleActivity.this, AdminHomeActivity.class));
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
