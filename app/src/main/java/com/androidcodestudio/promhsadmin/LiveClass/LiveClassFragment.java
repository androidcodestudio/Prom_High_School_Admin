package com.androidcodestudio.promhsadmin.LiveClass;



import static com.androidcodestudio.promhsadmin.Constant.TOPIC;
import static com.androidcodestudio.promhsadmin.Constant.sendNotification;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightActivity;
import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.model.NotificationData;
import com.androidcodestudio.promhsadmin.model.PushNotification;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class LiveClassFragment extends Fragment {
    private RecyclerView ChapterRecyclerView;
    private DatabaseReference reference;
    private List<LiveClassPojo> list;
    private LiveClassAdapter adapter;
    private ShimmerFrameLayout container;
    private LinearLayout shimmer_layout;
    private FloatingActionButton fab;
    private Dialog category_dialog;
    private EditText _categoryName,_upcomingDate;
    private Button _addBtn,_updateBtn;
    private LottieAnimationView _empty_chapter;
    private TextView no_chapter;
    String _subjectName;
    private TextView _upcomingText;


    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_live_class, container, false);
        _subjectName = requireActivity().getIntent().getStringExtra("title");

        progressDialog = new ProgressDialog(getContext());

        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        ChapterRecyclerView = view.findViewById(R.id.chapter_item_recycler_view);
        //swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        _empty_chapter = (LottieAnimationView) view.findViewById(R.id.animation);
        no_chapter = view.findViewById(R.id.no_order_text);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        _upcomingText = view.findViewById(R.id.upcomingText);
        fab = view.findViewById(R.id.fab_live);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_dialog.show();
                //startActivity(new Intent(getContext(), AdminAddChapterActivity.class));
            }
        });

        _upcomingText.setText("Create "+_subjectName+" Upcoming Classes");

        category_dialog = new Dialog(getContext());
        category_dialog.setContentView(R.layout.add_upcoming_dialog);
        category_dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.button));
        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        category_dialog.setCancelable(true);

        _categoryName = category_dialog.findViewById(R.id.edit_text);
        _addBtn = category_dialog.findViewById(R.id.add_button);
        _updateBtn = category_dialog.findViewById(R.id.update_button);

        btnDatePicker=category_dialog.findViewById(R.id.btn_date);
        btnTimePicker=category_dialog.findViewById(R.id.btn_time);
        txtDate=category_dialog.findViewById(R.id.in_date);
        txtTime=category_dialog.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });



        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_categoryName.getText().toString().isEmpty()) {
                    _categoryName.setError("Required");
                    return;
                }

                if (txtDate.getText().toString().isEmpty()) {
                    txtDate.setError("Required");
                    return;
                }

                if (txtTime.getText().toString().isEmpty()) {
                    txtTime.setError("Required");
                    return;
                }
                for(LiveClassPojo pojo :list){
                    if (_categoryName.getText().toString().equals(pojo.getUpcomingClassName())){
                        _categoryName.setError("Category Name Already Present!");
                        return;
                    }
                }
                category_dialog.dismiss();
                UploadCategoryName();
            }
        });

        list = new ArrayList<>();
        ChapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LiveClassAdapter(getContext(), list, new LiveClassAdapter.DeleteListener() {
            @Override
            public void onDelete(String key, int position) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Chapter")
                        .setMessage("Are you sure,you want to Delete This Upcoming Class")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child("UpcomingNote")
                                        .child(_subjectName)
                                        .child(list.get(position).getKey())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    list.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(getContext(), "fail to remove", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }).setNegativeButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              //todo
                                _addBtn.setVisibility(View.GONE);
                                progressDialog.setMessage("Updating...");
                                progressDialog.show();
                                category_dialog.show();
                                _updateBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        _categoryName.setText(list.get(position).getUpcomingClassName());
                                        txtDate.setText(list.get(position).getDate());
                                        txtTime.setText(list.get(position).getTime());
                                        if (_categoryName.getText().toString().isEmpty()) {
                                            _categoryName.setError("Required");
                                            return;
                                        }
                                        if (txtDate.getText().toString().isEmpty()) {
                                            txtDate.setError("Required");
                                            return;
                                        }
                                        if (txtTime.getText().toString().isEmpty()) {
                                            txtTime.setError("Required");
                                            return;
                                        }
                                        for(LiveClassPojo pojo :list){
                                            if (_categoryName.getText().toString().equals(pojo.getUpcomingClassName())){
                                                _categoryName.setError("Category Name Already Present!");
                                                return;
                                            }
                                        }
                                        category_dialog.dismiss();
                                        HashMap map = new HashMap();
                                        map.put("UpcomingClassName",_categoryName.getText().toString());
                                        map.put("Date",txtDate.getText().toString());
                                        map.put("Time",txtTime.getText().toString());

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        database.getReference()
                                                .child("UpcomingNote")
                                                .child(_subjectName)
                                                .child(list.get(position).getKey())
                                                .updateChildren(map)
                                                .addOnSuccessListener(new OnSuccessListener() {
                                                    @SuppressLint("NotifyDataSetChanged")
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        progressDialog.dismiss();
                                                        category_dialog.dismiss();
                                                        Toast.makeText(getContext(), "Updated Successful", Toast.LENGTH_SHORT).show();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });

                            }
                        })
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        ChapterRecyclerView.setAdapter(adapter);

        myRef.child("UpcomingNote")
                .child(_subjectName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot Snapshot : snapshot.getChildren()){
                            list.add(new LiveClassPojo(
                                     Snapshot.child("UpcomingClassName").getValue().toString()
                                    ,Snapshot.child("Date").getValue().toString()
                                    ,Snapshot.child("Time").getValue().toString()
                                    ,Snapshot.getKey()));
                        }
                        //adapter.notifyDataSetChanged();
//                        if (adapter != null){
                        if (list.size() == 0){
                            _empty_chapter.setVisibility(View.VISIBLE);
                            ChapterRecyclerView.setVisibility(View.GONE);
                            no_chapter.setVisibility(View.VISIBLE);
                            //location_dialog.dismiss();
                        }else{
                            _empty_chapter.setVisibility(View.GONE);
                            no_chapter.setVisibility(View.GONE);
                            ChapterRecyclerView.setVisibility(View.VISIBLE);

                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    private void UploadCategoryName() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = databaseReference.child("UpcomingNote");
        final String uniqueKey =  dbRef.push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put("UpcomingClassName", _categoryName.getText().toString());
        map.put("Date", txtDate.getText().toString());
        map.put("Time", txtTime.getText().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference()
                .child("UpcomingNote")
                .child(_subjectName)
                .child(uniqueKey)
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            list.add(new LiveClassPojo(_categoryName.getText().toString(),txtDate.getText().toString(),txtTime.getText().toString(),uniqueKey));
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            //deploy notification
                            PushNotification notification = new PushNotification(new NotificationData("One Vision ","Uploaded a New Upcoming Classes"),TOPIC);
                            sendNotification(notification,getContext());
                            //deploy notification
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}