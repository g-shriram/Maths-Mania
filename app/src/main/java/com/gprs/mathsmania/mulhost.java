package com.gprs.mathsmania;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

class RandomString {

    // function to generate a random string of length n
    static String getAlphaNumericString() {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}



    public class mulhost extends AppCompatActivity {

        String key;
        Button start;
        ArrayList<String> players;
        TextView code,joinedlist;
        team team,teams;
        EditText name;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_mulhost);

            name=findViewById(R.id.hostname);
            code=findViewById(R.id.code);
            start =findViewById(R.id.start);
            joinedlist = findViewById(R.id.joinedlist);
            key = RandomString.getAlphaNumericString();
            code.setText(key);

            team = new team(key,name.getText().toString(),new ArrayList<String>());

            FirebaseDatabase.getInstance().getReference().child(key).setValue(team);

            FirebaseDatabase.getInstance().getReference().child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            String list = "";
                            teams = dataSnapshot.getValue(team.class);
                            players = teams.getPlayers();
                            if (players != null) {
                                if (players.size() >= 1)
                                    start.setVisibility(View.VISIBLE);
                                else
                                    start.setVisibility(View.GONE);


                                for (int i = 0; i < players.size(); i++) {
                                    list = list + players.get(i);
                                }
                                joinedlist.setText(list);
                            }
                        }
                    }
                    catch (Exception e){

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name.getText().toString().equals(null)||name.getText().toString().equals("")){
                        Toast.makeText(mulhost.this, "Please fill your name !", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        teams.host = name.getText().toString();
                        teams.gamemode =1;

                        Intent i =new Intent(mulhost.this,MultiplayerHost.class);
                        i.putExtra("key",key);
                        i.putExtra("name",name.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child(key).setValue(teams);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }


    }
