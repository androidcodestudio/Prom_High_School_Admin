package com.androidcodestudio.promhsadmin.AdminLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.androidcodestudio.promhsadmin.MainActivity;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import static com.androidcodestudio.promhsadmin.Constant.EmployeeIdNumber;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText _employeeEditText;
    private ProgressBar _progressBar;
    private Button _btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        _employeeEditText = findViewById(R.id.edtSignupRoll);
        _progressBar = findViewById(R.id.login_progressbar);
        _btnVerify = findViewById(R.id.btnVerify);

        _btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_employeeEditText.getText().toString().isEmpty()) {
                    _employeeEditText.setError("required");
                    _progressBar.setVisibility(View.INVISIBLE);
                    _btnVerify.setVisibility(View.VISIBLE);
                    return;
                }

                FirebaseFirestore.getInstance().collection("EMPLOYEE").document("fvllwjHJqw6CHcCyuvLR")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {

                                    EmployeeIdNumber = task.getResult().getString("EmployeeNumber");

                                    if (_employeeEditText.getText().toString().equals(EmployeeIdNumber) ){

                                        Intent intent = new Intent(AdminLoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                        startActivity(intent);
                                        finish();

                                    }else{
                                        Toast.makeText(AdminLoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AdminLoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}