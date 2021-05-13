package com.gprs.mathsmania;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiplayerHost extends AppCompatActivity {

    TextView qA,qB,qC,op,n1,n2,n3,n4,n5,n6,n7,n8,n9,n0,neg,can,timedisplay;
    LinearLayout back,report,question,numberpad;
    ListView qres;
    String ans="?",sign="",uname;
    Button submit;
    CountDownTimer c1;
    timer time;
    ArrayList<ques> qa;
    String key;
    final int qcount =20;
    Boolean  gameOn=true;

    CustomPlayerAdapter customQuesAdapter;
    private final Integer setTime=60;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    try {
        if (key != null)
            FirebaseDatabase.getInstance().getReference().child(key).removeValue();

    }
    catch (Exception e){

    }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_host);

       key = getIntent().getStringExtra("key");

       uname= getIntent().getStringExtra("name");

        submit = findViewById(R.id.submit);

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

        if(qcount>0){
            qa.add(new ques());
            FirebaseDatabase.getInstance().getReference().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){
                        team t=dataSnapshot.getValue(team.class);
                        t.setQues(atos(qa));
                        FirebaseDatabase.getInstance().getReference().child(key).setValue(t);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("?")||ans.equals(""))
                    Toast.makeText(MultiplayerHost.this, "Answer please !!!", Toast.LENGTH_SHORT).show();
                else{
                    qa.get(qa.size()-1).uans = Integer.parseInt(sign+ans);


                    if(qa.get(qa.size()-1).uans == qa.get(qa.size()-1).ans)
                    {
                        qa.get(qa.size()-1).res =true;
                        qa.get(qa.size()-1).uname =uname;
                        qa.add(new ques());
                        back.setBackgroundColor(Color.parseColor("#FFC5FFC7"));
                        FirebaseDatabase.getInstance().getReference().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null){
                                    team t=dataSnapshot.getValue(team.class);
                                    t.setQues(atos(qa));
                                    FirebaseDatabase.getInstance().getReference().child(key).setValue(t);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else
                        back.setBackgroundColor(Color.parseColor("#FFFBC6C2"));
                    ans ="";
                    sign="";
                    qC.setText("?");

                    qA.setText(String.valueOf(qa.get(qa.size()-1).a));
                    qB.setText("("+ qa.get(qa.size() - 1).b +")");
                    op.setText(String.valueOf(qa.get(qa.size()-1).op));

                }
            }
        });





        FirebaseDatabase.getInstance().getReference().child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        team t = dataSnapshot.getValue(team.class);

                        if (t.getQues() != null) {

                            qa = stoa(t.getQues());
                            timedisplay.setText("Rem : " + (20 - qa.size()));

                            if (qa.size() > 1 && qa.get(qa.size() - 2).uname.equals(uname))
                                back.setBackgroundColor(Color.parseColor("#FFC5FFC7"));
                            else
                                back.setBackgroundColor(Color.parseColor("#FFFBC6C2"));

                        }

                        ans = "";
                        sign = "";
                        qC.setText("?");

                        qA.setText(String.valueOf(qa.get(qa.size() - 1).a));
                        qB.setText("(" + qa.get(qa.size() - 1).b + ")");
                        op.setText(String.valueOf(qa.get(qa.size() - 1).op));

                        if (qa.size() > 20) {
                            FirebaseDatabase.getInstance().getReference().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        team t = dataSnapshot.getValue(team.class);
                                        t.gamemode = 2;
                                        FirebaseDatabase.getInstance().getReference().child(key).setValue(t);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            HashMap<String, Integer> hm = new HashMap<>();
                            for (int i = 0; i < qa.size(); i++) {
                                if (hm.containsKey(qa.get(i).uname)) {
                                    hm.put(qa.get(i).uname, hm.get(qa.get(i).uname) + 1);
                                } else
                                    hm.put(qa.get(i).uname, 1);
                            }
                            String max = "";
                            int mm = 0;
                            for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                                if (entry.getValue() > mm) {
                                    mm = entry.getValue();
                                    max = entry.getKey();
                                }

                            }

                            timedisplay.setText("Total Question : " + qa.size());
                            submit.setVisibility(View.GONE);
                            question.setVisibility(View.GONE);
                            numberpad.setVisibility(View.GONE);
                            report.setVisibility(View.VISIBLE);
                            TextView score1 = findViewById(R.id.score);
                            score1.setText(max);
                            customQuesAdapter = new CustomPlayerAdapter(MultiplayerHost.this, qa, uname);
                            qres.setAdapter(customQuesAdapter);
                        }

                    }
                }
                catch (Exception e){
                    Toast.makeText(MultiplayerHost.this, "Host Ended the game!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MultiplayerHost.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Want to stop Game ?");
                LayoutInflater factory = LayoutInflater.from(MultiplayerHost.this);

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



    String atos(ArrayList<ques> qq){
        return new Gson().toJson(qq);
    }

    ArrayList<ques> stoa(String a){
        Gson gson = new Gson();
        TypeToken<ArrayList<ques>> token = new TypeToken<ArrayList<ques>>() {
        };
        ArrayList<ques> pb = gson.fromJson(a, token.getType());
        return pb;
    }

}