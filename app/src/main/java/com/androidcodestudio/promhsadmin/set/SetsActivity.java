package com.androidcodestudio.promhsadmin.set;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetsActivity extends AppCompatActivity {

    private GridView set_item_grid_view;
    private Window _window;
    private Toolbar toolbar;
    TextView name_tv,roll_tv,section_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        set_item_grid_view = findViewById(R.id.set_item_grid_view);

        Toolbar toolbar = findViewById(R.id.titles_bar);
        setSupportActionBar(toolbar);

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        getSupportActionBar().setTitle("title");
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().getThemedContext();

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        name_tv = findViewById(R.id.name);
        roll_tv = findViewById(R.id.roll);
        section_tv = findViewById(R.id.section);

        SetGridAdapter gridAdapter = new SetGridAdapter(
                getIntent().getIntExtra("sets",0), getIntent().getStringExtra("title"));
        set_item_grid_view.setAdapter(gridAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if (item.getItemId()==android.R.id.home){
           finish();
       }
        return super.onOptionsItemSelected(item);
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
                            Toast.makeText(SetsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}