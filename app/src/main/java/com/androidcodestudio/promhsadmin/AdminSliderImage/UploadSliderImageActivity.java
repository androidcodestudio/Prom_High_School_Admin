package com.androidcodestudio.promhsadmin.AdminSliderImage;

import static com.androidcodestudio.promhsadmin.Constant.TOPIC;
import com.androidcodestudio.promhsadmin.api.ApiUtilities;
import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.model.NotificationData;
import com.androidcodestudio.promhsadmin.model.PushNotification;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadSliderImageActivity extends AppCompatActivity {

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

    String sliderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_slider_image);
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        sliderName = getIntent().getStringExtra("sliderName");

        addImage = findViewById(R.id.addImage);
        noticeImageView = findViewById(R.id.noticeImageView);
        noticeTitle = findViewById(R.id.noticeTitle);
        UploadNoticeButton = findViewById(R.id.uploadNoticeBtn);

        UploadNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null){
                    Toast.makeText(UploadSliderImageActivity.this, "Please Slider Image", Toast.LENGTH_SHORT).show();
                }else if(noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                }else{
                    //uploadData();
                    uploadImage();
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(UploadSliderImageActivity.this);
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

        filePath= storageReference.child(sliderName).child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UploadSliderImageActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(UploadSliderImageActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void uploadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", noticeTitle.getText().toString());
        map.put("url", downloadUrl);

        dbRef = databaseReference.child(sliderName);
        final String Key =  dbRef.push().getKey();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference()
                .child(sliderName)
                .child(Key).setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //deploy notification
                            PushNotification notification = new PushNotification(new NotificationData("Prom High School (H.S)","Prom High School (H.S) Uploaded "+noticeTitle.getText().toString()+" Class"),TOPIC);
                            sendNotification(notification);
                            //deploy notification

                        } else {
                            Toast.makeText(UploadSliderImageActivity.this, "problem", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void sendNotification(PushNotification notification) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful()){
                    Toast.makeText(UploadSliderImageActivity.this, "success", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    Toast.makeText(UploadSliderImageActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(UploadSliderImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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