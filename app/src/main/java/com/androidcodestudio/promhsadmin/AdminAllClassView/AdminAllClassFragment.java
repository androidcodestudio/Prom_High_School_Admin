package com.androidcodestudio.promhsadmin.AdminAllClassView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;


public class AdminAllClassFragment extends Fragment {

    private Window _window;

    //----------categoryRecyclerView--------
    private RecyclerView _category_recyclerView;
    private ArrayList<AdminAllClassPojo> list;
    private AdminAllClassAdapter _adminAllClassAdapter;
    //----------categoryRecyclerView--------
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private Dialog category_dialog;
    private Spinner _class,_index;
    private String _className,_indexNames;
    private String _classNameUpdate,_indexNamesUpdate;

    private Button _addBtn;
    private Uri image;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private Dialog updateDialog;
    private CircleImageView _profile_img;
    private ImageView addImage;
    private TextInputEditText updateBackgroundColor;
    Spinner _classUpdate;
    Spinner _indexUpdate;
    Button _addBtnUpdate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_admin_all_class, container, false);

        _window=getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        }
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        progressDialog = new ProgressDialog(getContext());
        db = FirebaseFirestore.getInstance();

        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        fab = view.findViewById(R.id.add_class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_dialog.show();
            }
        });

        //For Update
        updateDialog = new Dialog(getContext());
        updateDialog.setContentView(R.layout.update_classinfo);
        updateDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.button));
        updateDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        updateDialog.setCancelable(true);

        addImage = updateDialog.findViewById(R.id.editPhotoIcon);
        _profile_img = updateDialog.findViewById(R.id.class_icon);
        updateBackgroundColor = updateDialog.findViewById(R.id.update_bg);
        _classUpdate = updateDialog.findViewById(R.id.classname);
        _indexUpdate = updateDialog.findViewById(R.id.indexing);
        _addBtnUpdate = updateDialog.findViewById(R.id.add_button);

        String[] _classUpdate_item = new String[]{"Select Class","V","Vi","Vii","Viii","IX","X","Xi","Xii"};
        _classUpdate.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,_classUpdate_item));

        _classUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _classNameUpdate = _classUpdate.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] _classUpdate_items = new String[]{"Select Index","1","2","3","4","5","6","7","8"};
        _indexUpdate.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,_classUpdate_items));

        _indexUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                _indexNamesUpdate = _indexUpdate.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //For Update



        //For Add Subject
        category_dialog = new Dialog(getContext());
        category_dialog.setContentView(R.layout.add_class);
        category_dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.button));
        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        category_dialog.setCancelable(true);

        _class = category_dialog.findViewById(R.id.classname);
        _index = category_dialog.findViewById(R.id.indexing);
        _addBtn = category_dialog.findViewById(R.id.add_button);

        String[] item = new String[]{"Select Class","V","Vi","Vii","Viii","IX","X","Xi","Xii"};
        _class.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,item));

        _class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _className = _class.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] items = new String[]{"Select Index","1","2","3","4","5","6","7","8"};
        _index.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,items));

        _index.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                _indexNames = _index.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_className.equals("Select Class")) {
                    Toast.makeText(getContext(), "Please Select Class", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                for(AdminAllClassPojo pojo : list){
                    if (_className.equals(pojo.getCategoryName())){
                        Toast.makeText(getContext(), "Class Name Already Present!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (_indexNames.equals("Select Index")) {
                    Toast.makeText(getContext(), "Please Select Index", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                for(AdminAllClassPojo pojo : list){
                    if (_indexNames.equals(String.valueOf(pojo.getIndex()))){
                        Toast.makeText(getContext(), "Index Already Present!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                //upload
                category_dialog.dismiss();
                UploadData();

            }
        });
        //For Add Subject


        list = new ArrayList<>();
        _category_recyclerView = (RecyclerView)view.findViewById(R.id.categari_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        _category_recyclerView.setLayoutManager(gridLayoutManager);

        _adminAllClassAdapter = new AdminAllClassAdapter(list, new AdminAllClassAdapter.DeleteListener() {
            @Override
            public void onDelete(String key, int position) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Class")
                        .setMessage("Are you sure,you want to Delete this Class")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("PROM_HIGH_SCHOOL_ADMIN_ALL_CLASS_ICONS")
                                        .document(key)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    list.remove(position);
                                                    _adminAllClassAdapter.notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(getContext(), "fail to remove", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                            }
                        }).setNegativeButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        updateDialog.show();
                                        _addBtnUpdate.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                updateBackgroundColor.setText(list.get(position).getBackgroundColor());



                                                if (_classNameUpdate.equals("Select Class")) {
                                                    Toast.makeText(getContext(), "Please Select Class", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    return;
                                                }


                                                if (_indexNamesUpdate.equals("Select Index")) {
                                                    Toast.makeText(getContext(), "Please Select Index", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    return;
                                                }

                                                //update
                                                updateDialog.dismiss();
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("background_color", updateBackgroundColor.getText().toString());
                                                user.put("categoryIconLink", "https://firebasestorage.googleapis.com/v0/b/exam-979e3.appspot.com/o/PromLogo%2FUntitled%20design%20(4).png?alt=media&token=71573317-9f6f-4dcf-a62e-d35e70d6747f");
                                                user.put("categoryName", _classNameUpdate);
                                                user.put("index", Integer.parseInt(_indexNamesUpdate));

                                                // Add a new document with a generated ID
                                                db.collection("PROM_HIGH_SCHOOL_ADMIN_ALL_CLASS_ICONS")
                                                        .document(list.get(position).getKey())
                                                        .update(user)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @SuppressLint("NotifyDataSetChanged")
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    swipeRefreshLayout.setRefreshing(true);
                                                                    _adminAllClassAdapter.notifyDataSetChanged();
                                                                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        });
                                    }
                                }
                        )
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        _category_recyclerView.setAdapter(_adminAllClassAdapter);

        FirebaseFirestore _firebaseFirestore = FirebaseFirestore.getInstance();
        _firebaseFirestore.collection("PROM_HIGH_SCHOOL_ADMIN_ALL_CLASS_ICONS").orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                list.add(new AdminAllClassPojo(
                                        documentSnapshot.get("categoryName").toString()
                                        ,documentSnapshot.get("categoryIconLink").toString()
                                        ,documentSnapshot.get("background_color").toString()
                                        ,Integer.parseInt(documentSnapshot.get("index").toString())
                                        ,documentSnapshot.getId()));
                            }
                            _adminAllClassAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//Category----------------


//        if (categorysPojoList.size() == 0){
//            loadCategoriess(_category_recyclerView, getContext());
//        }else{
//
//            _adminAllClassAdapter.notifyDataSetChanged();
//        }




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshLayout.setRefreshing(false);

            }
        });

       return view;
    }

    private void UploadData() {
        Map<String, Object> user = new HashMap<>();
        user.put("background_color", "#5DADE2");
        user.put("categoryIconLink", "https://firebasestorage.googleapis.com/v0/b/exam-979e3.appspot.com/o/PromLogo%2FUntitled%20design%20(4).png?alt=media&token=71573317-9f6f-4dcf-a62e-d35e70d6747f");
        user.put("categoryName", _className);
        user.put("index", Integer.parseInt(_indexNames));

        // Add a new document with a generated ID
        db.collection("PROM_HIGH_SCHOOL_ADMIN_ALL_CLASS_ICONS")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        swipeRefreshLayout.setRefreshing(true);
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}