package wtf.moneymod.client.api.management.impl;

public class SessionManagement {

    private static int kills;
    private static int death;
    private static int pops;

    public int getKills(){
        return kills;
    }
    public int getDeath(){
        return death;
    }
    public int getPops(){
        return pops;
    }

    public void addKills(){
        kills+=1;
    }
    public void addDeath(){
        death+=1;
    }
    public void addPops(){
        pops+=1;
    }

    public String getInfo(){
        return String.format("Session info: kills: %s, pops: %s, death: %s",kills,pops,death);
    }

    public void reset(){
        kills = 0;
        death = 0;
        pops = 0;
    }


}
