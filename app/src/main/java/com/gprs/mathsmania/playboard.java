package com.gprs.mathsmania;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

class timer{
    int m=5,s=0;
    final TextView time;
    timer(TextView t,int settime){
        time =t;
        m=settime/60;
        s=settime%60;
    }
    void dec(){
        if(m!=0 && s==0){
            m--;
            s=59;

        }
        else if(s!=0){
            s--;

        }
        time.setText(m +"m:"+ s +"s");
    }
}

class ques{
    int a,b,ans,uans;
    String uname;
    String op;
    final int[] signs = {1,-1};
    final String [] ops ={"+","-","/","X"};
    boolean res=false;

    ques(){
        do{
        Random rd = new Random();
        b = rd.nextInt(10);
        b = signs[rd.nextInt(2)]*b;
        a = rd.nextInt(10);
        a = signs[rd.nextInt(2)]*a;
        op = ops[rd.nextInt(4)];
        }while(op.equals("/") && b ==0);
        if(op.equals("+"))
            ans = a+b;
        else if(op.equals("-"))
            ans = a-b;
        else if(op.equals("/"))
            ans = a/b;
        else if(op.equals("X"))
            ans = a*b;
    }

}
public class playboard extends AppCompatActivity {

    TextView qA,qB,qC,op,n1,n2,n3,n4,n5,n6,n7,n8,n9,n0,neg,can,timedisplay;
    LinearLayout back,report,question,numberpad;
    ListView qres;
    String ans="?",sign="";
    Button submit,retry;
    CountDownTimer c1;
    timer time;
    int setTime=60;
    ArrayList<ques> qa;
    Boolean  gameOn=true;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    CustomQuesAdapter customQuesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_playboard);
        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();
        submit = findViewById(R.id.submit);
        retry=findViewById(R.id.retry);
        report=findViewById(R.id.report);
        numberpad=findViewById(R.id.numberpad);
        question=findViewById(R.id.question);
        qres=findViewById(R.id.qres);
        back =findViewById(R.id.background);
        qA = findViewById(R.id.qA);
        qB = findViewById(R.id.qB);
        qC = findViewById(R.id.qC);
        op = findViewById(R.id.op);
        n1 = findViewById(R.id.n1);
        n2 = findViewById(R.id.n2);
        n3 = findViewById(R.id.n3);
        n4 = findViewById(R.id.n4);
        n5 = findViewById(R.id.n5);
        n6 = findViewById(R.id.n6);
        n7 = findViewById(R.id.n7);
        n8 = findViewById(R.id.n8);
        n9 = findViewById(R.id.n9);
        n0 = findViewById(R.id.n0);
        neg = findViewById(R.id.neg);
        can = findViewById(R.id.can);
        qa =new ArrayList<>();
        timedisplay = findViewById(R.id.timer);
        time= new timer(timedisplay,60000);
        findViewById(R.id.background).setVisibility(View.GONE);
        setT();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(playboard.this,playboard.class));
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?")||ans.equals(""))
                    Toast.makeText(playboard.this, "Answer please !!!", Toast.LENGTH_SHORT).show();
                else{
                    qa.get(qa.size()-1).uans = Integer.parseInt(sign+ans);
                    if(qa.get(qa.size()-1).uans == qa.get(qa.size()-1).ans)
                    {
                        qa.get(qa.size()-1).res =true;
                        back.setBackgroundColor(Color.parseColor("#FFC5FFC7"));

                    }
                    else
                        back.setBackgroundColor(Color.parseColor("#FFFBC6C2"));
                    ans ="";
                    sign="";
                    qC.setText("?");
                    qa.add(new ques());
                    qA.setText(String.valueOf(qa.get(qa.size()-1).a));
                    qB.setText("("+ qa.get(qa.size() - 1).b +")");
                    op.setText(String.valueOf(qa.get(qa.size()-1).op));

                }
            }
        });

        qa.add(new ques());
        qA.setText(String.valueOf(qa.get(0).a));
        qB.setText("("+ qa.get(0).b +")");
        op.setText(String.valueOf(qa.get(0).op));



        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans="?";
                sign="";
                qC.setText(sign + ans);
            }
        });

        n0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="0";
                else if(ans.length()<=3)
                    ans=ans+'0';

                qC.setText(sign+ans);
            }
        });
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="1";
                else if(ans.length()<=3)
                    ans=ans+'1';

                qC.setText(sign+ans);
            }
        });
        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="2";
                else if(ans.length()<=3)
                    ans=ans+'2';

                qC.setText(sign+ans);
            }
        });
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="3";
                else if(ans.length()<=3)
                    ans=ans+'3';

                qC.setText(sign+ans);
            }
        });
        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="4";
                else if(ans.length()<=3)
                    ans=ans+'4';

                qC.setText(sign+ans);
            }
        });
        n5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="5";
                else if(ans.length()<=3)
                    ans=ans+'5';

                qC.setText(sign+ans);
            }
        });
        n6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="6";
                else if(ans.length()<=3)
                    ans=ans+'6';

                qC.setText(sign+ans);
            }
        });
        n7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="7";
                else if(ans.length()<=3)
                    ans=ans+'7';

                qC.setText(sign+ans);
            }
        });
        n8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="8";
                else if(ans.length()<=3)
                    ans=ans+'8';

                qC.setText(sign+ans);
            }
        });
        n9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?"))
                    ans ="9";
                else if(ans.length()<=3)
                    ans=ans+'9';

                qC.setText(sign+ans);
            }
        });
        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sign.equals("-"))
                sign="";
                else
                    sign = "-";

                qC.setText(sign+ans);
            }
        });
        ImageView exit =findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(playboard.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Want to stop practice ?");
                LayoutInflater factory = LayoutInflater.from(playboard.this);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setT() {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(playboard.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        alertDialog.setTitle("Set timer");
        alertDialog.setMessage("Decide your seconds .");
        final EditText input = new EditText(playboard.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setGravity(Gravity.CENTER);
        input.setHint("Select value from 30 to 600 seconds.");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.timer);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().equals("") && (Integer.parseInt(input.getText().toString()) < 30 || Integer.parseInt(input.getText().toString()) > 600)) {
                    Toast.makeText(playboard.this, "Please enter valid number ! ", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    setT();
                } else {
                    try {
                        setTime = Integer.parseInt(input.getText().toString());

                        time = new timer(timedisplay, setTime);
                        findViewById(R.id.background).setVisibility(View.VISIBLE);
                        c1 = new CountDownTimer(setTime * 1000, 1000) {
                            public void onTick(long l) {
                                time.dec();
                            }

                            public void onFinish() {
                                int score = 0;
                                for (int i = 0; i < qa.size(); i++) {
                                    if (qa.get(i).res)
                                        score = score + 1;
                                }
                                int t = pref.getInt("Atime", 1);
                                int v = pref.getInt("Avalue", 0);
                                double weigth = (double) v / t;

                                if ((double) score / setTime > weigth) {
                                    editor.putInt("Atime", setTime);
                                    editor.putInt("Avalue", score);
                                    editor.apply();
                                }
                                timedisplay.setText("Total Question : " + qa.size());
                                submit.setVisibility(View.GONE);
                                question.setVisibility(View.GONE);
                                numberpad.setVisibility(View.GONE);
                                report.setVisibility(View.VISIBLE);
                                TextView score1 = findViewById(R.id.score);
                                score1.setText(String.valueOf(score));
                                customQuesAdapter = new CustomQuesAdapter(playboard.this, qa);
                                qres.setAdapter(customQuesAdapter);

                            }
                        };
                        c1.start();
                    } catch (NumberFormatException e) {
                        Toast.makeText(playboard.this, "Please enter valid number ! ", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        setT();
                    }
                }
            }});

        alertDialog.show();
    }


}