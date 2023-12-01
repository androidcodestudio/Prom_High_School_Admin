package com.androidcodestudio.promhsadmin.AdminClassEightQuestion;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class AdminClassEightAddQuestionActivity extends AppCompatActivity {

    private EditText question;
    private RadioGroup options;
    private LinearLayout answers;
    private Button uploadBtn;
    private String categoryName;
    private int setNo,position;
    private Dialog loadingDialog;
    private AdminClassEightQuestionPojo adminClassEightQuestionPojo;
    private String id;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_eight_add_question);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Question");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(AdminClassEightAddQuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        question = findViewById(R.id.question);
        options = findViewById(R.id.options);
        answers = findViewById(R.id.answers);
        uploadBtn = findViewById(R.id.button);



        categoryName = getIntent().getStringExtra("categoryName");
        setNo = getIntent().getIntExtra("setNo",-1);
        position = getIntent().getIntExtra("position",-1);
        if (setNo==-1){
            finish();
            return;
        }
        if (position != -1){
            adminClassEightQuestionPojo = AdminClassEightQuestionActivity.list.get(position);
            setData();
        }

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (question.getText().toString().isEmpty()){
                    question.setText("Required");
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
        question.setText(adminClassEightQuestionPojo.getQuestion());
        ((EditText)answers.getChildAt(0)).setText(adminClassEightQuestionPojo.getOp_one());
        ((EditText)answers.getChildAt(1)).setText(adminClassEightQuestionPojo.getOp_two());
        ((EditText)answers.getChildAt(2)).setText(adminClassEightQuestionPojo.getOp_three());
        ((EditText)answers.getChildAt(3)).setText(adminClassEightQuestionPojo.getOp_four());

        for (int i =0; i <answers.getChildCount();i++){
            if (((EditText)answers.getChildAt(i)).getText().toString().equals(adminClassEightQuestionPojo.getCorrect_ans())){
                RadioButton radioButton = (RadioButton) options.getChildAt(i);
                radioButton.setChecked(true);
                break;
            }
        }


    }

    private void upLoad(){
        int correct = -1;
        for (int i = 0; i<options.getChildCount();i++){
            EditText answer = (EditText) answers.getChildAt(i);
            if (answer.getText().toString().isEmpty()){
                answer.setError("Required");
                return;
            }
            RadioButton radioButton = (RadioButton) options.getChildAt(i);
            if (radioButton.isChecked()){
                correct = i;
                break;
            }

        }

        if (correct == -1){
            Toast.makeText(AdminClassEightAddQuestionActivity.this, "Mark The Correct Option", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String,Object> map = new HashMap<>();
        map.put("correct_ans",((EditText)answers.getChildAt(correct)).getText().toString());
        map.put("op_four",((EditText)answers.getChildAt(3)).getText().toString());
        map.put("op_one",((EditText)answers.getChildAt(0)).getText().toString());
        map.put("op_three",((EditText)answers.getChildAt(2)).getText().toString());
        map.put("op_two",((EditText)answers.getChildAt(1)).getText().toString());
        map.put("question",question.getText().toString());
        map.put("setNo",setNo);

        if (position!= -1){
            id = adminClassEightQuestionPojo.getId();
        }else{
            id = UUID.randomUUID().toString();
        }

        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child("CLASS_EIGHT_SETS").child(categoryName)
                .child("questions").child(id)
                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            AdminClassEightQuestionPojo adminQuestionsPojo = new AdminClassEightQuestionPojo(id
                                    ,map.get("question").toString()
                                    ,map.get("op_one").toString()
                                    ,map.get("op_two").toString()
                                    ,map.get("op_three").toString()
                                    ,map.get("op_four").toString()
                                    ,map.get("correct_ans").toString()
                                    ,(int)map.get("setNo"));

                            if (position!=-1){
                                AdminClassEightQuestionActivity.list.set(position,adminQuestionsPojo);
                            }else{
                                AdminClassEightQuestionActivity.list.add(adminQuestionsPojo);
                            }
                            finish();

                        }else{
                            Toast.makeText(AdminClassEightAddQuestionActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();

                    }
                });

    }
}