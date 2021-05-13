package com.gprs.mathsmania;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.gprs.mathsmania.ui.dashboard.DashboardFragment;
import com.gprs.mathsmania.ui.notifications.NotificationsFragment;

import java.util.Calendar;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;


public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener, OnUserEarnedRewardListener {




    private PendingIntent pendingIntent;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private boolean rad=false;

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 1:
                    return new NotificationsFragment();

                case 0:
                default:
                    return new DashboardFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private RewardedVideoAd mRewardedVideoAd;



    int x=1;
    private boolean rewarded = false;

    AppUpdateManager appUpdateManager;
    private View constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);


        pref = getApplicationContext().getSharedPreferences("user", 0);
        editor=pref.edit();
        if(!pref.getBoolean("policy_accept",false)){
            policy();
        }
    alarmMethod();




        constraintLayout = findViewById(R.id.container);
         appUpdateManager = AppUpdateManagerFactory.create(this);
      Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.updatePriority() >= 0
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                // Request an immediate update.

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            123);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

/*
        List<String> testDeviceIds = Arrays.asList("554E4DE422B35DA267917FE682F546A2");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

*/

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);


        loadRewardedVideoAd();
        loadAd();


        BottomNavigationView navView = findViewById(R.id.nav_view);


        final ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setEnabled(false);
        viewPager.setAdapter(new MainActivity.MyPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==1)
                navView.setSelectedItemId(R.id.navigation_notifications);
                else
                    navView.setSelectedItemId(R.id.navigation_dashboard);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
               R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
       navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               if (item.getItemId() == R.id.navigation_notifications)
                   viewPager.setCurrentItem(1);
               else
                   viewPager.setCurrentItem(0);
               return true;
           }
       });

    }

    public void gotoeasy(View view) {
        startActivity(new Intent(MainActivity.this,playboard.class));
    }

    public void gotoinsane(View view) {
        startActivity(new Intent(MainActivity.this,playboardTimed.class));
    }

    public void gotohost(View view) {
        if(isOnline(MainActivity.this)) {
            if (mRewardedVideoAd.isLoaded()) {
                x = 1;
                mRewardedVideoAd.show();
            } else if (x == 3) {
                startActivity(new Intent(MainActivity.this, mulhost.class));
            } else
                Toast.makeText(this, "Ad is loading", Toast.LENGTH_SHORT).show();

        }
    }

    public void review(){
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.

                });
            } else {
                // There was some problem, log or handle the error code.
                //  @ReviewErrorCode int reviewErrorCode = ((TaskException) task.getException()).getErrorCode();
            }
        });
    }
    public void gotojoin(View view) {

        if(isOnline(MainActivity.this)) {
            if (mRewardedVideoAd.isLoaded()) {
                x = 2;
                mRewardedVideoAd.show();
            } else if (x == 3) {
                startActivity(new Intent(MainActivity.this, muljoin.class));
            } else
                Toast.makeText(this, "Ad is loading", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-9274231809050792/5299153825",
                new AdRequest.Builder().build());
    }



    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if(rewarded) {
            if (x == 1 && isOnline(MainActivity.this))
                startActivity(new Intent(MainActivity.this, mulhost.class));
            else if(isOnline(MainActivity.this))
                startActivity(new Intent(MainActivity.this, muljoin.class));
        }
        rewarded=false;
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(com.google.android.gms.ads.reward.RewardItem rewardItem) {
      rewarded = true;
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        x=3;
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Update required !", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_OK){
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Updated Successfully !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            123);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            if(!rad && rewardedInterstitialAd!=null) {
                rewardedInterstitialAd.show(/* Activity */ MainActivity.this,/*
    OnUserEarnedRewardListener */ MainActivity.this);
            }
            else
            super.onBackPressed();
            return;
        }
        if(!pref.getString("review","not_reviewed").equals(String.valueOf(Calendar.getInstance().getTime().getDate()))){
            editor.putString("review",String.valueOf(Calendar.getInstance().getTime().getDate())).apply();
            review();
        }

        else {
            this.doubleBackToExitPressedOnce = true;
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG);
            snackbar.show();


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    private void alarmMethod(){
        Intent myIntent = new Intent(this , NotificationService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();


    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+(1000*60*60*24), 1000 *60*60*24, pendingIntent);

    }

    public boolean isOnline(Context context) {

        ConnectivityManager cMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cMan.getActiveNetworkInfo();
        if(nInfo == null || !nInfo.isConnected()){
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Please check your connectivity !", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        return (nInfo != null && nInfo.isConnected());
    }

    private void policy() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_policy, null, true);


        Button button = view.findViewById(R.id.agree);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("policy_accept",true).apply();
                alert.hide();

            }
        });

        alert.setView(view);
        alert.show();

    }

    public void loadAd() {
        // Use the test ad unit ID to load an ad.
        RewardedInterstitialAd.load(MainActivity.this, "ca-app-pub-9274231809050792/9007847432",
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            /** Called when the ad failed to show full screen content. */
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.i("TAG", "onAdFailedToShowFullScreenContent");
                                rad=true;
                                onBackPressed();
                            }

                            /** Called when ad showed the full screen content. */
                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.i("TAG", "onAdShowedFullScreenContent");
                                rad=true;
                                onBackPressed();
                            }

                            /** Called when full screen content is dismissed. */
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.i("TAG", "onAdDismissedFullScreenContent");
                                rad=true;
                                onBackPressed();
                            }
                        });
                        Log.e("TAG", "onAdLoaded");
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("TAG", "onAdFailedToLoad");
                        rad=true;
                    }
                });
    }
}