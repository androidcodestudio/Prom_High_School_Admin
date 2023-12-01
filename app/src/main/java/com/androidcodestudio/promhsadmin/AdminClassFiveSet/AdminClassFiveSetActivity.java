package com.androidcodestudio.promhsadmin.AdminClassFiveSet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class AdminClassFiveSetActivity extends AppCompatActivity {

    private GridView set_item_grid_view;
    Dialog loadingDialog;
    AdminClassFiveSetGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_five_set);

        set_item_grid_view = findViewById(R.id.set_item_grid_view);

        Toolbar toolbar = findViewById(R.id.titles_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("title");
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        loadingDialog = new Dialog(AdminClassFiveSetActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        gridAdapter = new AdminClassFiveSetGridAdapter(getIntent().getIntExtra("sets", 0),
                getIntent().getStringExtra("title"), new AdminClassFiveSetGridAdapter.GridListner() {
            @Override
            public void addSet() {
                loadingDialog.show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference().child("ClassFiveCategories")
                        .child(getIntent().getStringExtra("key"))
                        .child("set").setValue(getIntent()
                                .getIntExtra("sets", 0)+1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            gridAdapter.sets++;
                            gridAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(AdminClassFiveSetActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });

            }
        });
        set_item_grid_view.setAdapter(gridAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}