package com.androidcodestudio.promhsadmin.AdminClassEleven;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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

import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFiveActivity;
import com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixActivity;
import com.androidcodestudio.promhsadmin.AdminSliderImage.AllSlideImageActivity;
import com.androidcodestudio.promhsadmin.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.Continuation;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminClassElevenActivity extends AppCompatActivity {

    private List<AdminClassElevenPojo> classElevenPojoList;
    private Dialog category_dialog;
    private CircleImageView _addImage;
    private EditText _categoryName;
    private Uri image;
    private String downloadurl;
    AdminClassElevenAdapter adminClassElevenAdapter;

    ImageSlider mainslider;
    Button showSliderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_eleven);

        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView category_item_recycler_view = findViewById(R.id.category_item_recycler_view);

        showSliderImage = findViewById(R.id.button2);
        showSliderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminClassElevenActivity.this, AllSlideImageActivity.class);
                intent.putExtra("sliderName","ClassElevenSlider");
                startActivity(intent);
            }
        });
        mainslider = (ImageSlider)findViewById(R.id.image_slider);

        final List<SlideModel> remoteimages =new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("ClassElevenSlider")
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
                                    Toast.makeText(AdminClassElevenActivity.this,remoteimages.get(i).getImagePath().toString(),Toast.LENGTH_SHORT).show();

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

        _addImage = category_dialog.findViewById(R.id.category_pic);
        _categoryName = category_dialog.findViewById(R.id.edit_text);
        Button _addBtn = category_dialog.findViewById(R.id.add_button);

        _addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 101);
            }
        });

        _addBtn.setOnClickListener(view -> {
            if (_categoryName.getText().toString().isEmpty()) {
                _categoryName.setError("Required");
                return;
            }
            for(AdminClassElevenPojo pojo :classElevenPojoList){
                if (_categoryName.getText().toString().equals(pojo.getName())){
                    _categoryName.setError("Category Name Already Present!");
                    return;
                }


            }
            //upload
            if (image == null) {
                Toast.makeText(AdminClassElevenActivity.this, "please select Subject image", Toast.LENGTH_SHORT).show();
                return;
            }
            category_dialog.dismiss();
            UploadData();
        });


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Class(Xi) Subjects");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        classElevenPojoList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        category_item_recycler_view.setLayoutManager(gridLayoutManager);

        adminClassElevenAdapter = new AdminClassElevenAdapter(this, classElevenPojoList, new AdminClassElevenAdapter.DeleteListener() {
            @Override
            public void onDelete(String key, int position) {

                new AlertDialog.Builder(AdminClassElevenActivity.this)
                        .setTitle("Delete Category")
                        .setMessage("Are you sure,you want to Delete this category")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child("ClassElevenCategories").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myRef.child("SETS").child(classElevenPojoList.get(position).getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        classElevenPojoList.remove(position);
                                                        adminClassElevenAdapter.notifyDataSetChanged();
                                                    }else{
                                                        Toast.makeText(AdminClassElevenActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });


                                        } else {
                                            Toast.makeText(AdminClassElevenActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null)
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        category_item_recycler_view.setAdapter(adminClassElevenAdapter);

        myRef.child("ClassElevenCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    //categories_pojos.add(Snapshot.getValue(AdminCategoriesPojo.class));
                    classElevenPojoList.add(new AdminClassElevenPojo(Snapshot.child("name").getValue().toString(),
                            Integer.parseInt(Snapshot.child("set").getValue().toString()),
                            Snapshot.child("url").getValue().toString(),
                            Snapshot.getKey()));
                }
                adminClassElevenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminClassElevenActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AdminClassElevenActivity.this, "Selected", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdminClassElevenActivity.this, "problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
            } else {
                Toast.makeText(AdminClassElevenActivity.this, "problem", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UploadCategoryName() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", _categoryName.getText().toString());
        map.put("set", 0);
        map.put("url", downloadurl);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("ClassElevenCategories").child("category" + (classElevenPojoList.size() + 1)).setValue(map)


                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()) {
                            classElevenPojoList.add(new AdminClassElevenPojo(_categoryName.getText().toString(), 0, downloadurl, "category" + (classElevenPojoList.size() + 1)));
                            adminClassElevenAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(AdminClassElevenActivity.this, "problem", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }
}