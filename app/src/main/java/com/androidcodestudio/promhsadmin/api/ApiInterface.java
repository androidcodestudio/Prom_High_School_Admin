package com.androidcodestudio.promhsadmin.api;


import static com.androidcodestudio.promhsadmin.Constant.CONTENT_TYPE;
import static com.androidcodestudio.promhsadmin.Constant.SERVER_KEY;
import com.androidcodestudio.promhsadmin.model.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiInterface {
    @Headers({"Authorization: key="+SERVER_KEY,"Content-Type:"+CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);
}
