package com.androidcodestudio.promhsadmin.ui.gallery;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AdminGalleryActivity;
import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {
    private RecyclerView vocational_list;
    private GalleryAdapter adapter;
    private DatabaseReference reference;

    private ShimmerFrameLayout shimmer_container;
    private LinearLayout shimmer_layout,main_con;
    private FloatingActionButton _add_general;
    private TextView _vocational_title,_civil_title,_mobile_title;

    private Dialog gallery_dialog;
    private Button _addBtn;
    private String downloadUrl="";
    private CircleImageView _addImage;
    private Bitmap bitmap;

    private String category;

    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("GALLERY");
        storageReference = FirebaseStorage.getInstance().getReference().child("GALLERY");;
        progressDialog = new ProgressDialog(getContext());

        shimmer_container =  view.findViewById(R.id.shimmer_view_container);
        shimmer_layout = view.findViewById(R.id.shimmer_layout);
        shimmer_container.startShimmer(); // If auto-start is set to false

        main_con = view.findViewById(R.id.main_con);
        vocational_list = view.findViewById(R.id.vocational_department_list);


        _vocational_title = view.findViewById(R.id.vocational_title);


        _add_general = view.findViewById(R.id.add_general);


        gallery_dialog = new Dialog(getContext());
        gallery_dialog.setContentView(R.layout.add_general_gallery);
        gallery_dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.button));
        gallery_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        gallery_dialog.setCancelable(true);

        _addImage = gallery_dialog.findViewById(R.id.category_pic);
        _addBtn = gallery_dialog.findViewById(R.id.add_button);

        _add_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                gallery_dialog.show();
//                category = _vocational_title.getText().toString();
                Intent intent = new Intent(getContext(), AdminGalleryActivity.class);
                startActivity(intent);

            }
        });

//        _add_civil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                gallery_dialog.show();
////                category = _civil_title.getText().toString();
//            }
//        });
//
//        _add_mobile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                gallery_dialog.show();
////                category = _mobile_title.getText().toString();
//            }
//        });
        _addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(getActivity());
            }
        });
        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null){
                    Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();
                    uploadImage();
                }
            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("GALLERY");
        getVocationalMobileImage();
        return view;
    }

    private void getVocationalMobileImage() {
        reference.child("Prom High School Gallery").addValueEventListener(new ValueEventListener() {
           List<String> imageList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String data = (String) snapshot1.getValue();
                    imageList.add(data);
                }
                adapter = new GalleryAdapter(getContext(),imageList);
                vocational_list.setLayoutManager(new GridLayoutManager(getContext(),3));
                vocational_list.setAdapter(adapter);
                shimmer_container.stopShimmer();
                shimmer_layout.setVisibility(View.GONE);
                main_con.setVisibility(View.VISIBLE);

//                vocational_title.setVisibility(View.VISIBLE);
//                civil_title.setVisibility(View.VISIBLE);
//                mobile_title.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;

        filePath = storageReference.child(finalImage + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener((Activity) getContext(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
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
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void uploadData() {
        dbRef = databaseReference.child(category);
        final String uniqueKey =  dbRef.push().getKey();

        dbRef.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Gallery Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity()
                            .getContentResolver(),resultUri);

                }catch (IOException e){
                    e.printStackTrace();
                }
                //Picasso.with(getContext()).load(resultUri).into(_addImage);
                Glide.with(getActivity()).load(resultUri).into(_addImage);
            }
        }
    }

    @Override
    public void onPause() {
        shimmer_container.stopShimmer();
        super.onPause();
    }

    @Override
    public void onResume() {
        shimmer_container.startShimmer();
        super.onResume();
    }
}