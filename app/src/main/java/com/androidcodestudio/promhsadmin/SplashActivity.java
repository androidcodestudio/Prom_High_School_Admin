package com.androidcodestudio.promhsadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.androidcodestudio.promhsadmin.AdminLogin.AdminLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth _firebaseAuth;
    private Window _window;
    //private Dialog _loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        _firebaseAuth = FirebaseAuth.getInstance();

        _window = this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.toolbar));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        //loading dialog
//        _loadingDialog = new Dialog(SplashActivity.this);
//        _loadingDialog.setContentView(R.layout.loading);
//        _loadingDialog.setCancelable(false);
//        _loadingDialog.getWindow().setBackgroundDrawable(SplashActivity.this.getDrawable(R.drawable.slider_background));
//        _loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        _loadingDialog.show();
//        //loading dialog

        FirebaseUser currentUser = _firebaseAuth.getCurrentUser();

        if (currentUser == null) {

            Intent RegisterIntent = new Intent(SplashActivity.this, AdminLoginActivity.class);
            startActivity(RegisterIntent);
            finish();
            //_loadingDialog.dismiss();

        } else {
            FirebaseFirestore.getInstance()
                    .collection("USERS")
                    .document(currentUser.getUid())
                    .update("LAST_SEEN", FieldValue.serverTimestamp())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                startActivity(intent);
                                finish();
                                //_loadingDialog.dismiss();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                                //_loadingDialog.dismiss();
                            }
                        }
                    });
        }

//        this code allays mobile screen light on
//
//                Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                dowork();
//            }
//        });
//        thread.start();

    }


//    public void dowork (){
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Intent splashintent = new Intent(SplashActivity.this, MainActivity.class);
//        startActivity(splashintent);
//        finish();
//    }
}