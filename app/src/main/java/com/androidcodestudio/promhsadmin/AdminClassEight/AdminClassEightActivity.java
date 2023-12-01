package com.androidcodestudio.promhsadmin.AdminClassEight;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixActivity;
import com.androidcodestudio.promhsadmin.AdminSliderImage.AllSlideImageActivity;
import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminClassEightActivity extends AppCompatActivity {

    private List<AdminClassEightPojo> adminClassEightPojoList;
    AdminClassEightAdapter classEightAdapter;

    private Dialog category_dialog,update_dialog;
    private CircleImageView _addImage,_profile_img;
    private EditText _categoryName;
    private Uri image;
    private String downloadurl;

    ImageView updateSubjectIcon;
    TextInputEditText updateSubjectName,_updateSet;
    Button updateButton;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    String  subject;
    int set;
    private StorageReference storageReference;

    ImageSlider mainslider;
    Button showSliderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_eight);

        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView category_item_recycler_view = findViewById(R.id.category_item_recycler_view);

        showSliderImage = findViewById(R.id.button2);
        showSliderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminClassEightActivity.this, AllSlideImageActivity.class);
                intent.putExtra("sliderName","ClassEightSlider");
                startActivity(intent);
            }
        });
        mainslider = (ImageSlider)findViewById(R.id.image_slider);

        final List<SlideModel> remoteimages =new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("ClassEightSlider")
                //.orderByChild("index")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            remoteimages.add(new SlideModel(data.child("url").getValue().toString()
                                    ,data.child("title").getValue().toString(), ScaleTypes.FIT));

                            mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                            mainslider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void doubleClick(int i) {

                                }

                                @Override
                                public void onItemSelected(int i) {
                                    Toast.makeText(AdminClassEightActivity.this,remoteimages.get(i).getImagePath().toString(),Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        category_dialog = new Dialog(this);
        category_dialog.setContentView(R.layout.add_category_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            category_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.button));
        }
        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        category_dialog.setCancelable(true);

        update_dialog = new Dialog(this);
        update_dialog.setContentView(R.layout.update_subject);
        update_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.button));
        update_dialog.setCancelable(true);

        _profile_img = update_dialog.findViewById(R.id.profile_img);
        updateSubjectIcon = update_dialog.findViewById(R.id.editPhotoIcon);
        updateSubjectName = update_dialog.findViewById(R.id.update_subjectNm);
        _updateSet = update_dialog.findViewById(R.id.update_set);
        updateButton = update_dialog.findViewById(R.id.update);


        updateSubjectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(AdminClassEightActivity.this);
            }
        });

        _addImage = category_dialog.findViewById(R.id.category_pic);
        _categoryName = category_dialog.findViewById(R.id.edit_text);
        Button _addBtn = category_dialog.findViewById(R.id.add_button);

        _addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 101);
                //CropImage.activity().start(AdminClassEightActivity.this);
            }
        });

        _addBtn.setOnClickListener(view -> {
            if (_categoryName.getText().toString().isEmpty()) {
                _categoryName.setError("Required");
                return;
            }
            for(AdminClassEightPojo pojo :adminClassEightPojoList){
                if (_categoryName.getText().toString().equals(pojo.getName())){
                    _categoryName.setError("Category Name Already Present!");
                    return;
                }


            }
            //upload
            if (image == null) {
                Toast.makeText(AdminClassEightActivity.this, "please select category image", Toast.LENGTH_SHORT).show();
                return;
            }
            category_dialog.dismiss();
            UploadData();
        });


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Class(Viii) Subjects");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adminClassEightPojoList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        category_item_recycler_view.setLayoutManager(gridLayoutManager);

        classEightAdapter = new AdminClassEightAdapter(this, adminClassEightPojoList, new AdminClassEightAdapter.DeleteListener() {
            @Override
            public void onDelete(String key, int position) {

                new AlertDialog.Builder(AdminClassEightActivity.this)
                        .setTitle("Delete Subject")
                        .setMessage("Are you sure,you want to Delete this Subject")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child("ClassEightCategories").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myRef.child("CLASS_EIGHT_SETS").child(adminClassEightPojoList.get(position).getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        adminClassEightPojoList.remove(position);
                                                        classEightAdapter.notifyDataSetChanged();
                                                    }else{
                                                        Toast.makeText(AdminClassEightActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });


                                        } else {
                                            Toast.makeText(AdminClassEightActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                update_dialog.show();
                                Glide.with(AdminClassEightActivity.this).load(adminClassEightPojoList.get(position).getUrl()).into(_profile_img);
                                updateSubjectName.setText(adminClassEightPojoList.get(position).getName());
                                _updateSet.setText(String.valueOf(adminClassEightPojoList.get(position).getSet()));
                                updateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        progressDialog.setMessage("Updating...");
                                        progressDialog.show();

                                        subject = updateSubjectName.getText().toString();
                                        set = Integer.parseInt(_updateSet.getText().toString());

                                        if (bitmap == null) {
                                            HashMap map = new HashMap();
                                            map.put("name",subject);
                                            map.put("url",adminClassEightPojoList.get(position).getUrl());
                                            map.put("set",set);

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            database.getReference()
                                                    .child("ClassEightCategories")
                                                    .child(adminClassEightPojoList.get(position).getKey()).updateChildren(map)
                                                    .addOnSuccessListener(new OnSuccessListener() {
                                                        @SuppressLint("NotifyDataSetChanged")
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            progressDialog.dismiss();
                                                            update_dialog.dismiss();
                                                            Toast.makeText(AdminClassEightActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
                                                            classEightAdapter.notifyDataSetChanged();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(AdminClassEightActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }else {

                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
                                            byte[] finalImage = baos.toByteArray();
                                            final StorageReference filePath;


                                            filePath = storageReference.child("categories").child(finalImage+"jpg");
                                            final UploadTask uploadTask = filePath.putBytes(finalImage);
                                            uploadTask.addOnCompleteListener(AdminClassEightActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        //downloadurl = task.getResult().toString();
                                                                        downloadurl = String.valueOf(uri);

                                                                        HashMap map = new HashMap();
                                                                        map.put("name",subject);
                                                                        map.put("url",downloadurl);
                                                                        map.put("set",set);

                                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                        database.getReference()
                                                                                .child("ClassEightCategories")
                                                                                .child(adminClassEightPojoList.get(position).getKey()).updateChildren(map)
                                                                                .addOnSuccessListener(new OnSuccessListener() {
                                                                                    @SuppressLint("NotifyDataSetChanged")
                                                                                    @Override
                                                                                    public void onSuccess(Object o) {
                                                                                        progressDialog.dismiss();
                                                                                        update_dialog.dismiss();
                                                                                        Toast.makeText(AdminClassEightActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
                                                                                        classEightAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText(AdminClassEightActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }else{
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AdminClassEightActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }
                                    }
                                });

//                                _updateSubjectBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        subject = updateSubjectName.getText().toString();
//
//                                        if (subject.isEmpty()) {
//                                            _categoryName.setError("Required");
//                                            return;
//                                        }
//                                        HashMap map = new HashMap();
//                                        map.put("name",subject);
//                                        map.put("url",adminClassEightPojoList.get(position).getUrl());
//                                        map.put("set",adminClassEightPojoList.get(position).getSet());
//
//                                        DatabaseReference databaseReference,dbRef;
//                                        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                        database.getReference()
//                                                .child("ClassEightCategories")
//                                                .child(adminClassEightPojoList.get(position).getKey()).updateChildren(map)
//                                                .addOnSuccessListener(new OnSuccessListener() {
//                                                    @Override
//                                                    public void onSuccess(Object o) {
//                                                        progressDialog.dismiss();
//                                                        update_dialog.dismiss();
//                                                        Toast.makeText(AdminClassEightActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
//                                                        classEightAdapter.notifyDataSetChanged();
//                                                    }
//                                                }).addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        progressDialog.dismiss();
//                                                        Toast.makeText(AdminClassEightActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//
//                                    }
//                                });
//
//                                _updateSetBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        set = Integer.parseInt(_updateSet.getText().toString());
//                                        if (_updateSet.getText().toString().isEmpty()) {
//                                            _categoryName.setError("Required");
//                                            return;
//                                        }
//                                        HashMap map = new HashMap();
//                                        map.put("name",adminClassEightPojoList.get(position).getName());
//                                        map.put("url",adminClassEightPojoList.get(position).getUrl());
//                                        map.put("set",set);
//
//
//                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                        database.getReference()
//                                                .child("ClassEightCategories")
//                                                .child(adminClassEightPojoList.get(position).getKey()).updateChildren(map)
//                                                .addOnSuccessListener(new OnSuccessListener() {
//                                                    @SuppressLint("NotifyDataSetChanged")
//                                                    @Override
//                                                    public void onSuccess(Object o) {
//                                                        progressDialog.dismiss();
//                                                        update_dialog.dismiss();
//                                                        Toast.makeText(AdminClassEightActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
//                                                        classEightAdapter.notifyDataSetChanged();
//                                                    }
//                                                }).addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        progressDialog.dismiss();
//                                                        Toast.makeText(AdminClassEightActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                    }
//                                });
                            }
                        })
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        category_item_recycler_view.setAdapter(classEightAdapter);

        myRef.child("ClassEightCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    adminClassEightPojoList.add(new AdminClassEightPojo(Snapshot.child("name").getValue().toString(),
                            Integer.parseInt(Snapshot.child("set").getValue().toString()),
                            Snapshot.child("url").getValue().toString(),
                            Snapshot.getKey()));
                }
                classEightAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminClassEightActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //close on create
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add) {
            Toast.makeText(AdminClassEightActivity.this, "Selected", Toast.LENGTH_SHORT).show();
            category_dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                image = data.getData();
                _addImage.setImageURI(image);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);

                }catch (IOException e){
                    e.printStackTrace();
                }
                Glide.with(this).load(resultUri).into(_profile_img);
            }
        }
    }

    private void UploadData() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageReference = storageRef.child("categories").child(image.getLastPathSegment());

        UploadTask uploadTask = imageReference.putFile(image);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                // Continue with the task to get the download URL
                return imageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadurl = task.getResult().toString();
                            UploadCategoryName();
                        } else {
                            Toast.makeText(AdminClassEightActivity.this, "problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
            } else {
                Toast.makeText(AdminClassEightActivity.this, "problem", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UploadCategoryName() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", _categoryName.getText().toString());
        map.put("set", 0);
        map.put("url", downloadurl);
        DatabaseReference databaseReference,dbRef;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        dbRef = databaseReference.child("ClassEightCategories");
        final String uniqueKey =  dbRef.push().getKey();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("ClassEightCategories").child(uniqueKey).setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()) {
                            adminClassEightPojoList.add(new AdminClassEightPojo(_categoryName.getText().toString(), 0, downloadurl, "category" + (adminClassEightPojoList.size() + 1)));
                            classEightAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(AdminClassEightActivity.this, "problem", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }
}