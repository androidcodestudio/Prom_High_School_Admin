package com.androidcodestudio.promhsadmin.AdminSliderImage;



import static com.androidcodestudio.promhsadmin.Constant.TOPIC;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightActivity;
import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.api.ApiUtilities;
import com.androidcodestudio.promhsadmin.model.NotificationData;
import com.androidcodestudio.promhsadmin.model.PushNotification;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSlideImageActivity extends AppCompatActivity {

    private List<SliderPojo> sliderPojos;
    SliderAdapter sliderAdapter;
    FloatingActionButton _fab_slider;

    private Dialog update_dialog;
    private TextInputEditText updateSubjectName,updateSet;
    private Button updateButton;
    private ProgressDialog progressDialog;
    private ImageView addImage;

    private Bitmap bitmap;
    private StorageReference storageReference;
    String downloadUrl="";
    ImageView profile_img;
    String sliderName;



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_slide_image);
        RecyclerView slider_item_recycler_view = findViewById(R.id.slider_item_recycler_view);
        _fab_slider = findViewById(R.id.fab_slider);

        sliderName = getIntent().getStringExtra("sliderName");

        progressDialog = new ProgressDialog(AllSlideImageActivity.this);
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        storageReference = FirebaseStorage.getInstance().getReference();

        update_dialog = new Dialog(AllSlideImageActivity.this);
        update_dialog.setContentView(R.layout.update_suject);
        update_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.button));
        update_dialog.setCancelable(true);

        updateSubjectName = update_dialog.findViewById(R.id.update_subject);
        updateSet = update_dialog.findViewById(R.id.update_set1);
        addImage = (ImageView) update_dialog.findViewById(R.id.editPhotoIcon);
        updateButton = update_dialog.findViewById(R.id.update1);
        profile_img = update_dialog.findViewById(R.id.profile_img);

        _fab_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSlideImageActivity.this, UploadSliderImageActivity.class);
                intent.putExtra("sliderName",sliderName);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        sliderPojos = new ArrayList<>();
        slider_item_recycler_view.setLayoutManager(new LinearLayoutManager(AllSlideImageActivity.this));

        sliderAdapter = new SliderAdapter(AllSlideImageActivity.this, sliderPojos, new SliderAdapter.DeleteListener()  {
            @Override
            public void onDelete(String key, int position) {

                new AlertDialog.Builder(AllSlideImageActivity.this)
                        .setTitle("Delete Slider Item?")
                        .setMessage("Are you sure,you want to Delete this Slider Image")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child(sliderName)
                                        .child(key)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    sliderPojos.remove(position);
                                                    sliderAdapter.notifyDataSetChanged();
                                                    Toast.makeText(AllSlideImageActivity.this, "slider image successfully remove", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(AllSlideImageActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }).setNegativeButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_dialog.show();
                                updateSubjectName.setText(sliderPojos.get(position).getTitle());
                                updateSet.setText(sliderPojos.get(position).getUrl());

                                addImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CropImage.activity().start(AllSlideImageActivity.this);
                                    }
                                });

                                updateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String  title = updateSubjectName.getText().toString();
                                        String  url = updateSet.getText().toString();

                                        if (bitmap == null) {
                                            Toast.makeText(AllSlideImageActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                        }else if(title.isEmpty()){
                                            updateSubjectName.setError("Empty");
                                            updateSubjectName.requestFocus();
                                        }else if (url.isEmpty()){
                                            updateSet.setError("Empty");
                                            updateSet.requestFocus();
                                        }else{
                                            progressDialog.setMessage("Uploading...");
                                            progressDialog.show();

                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
                                            byte[] finalImage = baos.toByteArray();
                                            final StorageReference filePath;

                                            filePath= storageReference.child("Slider").child(finalImage+"jpg");
                                            final UploadTask uploadTask = filePath.putBytes(finalImage);
                                            uploadTask.addOnCompleteListener(AllSlideImageActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                                                        HashMap hp = new HashMap();
                                                                        hp.put("title",title);
                                                                        hp.put("url",downloadUrl);
                                                                        myRef.child("ClassFiveSlider")
                                                                                .child(key)
                                                                                .updateChildren(hp)
                                                                                .addOnSuccessListener(new OnSuccessListener() {
                                                                                    @SuppressLint("NotifyDataSetChanged")
                                                                                    @Override
                                                                                    public void onSuccess(Object o) {
                                                                                        progressDialog.dismiss();
                                                                                        update_dialog.dismiss();
                                                                                        //deploy notification
                                                                                        PushNotification notification = new PushNotification(new NotificationData("Prom High School ","Prom High School  Uploaded "+title+" Class"),TOPIC);
                                                                                        sendNotification(notification);
                                                                                        //deploy notification
                                                                                        Toast.makeText(AllSlideImageActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
                                                                                        sliderAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText(AllSlideImageActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }else{
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AllSlideImageActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });





                                        }
                                    }
                                });
                            }
                        })
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        slider_item_recycler_view.setAdapter(sliderAdapter);
        myRef.child(sliderName).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    sliderPojos.add(new SliderPojo(
                            Snapshot.child("title").getValue().toString(),
                            Snapshot.child("url").getValue().toString(),
                            Snapshot.getKey()));
                }
                sliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllSlideImageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(PushNotification notification) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful()){
                    Toast.makeText(AllSlideImageActivity.this, "success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AllSlideImageActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(AllSlideImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

                Glide.with(this).load(resultUri).into(profile_img);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        sliderAdapter.notifyDataSetChanged();
    }
}