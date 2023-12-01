package com.androidcodestudio.promhsadmin.AdminClassSevenQuestion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AdminClassSevenQuestionActivity extends AppCompatActivity {

    private Button _add_btn, _excel;
    private RecyclerView _recyclerView;
    private AdminClassSevenQuestionAdapter _adepter;
    public static List<AdminClassSevenQuestionPojo> list;
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
        setContentView(R.layout.activity_admin_class_seven_question);

        Toolbar toolbar = findViewById(R.id.toolbara);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Question");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        myRef = FirebaseDatabase.getInstance().getReference();

        categoryName = getIntent().getStringExtra("category");
        set = getIntent().getIntExtra("setNo", 1);

        getSupportActionBar().setTitle(categoryName + "/set" + set);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        loadingDialog = new Dialog(AdminClassSevenQuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        //loadingText = loadingDialog.findViewById(R.id.textView);

        _add_btn = findViewById(R.id.add_btn);
        _excel = findViewById(R.id.excel_btn);
        _recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        _recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();

        _adepter = new AdminClassSevenQuestionAdapter(list, categoryName, new AdminClassSevenQuestionAdapter.DeleteListener() {
            @Override
            public void onLongClick(int position, String id) {
                new AlertDialog.Builder(AdminClassSevenQuestionActivity.this)
                        .setTitle("Delete Question")
                        .setMessage("Are you sure,you want to Delete This Question")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child("CLAAS_SEVEN_SETS").child(categoryName).child("question").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            list.remove(position);
                                            _adepter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(AdminClassSevenQuestionActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        _recyclerView.setAdapter(_adepter);

        getData(categoryName, set);

        _add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add Question Activity
                Intent addQuestion = new Intent(AdminClassSevenQuestionActivity.this, AdminClassSevenAddQuestionActivity.class);
                addQuestion.putExtra("categoryName", categoryName);
                addQuestion.putExtra("setNo", set);
                startActivity(addQuestion);
            }
        });

        _excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AdminClassSevenQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFile();
                } else {
                    ActivityCompat.requestPermissions(AdminClassSevenQuestionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

                }

            }
        });
    }
    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 102);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select File"), 102);
            } else {
                Toast.makeText(AdminClassSevenQuestionActivity.this, "allow first", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                String filePath = data.getData().getPath();
                if (filePath.endsWith(".xlsx")) {
                    readFile(data.getData());
                    Toast.makeText(AdminClassSevenQuestionActivity.this, "File Selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminClassSevenQuestionActivity.this, "Chose Another Excel File", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(String categoryName, int set) {
        loadingDialog.show();
        myRef.child("CLASS_SEVEN_SETS").child(categoryName)
                .child("questions").orderByChild("setNo")
                .equalTo(set).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String id = dataSnapshot.getKey();
                            String question = dataSnapshot.child("question").getValue().toString();
                            String a = dataSnapshot.child("op_one").getValue().toString();
                            String b = dataSnapshot.child("op_two").getValue().toString();
                            String c = dataSnapshot.child("op_three").getValue().toString();
                            String d = dataSnapshot.child("op_four").getValue().toString();
                            String correct_ans = dataSnapshot.child("correct_ans").getValue().toString();
                            list.add(new AdminClassSevenQuestionPojo(id, question, a, b, c, d, correct_ans, set));
                        }
                        loadingDialog.dismiss();
                        _adepter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminClassSevenQuestionActivity.this, "error", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        finish();
                    }
                });

    }

    private void readFile(Uri fileUri) {
        //loadingText.setText("Scanning Questions...");
        loadingDialog.show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> parentMap = new HashMap<>();
                List<AdminClassSevenQuestionPojo> templist = new ArrayList<>();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

                    int rowsCount = sheet.getPhysicalNumberOfRows();

                    if (rowsCount > 0) {
                        for (int r = 0; r < rowsCount; r++) {
                            Row row = sheet.getRow(r);

                            if (row.getPhysicalNumberOfCells() == CELL_COUNT) {
                                String question = getCellData(row, 0, formulaEvaluator);
                                String a = getCellData(row, 1, formulaEvaluator);
                                String b = getCellData(row, 2, formulaEvaluator);
                                String c = getCellData(row, 3, formulaEvaluator);
                                String d = getCellData(row, 4, formulaEvaluator);
                                String correct_ans = getCellData(row, 5, formulaEvaluator);

                                if (correct_ans.equals(a) || correct_ans.equals(b) || correct_ans.equals(c) || correct_ans.equals(d)) {
                                    HashMap<String, Object> questionMap = new HashMap<>();
                                    questionMap.put("question", question);
                                    questionMap.put("op_one", a);
                                    questionMap.put("op_two", b);
                                    questionMap.put("op_three", c);
                                    questionMap.put("op_four", d);
                                    questionMap.put("correct_ans", correct_ans);
                                    questionMap.put("setNo", set);

                                    String id = UUID.randomUUID().toString();
                                    parentMap.put(id, questionMap);
                                    templist.add(new AdminClassSevenQuestionPojo(id, question, a, b, c, d, correct_ans, set));

                                } else {
                                    int finalR = r;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //loadingText.setText("Loading...");
                                            loadingDialog.dismiss();
                                            Toast.makeText(AdminClassSevenQuestionActivity.this, "Row No. " + (finalR + 1) + "Has No Correct Option", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    return;

                                }
                            } else {
                                int finalR1 = r;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //loadingText.setText("Loading...");
                                        loadingDialog.dismiss();
                                        Toast.makeText(AdminClassSevenQuestionActivity.this, "Row No. " + (finalR1 + 1) + "Has Incorrect Data", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return;
                            }

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loadingText.setText("Uploading....");
                                FirebaseDatabase.getInstance().getReference()
                                        .child("CLASS_SEVEN_SETS").child(categoryName)
                                        .child("questions").updateChildren(parentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    list.addAll(templist);
                                                    _adepter.notifyDataSetChanged();

                                                } else {
                                                    //loadingText.setText("Loading...");
                                                    Toast.makeText(AdminClassSevenQuestionActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loadingText.setText("Loading...");
                                loadingDialog.dismiss();
                                Toast.makeText(AdminClassSevenQuestionActivity.this, "File is Empty", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;


                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //loadingText.setText("Loading...");
                            loadingDialog.dismiss();
                            Toast.makeText(AdminClassSevenQuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //loadingText.setText("Loading...");
                            loadingDialog.dismiss();
                            Toast.makeText(AdminClassSevenQuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        _adepter.notifyDataSetChanged();
    }

    private String getCellData(Row row, int cellPosition, FormulaEvaluator formulaEvaluator) {
        String value = "";
        Cell cell = row.getCell(cellPosition);
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return value + cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return value + cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return value + cell.getStringCellValue();
            default:
                return value;

        }

    }
}