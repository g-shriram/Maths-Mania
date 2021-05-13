package com.gprs.mathsmania;

import java.util.ArrayList;

public class team {
    String host,code;
    int gamemode=0;
    String q;

    public team(String host, String code, int gamemode, String ques, ArrayList<String> players) {
        this.host = host;
        this.code = code;
        this.gamemode = gamemode;

        this.q = ques;
        this.players = players;
    }

    public String getCode() {
        return code;
    }

    public String  getQues() {
          return q;
    }

    public void setQues(String ques) {
        this.q = ques;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGamemode() {
        return gamemode;
    }

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    ArrayList<String> players;

    public  team(){
        players = new ArrayList<>();
        host="";
        code = "";
    }
    public team(String code,String host, ArrayList<String> players) {
        this.code=code;
        this.host = host;
        this.players = players;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }
}
