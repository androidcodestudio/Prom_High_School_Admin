package com.androidcodestudio.promhsadmin.AdminClassFiveOnlineClass;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.UUID;

public class AdminClassFiveAddOnlineClassActivity extends AppCompatActivity {

    private EditText _concept,_url;
    private Button uploadBtn;
    private String categoryName;
    private int setNo,position;
    private Dialog loadingDialog;
    private ClassFiveOnlineClassePojo adminClassFiveOnlineClassePojo;
    private String id;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_five_add_online_class);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Concept");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(AdminClassFiveAddOnlineClassActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        _concept = findViewById(R.id.concept);
        _url = findViewById(R.id.url);
        uploadBtn = findViewById(R.id.button);

        categoryName = getIntent().getStringExtra("categoryName");
        setNo = getIntent().getIntExtra("setNo",-1);
        position = getIntent().getIntExtra("position",-1);
        if (setNo==-1){
            finish();
            return;
        }
        if (position != -1){
            adminClassFiveOnlineClassePojo = AdminClassFiveOnlineClassActivity.list.get(position);
            setData();
        }

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_concept.getText().toString().isEmpty()){
                    _concept.setText("Required");
                    return;
                }

                if (_url.getText().toString().isEmpty()){
                    _url.setText("Required");
                    return;
                }
                upLoad();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData(){
        _concept.setText(adminClassFiveOnlineClassePojo.getConcept());
        _url.setText(adminClassFiveOnlineClassePojo.getUrl());
    }

    private void upLoad(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("concept",_concept.getText().toString());
        map.put("url",_url.getText().toString());
        map.put("setNo",setNo);

        if (position!= -1){
            id = adminClassFiveOnlineClassePojo.getId();
        }else{
            id = UUID.randomUUID().toString();
        }

        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child("CLASS_FIVE_CONCEPT")
                .child(categoryName)
                .child("concept")
                .child(id)
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ClassFiveOnlineClassePojo adminQuestionsPojo = new ClassFiveOnlineClassePojo(
                                    id
                                    ,map.get("concept").toString()
                                    ,map.get("url").toString()
                                    ,(int)map.get("setNo"));

                            if (position!=-1){
                                AdminClassFiveOnlineClassActivity.list.set(position,adminQuestionsPojo);
                            }else{
                                AdminClassFiveOnlineClassActivity.list.add(adminQuestionsPojo);
                            }
                            finish();

                        }else{
                            Toast.makeText(AdminClassFiveAddOnlineClassActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();

                    }
                });

    }
}