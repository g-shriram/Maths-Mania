package com.gprs.mathsmania.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gprs.mathsmania.R;

public class NotificationsFragment extends Fragment {

    TextView Atime,Avalue,Timescore;
    SharedPreferences pref;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        pref = getActivity().getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode


        AdView mAdView = root.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

      Atime =root.findViewById(R.id.Atime);
      Avalue=root.findViewById(R.id.Avalue);
      Timescore =root.findViewById(R.id.TimeScore);
      Atime.setText("Time : "+ pref.getInt("Atime", 0));
      Avalue.setText("Score : "+ pref.getInt("Avalue", 0));
      Timescore.setText("Score : "+ pref.getInt("Atimescore", 0));

        return root;
    }
}