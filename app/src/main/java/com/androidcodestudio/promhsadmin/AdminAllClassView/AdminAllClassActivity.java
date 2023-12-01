package com.androidcodestudio.promhsadmin.AdminAllClassView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys;
import com.androidcodestudio.promhsadmin.MainActivity;
import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import de.hdodenhof.circleimageview.CircleImageView;


public class AdminAllClassActivity extends AppCompatActivity {

    //----------categoryRecyclerView--------
    private RecyclerView _category_recyclerView;
    private List<AdminAllClassPojo> categoryPojoFakeLists = new ArrayList<>();
    private AdminAllClassAdapter _adminAllClassAdapter;
    //----------categoryRecyclerView--------

    private SwipeRefreshLayout swipeRefreshLayout;
    private Window _window;
    private Toolbar toolbar;
    private Dialog _loadingDialog;

    private FloatingActionButton fab;
    private Dialog category_dialog,updateDialog;
    private Spinner _class,_index;
    private String _className,_indexNames;
    private int _indexName;
    private Button _addBtn;
    private Uri image;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;

    private TextInputEditText updateBackgroundColor;
    private CircleImageView _profile_img;

    private ImageView addImage;



//    @SuppressLint("NotifyDataSetChanged")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_all_class);
//
//        progressDialog = new ProgressDialog(this);
//        db = FirebaseFirestore.getInstance();
//
//        toolbar = findViewById(R.id.toolbar);
//        swipeRefreshLayout = findViewById(R.id.refresh_layout);
//
//        fab = findViewById(R.id.add_class);
//
//
//        //loading dialog
//        _loadingDialog = new Dialog(this);
//        _loadingDialog.setContentView(R.layout.loading);
//        _loadingDialog.setCancelable(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            _loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
//        }
//        _loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        _loadingDialog.dismiss();
//        //loading dialog
//
//        _window=this.getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
//        }
//        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                category_dialog.show();
//            }
//        });
//
//        //For Update
//        updateDialog = new Dialog(this);
//        updateDialog.setContentView(R.layout.update_classinfo);
//        updateDialog.getWindow().setBackgroundDrawable(AdminAllClassActivity.this.getDrawable(R.drawable.button));
//        updateDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        updateDialog.setCancelable(true);
//
//        addImage = updateDialog.findViewById(R.id.editPhotoIcon);
//        _profile_img = updateDialog.findViewById(R.id.class_icon);
//        updateBackgroundColor = updateDialog.findViewById(R.id.update_bg);
//        _class = updateDialog.findViewById(R.id.classname);
//        _index = updateDialog.findViewById(R.id.indexing);
//        _addBtn = updateDialog.findViewById(R.id.add_button);
//
//
//        //For Add Subject
//        category_dialog = new Dialog(this);
//        category_dialog.setContentView(R.layout.add_class);
//        category_dialog.getWindow().setBackgroundDrawable(AdminAllClassActivity.this.getDrawable(R.drawable.button));
//        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        category_dialog.setCancelable(true);
//
//        _class = category_dialog.findViewById(R.id.classname);
//        _index = category_dialog.findViewById(R.id.indexing);
//        _addBtn = category_dialog.findViewById(R.id.add_button);
//
//        String[] item = new String[]{"Select Class","V","Vi","Vii","Viii","IX","X","Xi","Xii"};
//        _class.setAdapter(new ArrayAdapter<String>(AdminAllClassActivity.this, android.R.layout.simple_spinner_dropdown_item,item));
//
//        _class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                _className = _class.getSelectedItem().toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        String[] items = new String[]{"Select Index","1","2","3","4","5","6","7","8"};
//        _index.setAdapter(new ArrayAdapter<String>(AdminAllClassActivity.this, android.R.layout.simple_spinner_dropdown_item,items));
//
//        _index.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                _indexNames = _index.getSelectedItem().toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//
//        _addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (_className.equals("Select Class")) {
//                    Toast.makeText(AdminAllClassActivity.this, "Please Select Class", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    return;
//                }
//
//                for(AdminAllClassPojo pojo :categorysPojoList){
//                    if (_className.equals(pojo.getCategoryName())){
//                        Toast.makeText(AdminAllClassActivity.this, "Class Name Already Present!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                }
//
//                if (_indexNames.equals("Select Index")) {
//                    Toast.makeText(AdminAllClassActivity.this, "Please Select Index", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    return;
//                }
//
//                for(AdminAllClassPojo pojo :categorysPojoList){
//                    if (_indexNames.equals(String.valueOf(pojo.getIndex()))){
//                        Toast.makeText(AdminAllClassActivity.this, "Index Already Present!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                }
//                //upload
//                category_dialog.dismiss();
//                UploadData();
//            }
//        });
//        //For Add Subject
//
//        _category_recyclerView = (RecyclerView)findViewById(R.id.categari_recycler_view);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
//        _category_recyclerView.setLayoutManager(gridLayoutManager);
//
//
//        //category Fake List----------------------------------
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//        categoryPojoFakeLists.add(new AdminAllClassPojo("    ","","",0,""));
//
//        if (categorysPojoList.size() == 0){
//            loadCategoriess(_category_recyclerView, AdminAllClassActivity.this);
//        }else{
//            _adminAllClassAdapter = new AdminAllClassAdapter(categorysPojoList, new AdminAllClassAdapter.DeleteListener() {
//                @Override
//                public void onDelete(String key, int position) {
//                    new AlertDialog.Builder(AdminAllClassActivity.this)
//                            .setTitle("Delete Class")
//                            .setMessage("Are you sure,you want to Delete this Class")
//                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    db.collection("PROM_HIGH_SCHOOL_ADMIN_ALL_CLASS_ICONS")
//                                            .document(key)
//                                            .delete()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()){
//                                                        categorysPojoList.remove(position);
//                                                        _adminAllClassAdapter.notifyDataSetChanged();
//                                                    }else{
//                                                        Toast.makeText(AdminAllClassActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();
//                                                    }
//
//                                                }
//                                            });
//
//                                }
//                            }).setNegativeButton("Update", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    updateDialog.show();
//
//                                    _addBtn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//
//                                            if (_className.equals("Select Class")) {
//                                                Toast.makeText(AdminAllClassActivity.this, "Please Select Class", Toast.LENGTH_SHORT).show();
//                                                progressDialog.dismiss();
//                                                return;
//                                            }
//
//                                            for(AdminAllClassPojo pojo :categorysPojoList){
//                                                if (_className.equals(pojo.getCategoryName())){
//                                                    Toast.makeText(AdminAllClassActivity.this, "Class Name Already Present!", Toast.LENGTH_SHORT).show();
//                                                    return;
//                                                }
//
//                                            }
//
//                                            if (_indexNames.equals("Select Index")) {
//                                                Toast.makeText(AdminAllClassActivity.this, "Please Select Index", Toast.LENGTH_SHORT).show();
//                                                progressDialog.dismiss();
//                                                return;
//                                            }
//
//                                            for(AdminAllClassPojo pojo :categorysPojoList){
//                                                if (_indexNames.equals(String.valueOf(pojo.getIndex()))){
//                                                    Toast.makeText(AdminAllClassActivity.this, "Index Already Present!", Toast.LENGTH_SHORT).show();
//                                                    return;
//                                                }
//
//                                            }
//                                            //upload
//                                            updateDialog.dismiss();
//                                            Map<String, Object> user = new HashMap<>();
//                                            user.put("background_color", "#5DADE2");
//                                            user.put("categoryIconLink", "https://firebasestorage.googleapis.com/v0/b/comexam-e9453.appspot.com/o/Fotor_167268186502440.jpg?alt=media&token=c042c139-f1af-403e-a56a-e7c4089e6cdd");
//                                            user.put("categoryName", _className);
//                                            user.put("index", Integer.parseInt(_indexNames));
//
//                                            // Add a new document with a generated ID
//                                            db.collection("PROM_HIGH_SCHOOL_ADMIN_ALL_CLASS_ICONS")
//                                                    .document("R0pU9z0g6JarvRXLgXTM")
//                                                    .update(user)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                swipeRefreshLayout.setRefreshing(true);
//                                                                reloadPage();
//
//                                                                loadCategoriess(_category_recyclerView, AdminAllClassActivity.this);
//                                                                _adminAllClassAdapter = new AdminAllClassAdapter(categorysPojoList,null);
//                                                                _adminAllClassAdapter.notifyDataSetChanged();
//                                                                _category_recyclerView.setAdapter(_adminAllClassAdapter);
//                                                                Toast.makeText(AdminAllClassActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                                            } else {
//                                                                String error = task.getException().getMessage();
//                                                                Toast.makeText(AdminAllClassActivity.this, error, Toast.LENGTH_SHORT).show();
//                                                                Toast.makeText(AdminAllClassActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                                                //_loadingDialog.dismiss();
//                                                            }
//                                                        }
//                                                    });
//
//                                        }
//                                    });
//                                    // todo update
//
//
//                                }
//                            })
//                            .setIcon(R.drawable.ic_baseline_warning_amber_24)
//                            .show();
//                }
//            });
//            _adminAllClassAdapter.notifyDataSetChanged();
//        }
//        _category_recyclerView.setAdapter(_adminAllClassAdapter);
//
//        //Category----------------
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                reloadPage();
//
//                loadCategoriess(_category_recyclerView, AdminAllClassActivity.this);
//                _adminAllClassAdapter = new AdminAllClassAdapter(categorysPojoList,null);
//                _adminAllClassAdapter.notifyDataSetChanged();
//                _category_recyclerView.setAdapter(_adminAllClassAdapter);
//            }
//        });
//    }
//
//    private void UploadData() {
//        Map<String, Object> user = new HashMap<>();
//        user.put("background_color", "#5DADE2");
//        user.put("categoryIconLink", "https://firebasestorage.googleapis.com/v0/b/comexam-e9453.appspot.com/o/Fotor_167268186502440.jpg?alt=media&token=c042c139-f1af-403e-a56a-e7c4089e6cdd");
//        user.put("categoryName", _className);
//        user.put("index", Integer.parseInt(_indexNames));
//
//        // Add a new document with a generated ID
//        db.collection("PROM_HIGH_SCHOOL_ADMIN_ALL_CLASS_ICONS")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        swipeRefreshLayout.setRefreshing(true);
//                        reloadPage();
//
//                        loadCategoriess(_category_recyclerView, AdminAllClassActivity.this);
//                        _adminAllClassAdapter = new AdminAllClassAdapter(categorysPojoList,null);
//                        _adminAllClassAdapter.notifyDataSetChanged();
//                        _category_recyclerView.setAdapter(_adminAllClassAdapter);
//                        Toast.makeText(AdminAllClassActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AdminAllClassActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
//
//    private void reloadPage() {
//        swipeRefreshLayout.setColorSchemeColors(AdminAllClassActivity.this.getResources().getColor(R.color.colorPrimary), AdminAllClassActivity.this.getResources().getColor(R.color.colorAccent), AdminAllClassActivity.this.getResources().getColor(R.color.colorPrimaryDark));
//        DataBaseQuerys.clearData();
//        swipeRefreshLayout.setRefreshing(false);
//
//    }


}
