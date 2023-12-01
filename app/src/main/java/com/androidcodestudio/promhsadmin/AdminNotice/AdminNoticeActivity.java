package com.androidcodestudio.promhsadmin.AdminNotice;

import static com.androidcodestudio.promhsadmin.Constant.TOPIC;
import static com.androidcodestudio.promhsadmin.Constant.sendNotification;

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

import com.androidcodestudio.promhsadmin.AddConceptActivity;
import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.model.NotificationData;
import com.androidcodestudio.promhsadmin.model.PushNotification;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminNoticeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_admin_notice);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        addImage = findViewById(R.id.addImage);
        noticeImageView = findViewById(R.id.noticeImageView);
        noticeTitle = findViewById(R.id.noticeTitle);
        UploadNoticeButton = findViewById(R.id.uploadNoticeBtn);

        UploadNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                }else if(bitmap== null){
                    uploadData();
                }else {
                    uploadImage();
                }


            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQ);
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
         uploadTask.addOnCompleteListener(AdminNoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                     Toast.makeText(AdminNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                 }

             }
         });

    }

    private void uploadData() {
        dbRef = databaseReference.child("PromNotice");
        final String uniqueKey =  dbRef.push().getKey();

        String  NoticeTitle = noticeTitle.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        AdminNoticePojo adminNoticePojo = new AdminNoticePojo(NoticeTitle,downloadUrl,date,time,uniqueKey);
        dbRef.child(uniqueKey).setValue(adminNoticePojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                //deploy notification
                PushNotification notification = new PushNotification(new NotificationData("Prom High School (H.S)","Prom High School (H.S) Class Five Concept Uploaded"),TOPIC);
                sendNotification(notification, AdminNoticeActivity.this);
                //deploy notification
                Toast.makeText(AdminNoticeActivity.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AdminNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

            }catch (IOException  e){
                e.printStackTrace();
            }
            noticeImageView.setImageBitmap(bitmap);
        }
    }
}