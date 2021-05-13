package com.gprs.mathsmania;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class CustomPlayerAdapter extends ArrayAdapter {

    private final ArrayList<ques> name;
    private final Activity context;
    final String uname;


    public CustomPlayerAdapter(Activity context, ArrayList<ques> name,String uname) {
        super(context, R.layout.quesitem, name);
        this.context = context;
        this.name = name;
        this.uname =uname;



    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.quesitem, null, true);
        TextView name1 = rowView.findViewById(R.id.name);
        TextView ins1 = rowView.findViewById(R.id.type);
        TextView type1 = rowView.findViewById(R.id.type1);
        TextView point = rowView.findViewById(R.id.point);
        ImageView logo = rowView.findViewById(R.id.logo);



        name1.setText(name.get(position).a+" "+name.get(position).op +" "+name.get(position).b + " = ?");
        ins1.setText("Correct : "+ name.get(position).ans);
        type1.setText("Your's : " + name.get(position).uans);

        point.setText(name.get(position).uname);

        if(uname.equals(name.get(position).uname)){
            logo.setImageResource(R.drawable.correct);
        }

        else {
            logo.setImageResource(R.drawable.wrong);
        }

        return rowView;

    }

}


