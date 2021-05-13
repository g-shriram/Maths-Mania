package com.gprs.mathsmania.ui.dashboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gprs.mathsmania.R;

public class DashboardFragment extends Fragment  {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        AdView mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        root.findViewById(R.id.policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policy();
            }
        });

        return root;
    }

     void policy() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();


        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_policy, null, true);


        Button button = view.findViewById(R.id.agree);
        button.setVisibility(View.GONE);

        alert.setView(view);
        alert.show();

    }
}