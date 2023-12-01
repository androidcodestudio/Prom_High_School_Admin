package com.androidcodestudio.promhsadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys;
import com.androidcodestudio.promhsadmin.category.Categories_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private TextView total;
    private Button done_btn;
    ProgressDialog progressDialog;


    TextView name_tv,roll_tv,section_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        //Restrict user to taking screenshot/screenRecord
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        //Restrict user to taking screenshot/screenRecord

//        final Dialog loadingDialog = new Dialog(this);
//        loadingDialog.setContentView(R.layout.loading);
//        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        loadingDialog.setCancelable(false);


        Toolbar toolbar = findViewById(R.id.score_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        done_btn=findViewById(R.id.done_btn);
        total=findViewById(R.id.total_number);
        score=findViewById(R.id.main_score_tv);

        name_tv = findViewById(R.id.name);
        roll_tv = findViewById(R.id.roll);
        section_tv = findViewById(R.id.section);

        score.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        total.setText(String.valueOf(getIntent().getIntExtra("total",0)));

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScoreActivity.this, Categories_Activity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //DataBaseQuerys.phone = task.getResult().getString("phone");
                            DataBaseQuerys.Name = task.getResult().getString("Name");
                            DataBaseQuerys.roll = task.getResult().getString("Roll");
                            DataBaseQuerys.section = task.getResult().getString("Section");

                            //_user_phone.setText(DataBaseQuerys.phone);
                            name_tv.setText(String.format(DataBaseQuerys.Name));
                            roll_tv.setText(String.format(DataBaseQuerys.roll));
                            section_tv.setText(String.format(DataBaseQuerys.section));

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ScoreActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}