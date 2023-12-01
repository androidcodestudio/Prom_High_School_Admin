package com.androidcodestudio.promhsadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidcodestudio.promhsadmin.AdminFaculty.AdminFacultyActivity;
import com.androidcodestudio.promhsadmin.AdminNotice.AdminNoticeActivity;
import com.androidcodestudio.promhsadmin.AdminNotice.DeleteNoticeActivity;


public class AdminPortalActivity extends AppCompatActivity {

  private  CardView adNotice,addGalleryImage,addEbook,addFaculty,deleteNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        adNotice = findViewById(R.id.addNotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        addEbook = findViewById(R.id.addEbook);
        addFaculty = findViewById(R.id.addFaculty);
        deleteNotice = findViewById(R.id.deleteNotice);

        adNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeIntent = new Intent(AdminPortalActivity.this, AdminNoticeActivity.class);
                startActivity(noticeIntent);

            }
        });

        addGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeIntent = new Intent(AdminPortalActivity.this, AdminGalleryActivity.class);
                startActivity(noticeIntent);

            }
        });

        addEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeIntent = new Intent(AdminPortalActivity.this, UploadPdfActivity.class);
                startActivity(noticeIntent);

            }
        });

        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeIntent = new Intent(AdminPortalActivity.this, AdminFacultyActivity.class);
                startActivity(noticeIntent);

            }
        });

        deleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeIntent = new Intent(AdminPortalActivity.this, DeleteNoticeActivity.class);
                startActivity(noticeIntent);

            }
        });

    }
}