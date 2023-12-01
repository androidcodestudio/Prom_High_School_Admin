package com.androidcodestudio.promhsadmin.AdminClassTenQuestion;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.androidcodestudio.promhsadmin.R;

public class AdminClassTenAddQuestionActivity extends AppCompatActivity {

    private EditText question;
    private RadioGroup options;
    private LinearLayout answers;
    private Button uploadBtn;
    private String categoryName;
    private int setNo,position;
    private Dialog loadingDialog;
    private AdminClassTenQuestionPojo adminClassTenQuestionPojo;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_ten_add_question);
    }
}