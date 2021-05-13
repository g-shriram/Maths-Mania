package com.gprs.mathsmania;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class playboardTimed extends AppCompatActivity {

    TextView qA, qB, qC, op, n1, n2, n3, n4, n5, n6, n7, n8, n9, n0, neg, can, timedisplay;
    LinearLayout back, report, question, numberpad;
    ListView qres;
    String ans = "?", sign = "";
    Button submit, retry;
    CountDownTimer c1;
    timer time;
    int setTime = 60;
    ArrayList<ques> qa;
    Boolean gameOn = true;
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
        submit = findViewById(R.id.submit);
        retry = findViewById(R.id.retry);
        report = findViewById(R.id.report);
        numberpad = findViewById(R.id.numberpad);
        question = findViewById(R.id.question);
        qres = findViewById(R.id.qres);
        back = findViewById(R.id.background);
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
        qa = new ArrayList<>();
        timedisplay = findViewById(R.id.timer);
        time = new timer(timedisplay, 60000);

        pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();

        time = new timer(timedisplay, setTime);
        c1 = new CountDownTimer(1000 * setTime, 1000) {
            public void onTick(long l) {
                time.dec();
            }

            public void onFinish() {
                back.setBackgroundColor(Color.parseColor("#FFFBC6C2"));

                int score = 0;
                for (int i = 0; i < qa.size(); i++) {
                    if (qa.get(i).res)
                        score = score + 1;
                }
                timedisplay.setText("Total Question : " + qa.size());
                submit.setVisibility(View.GONE);
                question.setVisibility(View.GONE);
                numberpad.setVisibility(View.GONE);
                report.setVisibility(View.VISIBLE);
                TextView score1 = findViewById(R.id.score);
                score1.setText(String.valueOf(score));
                if(pref.getInt("Atimescore",0)<score){
                    editor.putInt("Atimescore",score).apply();
                }
                customQuesAdapter = new CustomQuesAdapter(playboardTimed.this, qa);
                qres.setAdapter(customQuesAdapter);
            }
        };
        c1.start();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(playboardTimed.this, playboardTimed.class));
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?") || ans.equals(""))
                    Toast.makeText(playboardTimed.this, "Answer please !!!", Toast.LENGTH_SHORT).show();
                else {
                    qa.get(qa.size() - 1).uans = Integer.parseInt(sign + ans);
                    if (qa.get(qa.size() - 1).uans == qa.get(qa.size() - 1).ans) {
                        qa.get(qa.size() - 1).res = true;
                        ans ="";
                        sign="";
                        qC.setText("?");
                        qa.add(new ques());
                        qA.setText(String.valueOf(qa.get(qa.size()-1).a));
                        qB.setText("("+ qa.get(qa.size() - 1).b +")");
                        op.setText(String.valueOf(qa.get(qa.size()-1).op));
                        c1.cancel();
                        if(setTime>30)
                        setTime=setTime-2;
                        else if(setTime>3)
                            setTime=setTime-1;
                        time = new timer(timedisplay, setTime);
                        c1 = new CountDownTimer(1000 * setTime, 1000) {
                            public void onTick(long l) {
                                time.dec();
                            }

                            public void onFinish() {
                                back.setBackgroundColor(Color.parseColor("#FFFBC6C2"));

                                int score = 0;
                                for (int i = 0; i < qa.size(); i++) {
                                    if (qa.get(i).res)
                                        score = score + 1;
                                }
                                timedisplay.setText("Total Question : " + qa.size());
                                submit.setVisibility(View.GONE);
                                question.setVisibility(View.GONE);
                                numberpad.setVisibility(View.GONE);
                                report.setVisibility(View.VISIBLE);
                                TextView score1 = findViewById(R.id.score);
                                score1.setText(String.valueOf(score));
                                if(pref.getInt("Atimescore",0)<score){
                                    editor.putInt("Atimescore",score).apply();
                                }
                                customQuesAdapter = new CustomQuesAdapter(playboardTimed.this, qa);
                                qres.setAdapter(customQuesAdapter);
                            }
                        };
                        c1.start();

                    } else {
                        back.setBackgroundColor(Color.parseColor("#FFFBC6C2"));

                        int score = 0;
                        for (int i = 0; i < qa.size(); i++) {
                            if (qa.get(i).res)
                                score = score + 1;
                        }
                        time = new timer(timedisplay, 1);
                        c1.start();
                        if(pref.getInt("Atimescore",0)<score){
                            editor.putInt("Atimescore",score).apply();
                        }
                        timedisplay.setText("Total Question : " + qa.size());
                        submit.setVisibility(View.GONE);
                        question.setVisibility(View.GONE);
                        numberpad.setVisibility(View.GONE);
                        report.setVisibility(View.VISIBLE);
                        TextView score1 = findViewById(R.id.score);
                        score1.setText(String.valueOf(score));
                        customQuesAdapter = new CustomQuesAdapter(playboardTimed.this, qa);
                        qres.setAdapter(customQuesAdapter);
                    }
                }
            }
        });

        qa.add(new ques());
        qA.setText(String.valueOf(qa.get(0).a));
        qB.setText("(" + qa.get(0).b + ")");
        op.setText(String.valueOf(qa.get(0).op));


        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans = "?";
                sign = "";
                qC.setText(sign + ans);
            }
        });

        n0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "0";
                else if (ans.length() <= 3)
                    ans = ans + '0';

                qC.setText(sign + ans);
            }
        });
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "1";
                else if (ans.length() <= 3)
                    ans = ans + '1';

                qC.setText(sign + ans);
            }
        });
        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "2";
                else if (ans.length() <= 3)
                    ans = ans + '2';

                qC.setText(sign + ans);
            }
        });
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "3";
                else if (ans.length() <= 3)
                    ans = ans + '3';

                qC.setText(sign + ans);
            }
        });
        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "4";
                else if (ans.length() <= 3)
                    ans = ans + '4';

                qC.setText(sign + ans);
            }
        });
        n5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "5";
                else if (ans.length() <= 3)
                    ans = ans + '5';

                qC.setText(sign + ans);
            }
        });
        n6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "6";
                else if (ans.length() <= 3)
                    ans = ans + '6';

                qC.setText(sign + ans);
            }
        });
        n7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "7";
                else if (ans.length() <= 3)
                    ans = ans + '7';

                qC.setText(sign + ans);
            }
        });
        n8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "8";
                else if (ans.length() <= 3)
                    ans = ans + '8';

                qC.setText(sign + ans);
            }
        });
        n9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans.equals("?"))
                    ans = "9";
                else if (ans.length() <= 3)
                    ans = ans + '9';

                qC.setText(sign + ans);
            }
        });
        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign.equals("-"))
                    sign = "";
                else
                    sign = "-";

                qC.setText(sign + ans);
            }
        });
        ImageView exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(playboardTimed.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Want to stop practice ?");
                LayoutInflater factory = LayoutInflater.from(playboardTimed.this);

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




}