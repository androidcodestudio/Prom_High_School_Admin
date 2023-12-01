package com.androidcodestudio.promhsadmin.AdminClassFiveSet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightPojo;
import com.androidcodestudio.promhsadmin.AdminClassEleven.AdminClassElevenPojo;
import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFivePojo;
import com.androidcodestudio.promhsadmin.AdminClassNine.AdminClassNinePojo;
import com.androidcodestudio.promhsadmin.AdminClassSeven.AdminClassSevenPojo;
import com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixPojo;
import com.androidcodestudio.promhsadmin.AdminClassTen.AdminClassTenPojo;
import com.androidcodestudio.promhsadmin.AdminClassTwelve.AdminClassTwelvePojo;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import static com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightAdapter.adminClassEightPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassEleven.AdminClassElevenAdapter.adminClassElevenPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFiveAdapter.adminClassFivePojoList;
import static com.androidcodestudio.promhsadmin.AdminClassNine.AdminClassNineAdapter.adminClassNinePojoList;
import static com.androidcodestudio.promhsadmin.AdminClassSeven.AdminClassSevenAdapter.adminClassSevenPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixAdapter.adminClassSixPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassTen.AdminClassTenAdapter.adminClassTenPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassTwelve.AdminClassTwelveAdapter.adminClassTwelvePojoList;

public class AdminMockTesFragment extends Fragment {

    private GridView set_item_grid_view;
    private Dialog loadingDialog;
    private AdminClassFiveSetGridAdapter gridAdapter;
    private FloatingActionButton fab;
    public static Dialog addmocktest_dialog;
    private EditText _categoryName;
    private TextView _className;
    //private List<AdminClassFivePojo> adminClassFivePojoList;
    String _ClassName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_mock_tes, container, false);
        set_item_grid_view = view.findViewById(R.id.set_item_grid_view);

        _ClassName = getActivity().getIntent().getStringExtra("className");

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        addmocktest_dialog = new Dialog(getContext());
        addmocktest_dialog.setContentView(R.layout.add_mocktest_dialog);
        addmocktest_dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.button));
        addmocktest_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addmocktest_dialog.setCancelable(true);


        _categoryName = addmocktest_dialog.findViewById(R.id.edit_text);
        _className = view.findViewById(R.id.textView8);
        _className.setText("Create "+getActivity().getIntent().getStringExtra("title")+" MockTest");

        Button _addBtn = addmocktest_dialog.findViewById(R.id.add_button);

        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_categoryName.getText().toString().isEmpty()) {
                    _categoryName.setError("Required");
                    return;
                }

                switch (_ClassName) {
                    case "ClassFiveCategories":

                        for (AdminClassFivePojo pojo : adminClassFivePojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }

                        break;
                    case "ClassSixCategories":
                        for (AdminClassSixPojo pojo : adminClassSixPojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }


                        break;
                    case "ClassSevenCategories":

                        for (AdminClassSevenPojo pojo : adminClassSevenPojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }

                        break;
                    case "ClassEightCategories":

                        for (AdminClassEightPojo pojo : adminClassEightPojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }
                        break;
                    case "ClassNineCategories":

                        for (AdminClassNinePojo pojo : adminClassNinePojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }

                        break;
                    case "ClassTenCategories":

                        for (AdminClassTenPojo pojo : adminClassTenPojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }

                        break;
                    case "ClassElevenCategories":

                        for (AdminClassElevenPojo pojo : adminClassElevenPojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }

                        break;
                    case "ClassTwelveCategories":

                        for (AdminClassTwelvePojo pojo : adminClassTwelvePojoList) {
                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
                                return;
                            }
                        }

                        break;
                }


                addmocktest_dialog.dismiss();
                loadingDialog.show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference().child(getActivity().getIntent().getStringExtra("className"))
                        .child(getActivity().getIntent().getStringExtra("key"))
                        .child("set").setValue(Integer.valueOf(_categoryName.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    gridAdapter = new AdminClassFiveSetGridAdapter(Integer.parseInt(_categoryName.getText().toString()), getActivity().getIntent().getStringExtra("title"),null);
                                    gridAdapter.notifyDataSetChanged();
                                    set_item_grid_view.setAdapter(gridAdapter);
                                    Toast.makeText(getContext(), "Your Data Successful Update", Toast.LENGTH_SHORT).show();
                                    _categoryName.setText(null);
                                }else{
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
            }
        });


        fab = view.findViewById(R.id.fab_Create_mockTest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addmocktest_dialog.show();
            }
        });

        gridAdapter = new AdminClassFiveSetGridAdapter(getActivity().getIntent().getIntExtra("sets", 0)
                ,getActivity().getIntent().getStringExtra("title")
                , new AdminClassFiveSetGridAdapter.GridListner() {
            @Override
            public void addSet() {
                addmocktest_dialog.show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference()
                        .child(getActivity().getIntent().getStringExtra("className"))
                        .child(getActivity().getIntent().getStringExtra("key"))
                        .child("set")
                        .setValue(getActivity().getIntent().getIntExtra("sets", 0)+1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    gridAdapter.sets++;
                                    gridAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Your Data Successful Update", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });

            }
        });
        set_item_grid_view.setAdapter(gridAdapter);
        return view;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        gridAdapter.notifyDataSetChanged();
    }
}