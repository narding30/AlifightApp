package com.example.alihfight.alifightapp.Admin.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alihfight.alifightapp.R;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PostContentActivity extends AppCompatActivity implements View.OnClickListener{

    private int GALLERY = 1;
    ImageView btnBack;
    Button btnChoose;
    ImageButton camImage;
    private StorageReference mImageStorage;
    String key;
    private DatabaseReference mUserDatabase;
    DatabaseReference databaseReference2;
    ImageView imageView;
    Button btnpost;
    EditText ETContent;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        camImage = findViewById(R.id.btnCamera);
        btnBack = findViewById(R.id.btnBackPost);
        btnChoose = findViewById(R.id.btnChoose);
        imageView = findViewById(R.id.IVContentImage);
        btnpost = findViewById(R.id.btnPost);
        ETContent = findViewById(R.id.rET);

        camImage.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
        btnpost.setOnClickListener(this);

        databaseReference2 = FirebaseDatabase.getInstance().getReference(("AdminNewsFeed"));
        key = databaseReference2.push().getKey();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("AdminNewsFeed").child(key);

        populateImage();
    }

    public void populateImage(){
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String image = dataSnapshot.child("image").getValue().toString();

                    if (!image.equals("default")){
                        Picasso.with(PostContentActivity.this)
                                .load(image)
                                .fit().centerCrop()
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.zz)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(PostContentActivity.this)
                                                .load(image)
                                                .placeholder(R.drawable.zz)
                                                .into(imageView);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPost:
                DateFormat df1 = new SimpleDateFormat("yyyy/mmm/dd");
                final String date1 = df1.format(Calendar.getInstance().getTime());
                DateFormat df = new SimpleDateFormat("h:mm a");
                String date = df.format(Calendar.getInstance().getTime());
                DateFormat df2 = new SimpleDateFormat("mmm");
                String date2 = df2.format(Calendar.getInstance().getTime());


                final String getContent =  ETContent.getText().toString();
                final String getBtnText = btnChoose.getText().toString();

                if (!TextUtils.isEmpty(getContent)){

                    if (getBtnText.equals("Everyone")){

                        final HashMap<String, String> HashString1 = new HashMap<String, String>();
                        HashString1.put("Content", getContent);
                        HashString1.put("DateTime", date1+ " " + date);
                        HashString1.put("time", date);
                        HashString1.put("month",date2);
                        HashString1.put("key", key);
                        HashString1.put("feedtype", getBtnText);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child("AdminNewsFeed")
                                .child(key)
                                .setValue(HashString1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                        databaseReference.child("PersonnelNewsFeed")
                                                .child(key)
                                                .setValue(HashString1)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                                        databaseReference.child("UsersNewsFeed")
                                                                .child(key)
                                                                .setValue(HashString1)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });

                    }else if (getBtnText.equals("Personnel")){

                        final HashMap<String, String> HashString1 = new HashMap<String, String>();
                        HashString1.put("Content", getContent);
                        HashString1.put("DateTime", date1+ " " + date);
                        HashString1.put("time", date);
                        HashString1.put("month",date2);
                        HashString1.put("key", key);
                        HashString1.put("feedtype", getBtnText);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child("PersonnelNewsFeed")
                                .child(key)
                                .setValue(HashString1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                databaseReference.child("AdminNewsFeed")
                                        .child(key)
                                        .setValue(HashString1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });

                    }else {
                        final HashMap<String, String> HashString1 = new HashMap<String, String>();
                        HashString1.put("Content", getContent);
                        HashString1.put("DateTime", date1+ " " + date);
                        HashString1.put("time", date);
                        HashString1.put("month",date2);
                        HashString1.put("key", key);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child("UsersNewsFeed")
                                .child(key).
                                setValue(HashString1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                        databaseReference.child("AdminNewsFeed")
                                                .child(key)
                                                .setValue(HashString1)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                    }
                                });

                    }

                }else {
                    final Snackbar snackbar = Snackbar
                            .make(PostContentActivity.this.findViewById(R.id.myFrame), "You have not indicated a content before posting!", Snackbar.LENGTH_LONG)
                            .setAction("Got it!", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ETContent.isFocusable();
                                }
                            });

                    snackbar.show();
                }

                break;
            case R.id.btnBackPost:
                startActivity(new Intent(PostContentActivity.this, AdminHomeActivity.class));
                break;
            case  R.id.btnChoose:
                final CharSequence[] items2 = {
                        "Everyone", "Personnel","Public User"
                };

                AlertDialog.Builder builder2 = new AlertDialog.Builder(PostContentActivity.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        btnChoose.setText(items2[item]);
                        if (items2[item].equals("Everyone")){
                            btnChoose.setText("Everyone");
                            btnChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.world, 0, R.drawable.down1, 0);
                        }else if (items2[item].equals("Personnel")){
                            btnChoose.setText("Personnel");
                            btnChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lieutenant, 0, R.drawable.down1, 0);
                        }else if (items2[item].equals("Public User")){
                            btnChoose.setText("Public User");
                            btnChoose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.userss, 0, R.drawable.down1, 0);
                        }
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
            case R.id.btnCamera:
                showPictureDialog();
                break;
        }
    }

    public void SelectFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void showPictureDialog() {
        SelectFromGallery();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode == RESULT_OK){

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

                StorageReference filepath = mImageStorage.child("news_images").child(key + ".jpg");

                //compress image to make thumbnaile
                final StorageReference thumb_file = mImageStorage.child("news_images").child("thumbs").child(key + ".jpg");


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
                                                    Toast.makeText(PostContentActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    } else {

                                        Toast.makeText(PostContentActivity.this, "Error uploading on Thumbnail", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {

                            Toast.makeText(PostContentActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}