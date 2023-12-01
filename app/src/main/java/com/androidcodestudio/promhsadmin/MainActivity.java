package com.androidcodestudio.promhsadmin;

import static com.androidcodestudio.promhsadmin.Constant.TOPIC;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidcodestudio.promhsadmin.AdminAllClassView.AdminAllClassActivity;
import com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightActivity;
import com.androidcodestudio.promhsadmin.AdminClassEleven.AdminClassElevenActivity;
import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFiveActivity;
import com.androidcodestudio.promhsadmin.AdminClassNine.AdminClassNineActivity;
import com.androidcodestudio.promhsadmin.AdminClassSeven.AdminClassSevenActivity;
import com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixActivity;
import com.androidcodestudio.promhsadmin.AdminClassTen.AdminClassTenActivity;
import com.androidcodestudio.promhsadmin.AdminClassTwelve.AdminClassTwelveActivity;
import com.androidcodestudio.promhsadmin.AdminFaculty.AdminAddTeacherActivity;
import com.androidcodestudio.promhsadmin.ClassFive.ClassFiveFragment;
import com.androidcodestudio.promhsadmin.eBook.EbookActivity;
import com.androidcodestudio.promhsadmin.register.LoginActivity;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int MY_REQUEST_CODE = 100;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Window _window;
    private ImageView logo;
    //private FirebaseAuth _firebaseAuth;
    private Intent intent;
    private AdView mAdView;
    private LinearLayout frame,frame1,frame2,frame3,frame4,frame5,frame6,frame7;
    private String messagestr, phonestr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForAppUpdate();

        askNotificationPermission();

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.start,R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Toolbar toolbar = findViewById(R.id.titles_bar);
        setSupportActionBar(toolbar);

        logo = findViewById(R.id.logo);

        toolbar.setTitleTextColor(Color.BLACK);//for red colored toolbar title
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_menu);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frame = findViewById(R.id.frams);

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        navController = Navigation.findNavController(MainActivity.this,R.id.frame_layout);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.


                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(this, "if not allow notification you will not show notifications", Toast.LENGTH_SHORT).show();
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // FCM SDK (and your app) can post notifications.

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

    }

    private void checkForAppUpdate(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.navigation_add_teachers:
                intent = new Intent(MainActivity.this, AdminAddTeacherActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_whatsapp:
                messagestr = "Hi I Have A Question ";
                phonestr = "+91" + "9735672070";
                if (isWhatappInstalled()) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phonestr + "&text=" + messagestr));
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.navigation_facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100094166857987")));
                break;
            case R.id.navigation_ebooks:
                intent = new Intent(MainActivity.this, EbookActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_share:
                BitmapDrawable bitmapDrawable = (BitmapDrawable)logo.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ShareImage(bitmap);
                break;
            case R.id.navigation_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.androidcodestudio.rathtala")));
                break;
            case R.id.navigation_admin:
                intent = new Intent(MainActivity.this, AdminPortalActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classFive:
                intent = new Intent(MainActivity.this, AdminClassFiveActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classSix:
                intent = new Intent(MainActivity.this, AdminClassSixActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classSeven:
                intent = new Intent(MainActivity.this, AdminClassSevenActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classEight:
                intent = new Intent(MainActivity.this, AdminClassEightActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classNine:
                intent = new Intent(MainActivity.this, AdminClassNineActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classTen:
                intent = new Intent(MainActivity.this, AdminClassTenActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classEleven:
                intent = new Intent(MainActivity.this, AdminClassElevenActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_classTwelve:
                intent = new Intent(MainActivity.this, AdminClassTwelveActivity.class);
                startActivity(intent);
                break;


        }
        return true;
    }

    private boolean isWhatappInstalled() {
        PackageManager packageManager = MainActivity.this.getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }

    private void ShareImage(Bitmap bitmap){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Uri bmpUri;
        String textToShare ="https://play.google.com/store/apps/details?id=com.androidcodestudio.rathtala";
        bmpUri = SaveImage(bitmap,this);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_STREAM,bmpUri);
        share.putExtra(Intent.EXTRA_SUBJECT,"New App");
        share.putExtra(Intent.EXTRA_TEXT,textToShare);
        startActivity(Intent.createChooser(share,"RATHTALA COLONY HIGH SCHOOL"));

    }

    private  Uri SaveImage(Bitmap image, Context context){
        File imagesFolder = new File(context.getCacheDir(),"images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder,"shared_images.jpg");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()), "com.androidcodestudio.globeconsultant"+".provider",file);
        }

        catch (IOException e){
            Log.d("TAG","Exception"+e.getMessage());
        }

        return uri;

    }
}