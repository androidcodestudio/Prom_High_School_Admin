package com.androidcodestudio.promhsadmin.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends AppCompatActivity {
    private TextInputEditText _nameEditText,_mobileEditText,_rollEditText;
    private ProgressBar _progressBar;
    private Button _btnVerify;
    private FirebaseAuth _firebaseAuth;
    private Window _window;

    private Spinner section_category,class_category;
    private ImageView selectImage;
    private CircleImageView galleryImageView;
    private String category,category1;

    private Bitmap bitmap;
    private final int REQ = 1;


    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;
    String downloadUrl="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        initializeView();

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.white));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on


        String[] item = new String[]{"Class","V","Vi","Vii","Viii","IX","X","Xi","Xii"};
        class_category.setAdapter(new ArrayAdapter<String>(LoginActivity.this,
                android.R.layout.simple_spinner_dropdown_item,item));

        class_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category1 = class_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] items = new String[]{"Section","A","B","C","D","E","F"};
        section_category.setAdapter(new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item,items));

        section_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = section_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(LoginActivity.this);
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, REQ);
            }
        });

        _btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _nameEditText.setError(null);
                _mobileEditText.setError(null);
                _rollEditText.setError(null);


                if (bitmap == null) {
                    Toast.makeText(LoginActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_nameEditText.getText().toString().isEmpty()) {
                    _nameEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_mobileEditText.getText().toString().isEmpty()) {
                    _mobileEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_mobileEditText.getText().toString().length() != 10) {
                    _mobileEditText.setError("please enter a valid phone number");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (_rollEditText.getText().toString().isEmpty()) {
                    _rollEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (category.equals("Section")) {
                    Toast.makeText(LoginActivity.this, "Please Select Section", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                if (category1.equals("Class")) {
                    Toast.makeText(LoginActivity.this, "Please Select Class", Toast.LENGTH_SHORT).show();
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                uploadImage();

            }
        });
    }

    private void initializeView() {
        section_category = findViewById(R.id.section_category);
        class_category = findViewById(R.id.class_category);
        selectImage = findViewById(R.id.editPhotoIcon);
        galleryImageView = findViewById(R.id.profile_img);

        _nameEditText = findViewById(R.id.edtSignupName);
        _mobileEditText = findViewById(R.id.edtSignupMobile);
        _rollEditText = findViewById(R.id.edtSignupRoll);
        _progressBar = findViewById(R.id.login_progressbar);
        _btnVerify = findViewById(R.id.btnVerify);
    }
    private void go_to_otp(){
        Intent otp_intent = new Intent(LoginActivity.this, OtpActivity.class);
        otp_intent.putExtra("Person",downloadUrl);
        otp_intent.putExtra("Name",_nameEditText.getText().toString());
        otp_intent.putExtra("Phone",_mobileEditText.getText().toString());
        otp_intent.putExtra("Roll",_rollEditText.getText().toString());
        otp_intent.putExtra("Section",category);
        otp_intent.putExtra("Class",category1);
        otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(otp_intent);
        finish();
    }

    private void uploadImage() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;

        filePath= storageReference.child("UserProfile").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(LoginActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    //uploadData();
                                    go_to_otp();
                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

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
                //Picasso.with(this).load(resultUri).into(galleryImageView);
                Glide.with(this).load(resultUri).into(galleryImageView);
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
