package com.androidcodestudio.promhsadmin;

import static com.androidcodestudio.promhsadmin.Constant.TOPIC;
import static com.androidcodestudio.promhsadmin.Constant.sendNotification;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidcodestudio.promhsadmin.Admin_Category.AdminCategoriesActivity;
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

import java.io.File;
import java.util.HashMap;

public class AddConceptActivity extends AppCompatActivity {

    private CardView addConcept;
    private ImageView pdfImageView;
    private Uri conceptData;
    private final int REQ = 1;
    private TextInputEditText conceptTitle;
    private Button UploadConceptButton;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl="";
    private ProgressDialog progressDialog;

    private TextView conceptTextView;
    String conceptName,title;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concept);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        addConcept = findViewById(R.id.addPdf);
        pdfImageView = findViewById(R.id.pdfImageView);
        conceptTitle = findViewById(R.id.pdfTitle);
        UploadConceptButton = findViewById(R.id.uploadPdfBtn);
        conceptTextView = findViewById(R.id.pdfTextView);

        addConcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("application/pdf");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Pdf File"), REQ);

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQ);
            }
        });

        UploadConceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = conceptTitle.getText().toString();

                if (title.isEmpty()) {
                    conceptTitle.setError("Empty");
                    conceptTitle.requestFocus();
                } else if (conceptData == null) {
                    Toast.makeText(AddConceptActivity.this, "Please Upload Video", Toast.LENGTH_SHORT).show();
                } else {
                    uploadPdf();
                }
            }
        });
    }

    private void uploadPdf() {
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Uploading Concept....");
        progressDialog.show();
        StorageReference reference = storageReference.child("CLASS_FIVE_CONCEPT/"+conceptName);
        reference.putFile(conceptData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        uploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddConceptActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("CLASS_FIVE_CONCEPT").push().getKey();
        HashMap data = new HashMap();
        data.put("ConceptTitle", title);
        data.put("ConceptUrl", downloadUrl);


        databaseReference.child("CLASS_FIVE_CONCEPT")
                .child(uniqueKey).setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(AddConceptActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        conceptTitle.setText("");
                        //deploy notification
                        PushNotification notification = new PushNotification(new NotificationData("Prom High School (H.S)","Prom High School (H.S) Class Five Concept Uploaded"),TOPIC);
                        sendNotification(notification, AddConceptActivity.this);
                        //deploy notification
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddConceptActivity.this, "Filed to Upload Pdf", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK){
            conceptData = data.getData();
            if (conceptData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = AddConceptActivity.this.getContentResolver().query(conceptData,null,null,null);
                    if (cursor!=null && cursor.moveToFirst()){
                        conceptName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (conceptData.toString().startsWith("file://")){
                conceptName = new File(conceptData.toString()).getName();
            }
            conceptTextView.setText(conceptName);

        }
    }
}