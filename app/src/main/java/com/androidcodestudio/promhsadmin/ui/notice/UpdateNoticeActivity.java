package com.androidcodestudio.promhsadmin.ui.notice;

import static com.androidcodestudio.promhsadmin.Constant.TOPIC;
import static com.androidcodestudio.promhsadmin.Constant.sendNotification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFiveActivity;
import com.androidcodestudio.promhsadmin.AdminNotice.AdminNoticeActivity;
import com.androidcodestudio.promhsadmin.AdminNotice.AdminNoticePojo;
import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.model.NotificationData;
import com.androidcodestudio.promhsadmin.model.PushNotification;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UpdateNoticeActivity extends AppCompatActivity {

    private CardView addImage;
    private ImageView noticeImageView;
    private Bitmap bitmap;
    private final int REQ = 1;
    private TextInputEditText noticeTitle;
    private Button UploadNoticeButton;
    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;
    String downloadUrl="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notice);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        addImage = findViewById(R.id.addImage);
        noticeImageView = findViewById(R.id.noticeImageView);
        noticeTitle = findViewById(R.id.noticeTitle);
        UploadNoticeButton = findViewById(R.id.uploadNoticeBtn);
        noticeTitle.setText(getIntent().getStringExtra("title"));
        Glide.with(UpdateNoticeActivity.this).load(getIntent().getStringExtra("image")).into(noticeImageView);

        UploadNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                }else if(bitmap == null){
                    updateExistingImageData();
                }else {
                    uploadImage();
                }


            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(UpdateNoticeActivity.this);
            }
        });



    }

    private void uploadImage() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;

        filePath= storageReference.child("PromNotice").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UpdateNoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    updateData();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(UpdateNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateData() {
        dbRef = databaseReference.child("PromNotice");
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        HashMap map = new HashMap();
        map.put("date",date);
        map.put("image",downloadUrl);
        map.put("time",time);
        map.put("title",noticeTitle.getText().toString());

        dbRef.child(getIntent().getStringExtra("key"))
                .updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                //deploy notification
                PushNotification notification = new PushNotification(new NotificationData("Prom High School (H.S)","Prom High School (H.S) Update Notice"),TOPIC);
                sendNotification(notification, UpdateNoticeActivity.this);
                //deploy notification
                Toast.makeText(UpdateNoticeActivity.this, "Notice Updated Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UpdateNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateExistingImageData() {
        dbRef = databaseReference.child("PromNotice");
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        HashMap map = new HashMap();
        map.put("date",date);
        map.put("image",getIntent().getStringExtra("image"));
        map.put("time",time);
        map.put("title",noticeTitle.getText().toString());

        dbRef.child(getIntent().getStringExtra("key"))
                .updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        //deploy notification
                        PushNotification notification = new PushNotification(new NotificationData("Prom High School (H.S)","Prom High School (H.S) Update Notice"),TOPIC);
                        sendNotification(notification, UpdateNoticeActivity.this);
                        //deploy notification
                        Toast.makeText(UpdateNoticeActivity.this, "Notice Updated Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);

                }catch (IOException e){
                    e.printStackTrace();
                }
                Glide.with(this).load(resultUri).into(noticeImageView);
            }
        }

    }
}