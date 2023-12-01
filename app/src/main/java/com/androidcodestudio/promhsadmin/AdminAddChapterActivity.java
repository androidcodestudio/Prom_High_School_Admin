package com.androidcodestudio.promhsadmin;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFiveAdapter;
import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFivePojo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAddChapterActivity extends AppCompatActivity {

    private List<AdminClassFivePojo> adminClassFivePojoList;
    private Dialog category_dialog;
    private CircleImageView _addImage;
    private EditText _categoryName;
    private Uri image;
    private String downloadurl;
    private AdminClassFiveAdapter adminClassFiveAdapter;
    private Bitmap bitmap;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_chapter);

        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView category_item_recycler_view = findViewById(R.id.chapter_item_recycler_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        category_dialog = new Dialog(this);
        category_dialog.setContentView(R.layout.add_category_dialog);
        category_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.button));
        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        category_dialog.setCancelable(true);

        _addImage = category_dialog.findViewById(R.id.category_pic);
        _categoryName = category_dialog.findViewById(R.id.edit_text);
        Button _addBtn = category_dialog.findViewById(R.id.add_button);

        _addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(AdminAddChapterActivity.this);
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, 101);
            }
        });

        _addBtn.setOnClickListener(view -> {
            if (_categoryName.getText().toString().isEmpty()) {
                _categoryName.setError("Required");
                return;
            }
            for(AdminClassFivePojo pojo :adminClassFivePojoList){
                if (_categoryName.getText().toString().equals(pojo.getName())){
                    _categoryName.setError("Chapter Name Already Present!");
                    return;
                }


            }
            //upload
            if (bitmap == null) {
                Toast.makeText(AdminAddChapterActivity.this, "Please Select Chapter Image", Toast.LENGTH_SHORT).show();
                return;
            }
            category_dialog.dismiss();
            UploadData();
        });


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Chapter,s");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adminClassFivePojoList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        category_item_recycler_view.setLayoutManager(gridLayoutManager);

        adminClassFiveAdapter = new AdminClassFiveAdapter(this, adminClassFivePojoList, new AdminClassFiveAdapter.DeleteListener() {
            @Override
            public void onDelete(String key, int position) {

                new AlertDialog.Builder(AdminAddChapterActivity.this)
                        .setTitle("Delete Chapter")
                        .setMessage("Are you sure,you want to Delete this Chapter")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child("ClassFiveChapter").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myRef.child("CLASS_FIVE_CHAPTER").child(adminClassFivePojoList.get(position).getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        adminClassFivePojoList.remove(position);
                                                        adminClassFiveAdapter.notifyDataSetChanged();
                                                    }else{
                                                        Toast.makeText(AdminAddChapterActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });


                                        } else {
                                            Toast.makeText(AdminAddChapterActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null)
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        category_item_recycler_view.setAdapter(adminClassFiveAdapter);

        myRef.child("ClassFiveChapter")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    adminClassFivePojoList.add(new AdminClassFivePojo(
                            Snapshot.child("name").getValue().toString(),
                            Integer.parseInt(Snapshot.child("set").getValue().toString()),
                            Snapshot.child("url").getValue().toString(),
                            Snapshot.getKey()));
                }
                adminClassFiveAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminAddChapterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add) {
            Toast.makeText(AdminAddChapterActivity.this, "Selected", Toast.LENGTH_SHORT).show();
            category_dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == 101) {
//            if (resultCode == RESULT_OK) {
//                image = data.getData();
//                _addImage.setImageURI(image);
//            }
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);

                }catch (IOException e){
                    e.printStackTrace();
                }
                //Picasso.with(this).load(resultUri).into(_addImage);
                Glide.with(this).load(resultUri).into(_addImage);
            }
        }
    }

    private void UploadData() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;

        filePath = storageReference.child("Chapters").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AdminAddChapterActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadurl = String.valueOf(uri);
                                    UploadCategoryName();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(AdminAddChapterActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

//    private void UploadData() {
//
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
////        byte[] finalImage = baos.toByteArray();
////        final StorageReference filePath;
////
////        filePath= storageReference.child("categories").child(finalImage+"jpg");
////        final UploadTask uploadTask = filePath.putBytes(finalImage);
//
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference imageReference = storageRef.child("categories").child(image.getLastPathSegment());
//
//        UploadTask uploadTask = imageReference.putFile(image);
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw Objects.requireNonNull(task.getException());
//                }
//                // Continue with the task to get the download URL
//                return imageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()) {
//                            downloadurl = task.getResult().toString();
//                            UploadCategoryName();
//                        } else {
//                            Toast.makeText(AdminClassFiveActivity.this, "problem", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Uri downloadUri = task.getResult();
//            } else {
//                Toast.makeText(AdminClassFiveActivity.this, "problem", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    private void UploadCategoryName() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", _categoryName.getText().toString());
        map.put("set", 0);
        map.put("url", downloadurl);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("ClassFiveChapter")
                .child("chapter" + (adminClassFivePojoList.size() + 1)).setValue(map)


                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            adminClassFivePojoList.add(new AdminClassFivePojo(_categoryName.getText().toString(), 0, downloadurl, "category" + (adminClassFivePojoList.size() + 1)));
                            adminClassFiveAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(AdminAddChapterActivity.this, "problem", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }
}
