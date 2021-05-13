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

public class muljoin extends AppCompatActivity {

    String key;
    ArrayList<String> players;
    TextView joinedlist;
    team team;
    Button start;
    EditText name,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_muljoin);

        name=findViewById(R.id.hostname);
        code=findViewById(R.id.code);
        joinedlist = findViewById(R.id.joinedlist);
        start =findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString().isEmpty() || name.getText().toString().isEmpty())
                    Toast.makeText(muljoin.this, "Please fill all details !", Toast.LENGTH_SHORT).show();
                else{

                    key = code.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                team t=dataSnapshot.getValue(team.class);
                                if(t!=null) {

                                    start.setText("Waiting for host to start");
                                    start.setEnabled(false);
                                    name.setEnabled(false);
                                    code.setEnabled(false);

                                    if (t.players == null)
                                        t.players = new ArrayList<>();
                                    t.players.add(name.getText().toString());
                                    FirebaseDatabase.getInstance().getReference().child(key).setValue(t);

                                    FirebaseDatabase.getInstance().getReference().child(key).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            try {
                                                if (dataSnapshot != null) {
                                                    StringBuilder list = new StringBuilder();
                                                    team teams = dataSnapshot.getValue(team.class);
                                                    if (teams.gamemode == 1) {
                                                        Intent i = new Intent(muljoin.this, Multiplayer.class);
                                                        i.putExtra("name", name.getText().toString());
                                                        i.putExtra("key", key);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                    players = teams.getPlayers();
                                                    if (players != null) {
                                                        if (players.size() >= 2)
                                                            start.setVisibility(View.VISIBLE);
                                                        else
                                                            start.setVisibility(View.GONE);


                                                        for (int i = 0; i < players.size(); i++) {
                                                            list.append(players.get(i)).append("\n");
                                                        }
                                                        joinedlist.setText(list.toString());
                                                    }
                                                }
                                            }
                                            catch (Exception e){
                                                Toast.makeText(muljoin.this, "Host ended the game !", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(muljoin.this,"Invalid code",Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

    }
}