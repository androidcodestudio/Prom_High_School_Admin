package com.androidcodestudio.promhsadmin.AdminClassFiveSet;

import static com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightAdapter.adminClassEightPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassEleven.AdminClassElevenAdapter.adminClassElevenPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFiveAdapter.adminClassFivePojoList;
import static com.androidcodestudio.promhsadmin.AdminClassFiveSet.AdminMockTesFragment.addmocktest_dialog;
import static com.androidcodestudio.promhsadmin.AdminClassNine.AdminClassNineAdapter.adminClassNinePojoList;
import static com.androidcodestudio.promhsadmin.AdminClassSeven.AdminClassSevenAdapter.adminClassSevenPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixAdapter.adminClassSixPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassTen.AdminClassTenAdapter.adminClassTenPojoList;
import static com.androidcodestudio.promhsadmin.AdminClassTwelve.AdminClassTwelveAdapter.adminClassTwelvePojoList;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightPojo;
import com.androidcodestudio.promhsadmin.AdminClassEleven.AdminClassElevenPojo;
import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFivePojo;
import com.androidcodestudio.promhsadmin.AdminClassFiveQuestion.AdminClassFiveQuestionActivity;
import com.androidcodestudio.promhsadmin.AdminClassNine.AdminClassNinePojo;
import com.androidcodestudio.promhsadmin.AdminClassSeven.AdminClassSevenPojo;
import com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixPojo;
import com.androidcodestudio.promhsadmin.AdminClassTen.AdminClassTenPojo;
import com.androidcodestudio.promhsadmin.AdminClassTwelve.AdminClassTwelvePojo;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class AdminClassFiveSetGridAdapter extends BaseAdapter {
    public int sets = 0;
    private String category;
    private GridListner gridListner;

    private String _WhichClassSet;
    private EditText _categoryName;


    public AdminClassFiveSetGridAdapter(int sets, String category, GridListner gridListner) {
        this.sets = sets;
        this.category = category;
        this.gridListner = gridListner;
    }

    @Override
    public int getCount() {
        return sets+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        //For Add Subject
        final Dialog classDialog = new Dialog(parent.getContext());
        classDialog.setContentView(R.layout.which_class_set);
        classDialog.getWindow().setBackgroundDrawable(parent.getContext().getDrawable(R.drawable.button));
        classDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        classDialog.setCancelable(true);

//        //for add mock test
//        final Dialog category_dialog = new Dialog(parent.getContext());
//        category_dialog.setContentView(R.layout.add_mocktest_dialog);
//        category_dialog.getWindow().setBackgroundDrawable(parent.getContext().getDrawable(R.drawable.button));
//        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        category_dialog.setCancelable(true);
//
//
//        _categoryName = category_dialog.findViewById(R.id.edit_text);
//        Button _addMockTest = category_dialog.findViewById(R.id.add_button);
//
//        _addMockTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (_categoryName.getText().toString().isEmpty()) {
//                    _categoryName.setError("Required");
//                    return;
//                }
//
//                switch (_ClassName) {
//                    case "ClassFiveCategories":
//                        for (AdminClassFivePojo pojo : adminClassFivePojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//
//                        break;
//                    case "ClassSixCategories":
//                        for (AdminClassSixPojo pojo : adminClassSixPojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//
//
//                        break;
//                    case "ClassSevenCategories":
//
//                        for (AdminClassSevenPojo pojo : adminClassSevenPojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//
//                        break;
//                    case "ClassEightCategories":
//
//                        for (AdminClassEightPojo pojo : adminClassEightPojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//                        break;
//                    case "ClassNineCategories":
//
//                        for (AdminClassNinePojo pojo : adminClassNinePojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//
//                        break;
//                    case "ClassTenCategories":
//
//                        for (AdminClassTenPojo pojo : adminClassTenPojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//
//                        break;
//                    case "ClassElevenCategories":
//
//                        for (AdminClassElevenPojo pojo : adminClassElevenPojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//
//                        break;
//                    case "ClassTwelveCategories":
//
//                        for (AdminClassTwelvePojo pojo : adminClassTwelvePojoList) {
//                            if (Integer.valueOf(_categoryName.getText().toString()).equals(pojo.getSet())) {
//                                _categoryName.setError("MockTest" + _categoryName.getText().toString() + "Already Present!");
//                                return;
//                            }
//                        }
//
//                        break;
//                }
//
//
//                category_dialog.dismiss();
//                loadingDialog.show();
//
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                database.getReference().child(getActivity().getIntent().getStringExtra("className"))
//                        .child(getActivity().getIntent().getStringExtra("key"))
//                        .child("set").setValue(Integer.valueOf(_categoryName.getText().toString()))
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()){
//                                    gridAdapter = new AdminClassFiveSetGridAdapter(Integer.parseInt(_categoryName.getText().toString()), getActivity().getIntent().getStringExtra("title"),null);
//                                    gridAdapter.notifyDataSetChanged();
//                                    set_item_grid_view.setAdapter(gridAdapter);
//                                    Toast.makeText(getContext(), "Your Data Successful Update", Toast.LENGTH_SHORT).show();
//                                    _categoryName.setText(null);
//                                }else{
//                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//                                }
//                                loadingDialog.dismiss();
//                            }
//                        });
//            }
//        });

       Spinner _class = classDialog.findViewById(R.id.classname);
       Button _addBtn = classDialog.findViewById(R.id.add_button);

        String[] item = new String[]{"Select Set","CLASS_FIVE_SETS","CLASS_SIX_SETS","CLASS_SEVEN_SETS","CLASS_EIGHT_SETS","CLASS_NINE_SETS","CLASS_TEN_SETS","CLASS_ELEVEN_SETS","CLASS_TWELVE_SETS"};
        _class.setAdapter(new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item,item));

        _class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _WhichClassSet = _class.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_WhichClassSet.equals("Select Set")) {
                    Toast.makeText(parent.getContext(), "Please Select Set", Toast.LENGTH_SHORT).show();

                }else {
                    Intent questionintent = new Intent(parent.getContext(), AdminClassFiveQuestionActivity.class);
                    questionintent.putExtra("category",category);
                    questionintent.putExtra("setName",_WhichClassSet);
                    questionintent.putExtra("setNo",position);
                    parent.getContext().startActivity(questionintent);
                }
            }
        });



        View view;
        if(convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item,parent,false);
        }else
        {
            view = convertView;
        }

        if (position == 0){
            //classDialog.show();

            ((TextView)view.findViewById(R.id.set_number)).setText(category+" Add Mock Test"+" +");
//            ((TextView)view.findViewById(R.id.attempt)).setText("Add Mock Test");


        }else{
            ((TextView)view.findViewById(R.id.set_number)).setText(category+" Mock Test "+String.valueOf(position));

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){
                    //add code
                    //gridListner.addSet();
                    addmocktest_dialog.show();
                }else{
                    classDialog.show();

                }

            }
        });

        //((TextView)view.findViewById(R.id.set_number)).setText(String.valueOf(position+1));
        return view;
    }
    public interface GridListner{
        public void addSet();
    }
}
