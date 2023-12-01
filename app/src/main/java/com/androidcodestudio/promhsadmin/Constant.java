package com.androidcodestudio.promhsadmin;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.androidcodestudio.promhsadmin.api.ApiUtilities;
import com.androidcodestudio.promhsadmin.model.PushNotification;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Constant {
    public static final String BASE_URL = "https://fcm.googleapis.com";
    public static final String SERVER_KEY = "AAAA1J9fGiY:APA91bHjGu7DFmzlehuSNPowCpbxZW75ZPXDCqL3rZrcAajXh_RWP51pmkH3Tx3km_U1jcp9Px_MmbE_UEUKNqCnN3qgKlTkaRnGjtNwbMhHwUAEw-BIz5t1-hys2sS9SFAY5SY2XGvN";
    public static final String CONTENT_TYPE ="application/json";
    public static final String TOPIC ="/topics/papayacoders";

    public static String EmployeeIdNumber;
    public static RewardedAd mRewardedAd;
    public static void loadRewardedAd(Context context) {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        RewardedAd.load(context, "ca-app-pub-3940256099942544/5224354917", adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                loadRewardedAd(context);
                mRewardedAd=null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd=rewardedAd;

                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });
            }
        });
    }

    public static void sendNotification(PushNotification notification, Context context) {

        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
