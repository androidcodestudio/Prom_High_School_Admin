package com.androidcodestudio.promhsadmin.bookmark;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.quiestion.Question_Pojo;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bookmarks =findViewById(R.id.bookmark);

        Toolbar toolbar = findViewById(R.id.bookmarkBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        bookmarks.setLayoutManager(linearLayoutManager);

        List<Question_Pojo> question_pojos = new ArrayList<>();
        question_pojos.add(new Question_Pojo("What is capital of India","Delhi","Kolkata","Punjab","Bihar","Delhi",0));
        question_pojos.add(new Question_Pojo("What is capital of India","Delhi","Kolkata","Punjab","Bihar","Delhi",0));
        question_pojos.add(new Question_Pojo("What is capital of India","Delhi","Kolkata","Punjab","Bihar","Delhi",0));
        question_pojos.add(new Question_Pojo("What is capital of India","Delhi","Kolkata","Punjab","Bihar","Delhi",0));
        question_pojos.add(new Question_Pojo("What is capital of India","Delhi","Kolkata","Punjab","Bihar","Delhi",0));

        BookmarkAdapter bookmarkAdapter  = new BookmarkAdapter(question_pojos);
        bookmarks.setAdapter(bookmarkAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}