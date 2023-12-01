package com.androidcodestudio.promhsadmin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidcodestudio.promhsadmin.network.ApiClient;
import com.androidcodestudio.promhsadmin.network.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.REMOTE_MSG_DATA;
import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.REMOTE_MSG_INVITATION;
import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.REMOTE_MSG_INVITER_TOKEN;
import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.REMOTE_MSG_REGISTRATION_IDS;
import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.REMOTE_MSG_TYPE;
import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.getRemoteMessageHeaders;
import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.token;

public class IncomingActivity extends AppCompatActivity {

    private String inviteToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming);

        String meetingType = getIntent().getStringExtra("set");

//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                if (task.isSuccessful() && task.getResult() != null){
//                    inviteToken = task.getResult().getToken();
//                }
//            }
//        });

        initiateMeeting();
    }
    private void initiateMeeting(){

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //DataBaseQuerys.phone = task.getResult().getString("phone");
                            token = task.getResult().getString("fcm_token");

                            try {
                                JSONArray tokens = new JSONArray();
                                tokens.put(token);

                                JSONObject body = new JSONObject();
                                JSONObject data = new JSONObject();

                                data.put(REMOTE_MSG_TYPE,REMOTE_MSG_INVITATION);
                                //data.put(REMOTE_MSG_MEETING_TYPE,meetingType);
                                data.put(REMOTE_MSG_INVITER_TOKEN,inviteToken);

                                body.put(REMOTE_MSG_DATA,data);
                                body.put(REMOTE_MSG_REGISTRATION_IDS,tokens);

                                sendRemoteMessage(body.toString(),REMOTE_MSG_INVITATION);

                            }catch (Exception exception){
                                Toast.makeText(IncomingActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }



                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(IncomingActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void sendRemoteMessage(String remoteMessageBody,String type){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                getRemoteMessageHeaders(),remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()){
                    if (type.equals(REMOTE_MSG_INVITATION)){
                        Toast.makeText(IncomingActivity.this, "INVITATION SENT SuccessFULLY", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(IncomingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Toast.makeText(IncomingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


}