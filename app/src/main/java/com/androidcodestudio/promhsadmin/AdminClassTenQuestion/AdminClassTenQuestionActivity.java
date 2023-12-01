package com.androidcodestudio.promhsadmin.AdminClassTenQuestion;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class AdminClassTenQuestionActivity extends AppCompatActivity {
    private Button _add_btn, _excel;
    private RecyclerView _recyclerView;
    private AdminClassTenQuestionAdapter _adepter;
    public static List<AdminClassTenQuestionPojo> list;
    private Dialog loadingDialog;
    private DatabaseReference myRef;
    public static final int CELL_COUNT = 6;
    private int set;
    private String categoryName;
    //private TextView loadingText;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_ten_question);


    }

}