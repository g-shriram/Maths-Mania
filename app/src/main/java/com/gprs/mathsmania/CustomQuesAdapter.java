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



class CustomQuesAdapter extends ArrayAdapter {

    private final ArrayList<ques> name;
    private final Activity context;


    public CustomQuesAdapter(Activity context, ArrayList<ques> name) {
        super(context, R.layout.quesitem, name);
        this.context = context;
        this.name = name;



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

        if(name.get(position).res){
            point.setText("point : 1");
            logo.setImageResource(R.drawable.correct);
        }

        else {
            logo.setImageResource(R.drawable.wrong);
            point.setText("point : 0");
        }

        return rowView;

    }

}


