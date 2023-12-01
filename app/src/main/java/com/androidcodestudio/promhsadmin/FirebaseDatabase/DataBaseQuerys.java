package com.androidcodestudio.promhsadmin.FirebaseDatabase;

import java.util.HashMap;
public class DataBaseQuerys {
    public static String Image;
    public static String phone;
    public static String Name;
    public static String roll;
    public static String section;
    public static String whichClass;


    public static String token;
    public static final String REMOTE_MSG_AUTHORIZATION ="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE ="Content-Type";

    public static final String REMOTE_MSG_TYPE ="type";
    public static final String REMOTE_MSG_INVITATION ="invitation";
    public static final String REMOTE_MSG_MEETING_TYPE ="meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN ="inviterToken";
    public static final String REMOTE_MSG_DATA ="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS ="registration_ids";

    public static HashMap<String,String> getRemoteMessageHeaders(){
        HashMap<String,String> headers = new HashMap<>();
        headers.put(REMOTE_MSG_AUTHORIZATION,"key=AAAAycvXVdE:APA91bHIvhjoiIVBiWHoC-WCyKyV6CnBw0Y-YguwdxN3SK_CygN6QMWrjt92VbhJUOO8msS0WA7OpNaDbX9SBtrp8z8li7FZdT6Ly1qcSyogLqx3yWwRiDihs5SPBB1yAIBAoVlUG3Dd");
        headers.put(REMOTE_MSG_CONTENT_TYPE,"application/json");
        return headers;
    }



}


