package com.androidcodestudio.promhsadmin.register;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.intro.IntroActivity;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    //init
    private PinView _otp_field;
    private ProgressBar _otp_progressbar;
    private Button _btn_lets;
    private TextView _tv_phone;
    private TextView _resend_btn;
    //init

    private Timer _timer;
    private int _count = 60;

    //firebase
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    private FirebaseAuth _firebaseAuth;
    private FirebaseFirestore _db;
    //firebase

    String image;
    String phone;
    String name;
    String roll;
    String section;
    String classes;

    private Dialog _loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        //loading dialog
        _loadingDialog = new Dialog(this);
        _loadingDialog.setContentView(R.layout.loading_progress_dialog);
        _loadingDialog.setCancelable(false);
        _loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        _loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //_loadingDialog.show();
        //loading dialog

        image = getIntent().getStringExtra("Person");
        name = getIntent().getStringExtra("Name");
        phone = getIntent().getStringExtra("Phone");
        roll = getIntent().getStringExtra("Roll");
        section = getIntent().getStringExtra("Section");
        classes = getIntent().getStringExtra("Class");

        initializeView();

        _firebaseAuth = FirebaseAuth.getInstance();
        _tv_phone.setText("Verification code has been sent to +91 " + phone);
        sendOtp();

        _timer = new Timer();
        _timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                OtpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (_count == 0) {
                            _resend_btn.setText("Resend ");
                            _resend_btn.setEnabled(true);
                            _resend_btn.setAlpha(1f);
//                            _loadingDialog.dismiss();

                        } else {
                            _resend_btn.setText("Resend " + _count);
                            _count--;
//                            _loadingDialog.show();

                        }
                    }
                });
            }
        }, 0, 1000);

        _resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
                _resend_btn.setEnabled(false);
                _resend_btn.setAlpha(0.5f);
                _count = 60;
            }
        });

        _btn_lets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_otp_field.getText() == null || _otp_field.getText().toString().isEmpty()) {
                    return;
                }
                _otp_field.setError(null);

                String code = _otp_field.getText().toString();
                verifyCode(code);


            }
        });


    }

    private void initializeView() {
        _otp_field = findViewById(R.id.otp_field);
        _otp_progressbar = findViewById(R.id.otp_progressbar);
        _btn_lets = findViewById(R.id.btn_lets);
        _tv_phone = findViewById(R.id.tv_phone);
        _resend_btn = findViewById(R.id.resend_btn);
        _db = FirebaseFirestore.getInstance();

    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
            _otp_progressbar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Toast toast = Toast.makeText(OtpActivity.this, "Verification Code is wrong, try again", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            _otp_progressbar.setVisibility(View.INVISIBLE);
        }

    }

    private void sendOtp() {
        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                String code = credential.getSmsCode();
                if (code != null) {
                    _otp_field.setText(code);

                    verifyCode(code);

                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(OtpActivity.this, "invalid number", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(OtpActivity.this, "number has been exceeded", Toast.LENGTH_LONG).show();
                }
                _otp_progressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone,
                60,
                TimeUnit.SECONDS,
                OtpActivity.this,
                mcallback);
    }

    private void resendOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone,
                60,
                TimeUnit.SECONDS,
                OtpActivity.this,
                mcallback, mResendToken);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        _firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> user = new HashMap<>();
                            user.put("Image",image);
                            user.put("Name", name);
                            user.put("phone", phone);
                            user.put("Roll", roll);
                            user.put("Section", section);
                            user.put("Class", classes);

                            // Add a new document with a generated ID
                            _db.collection("USERS").document(_firebaseAuth.getUid())
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent otp_intent = new Intent(OtpActivity.this, IntroActivity.class);
                                                otp_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                                startActivity(otp_intent);
                                                finish();

                                            } else {
                                                Toast.makeText(OtpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                _otp_field.setError("Invalid OTP");
                            }
                            _otp_progressbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _timer.cancel();
    }
}
