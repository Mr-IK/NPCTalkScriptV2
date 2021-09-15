package jp.mkserver.npctalkscriptv2.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserData {

    private UUID uuid;
    private String playerName;

    private List<String> setFlags = new ArrayList<>();
    private HashMap<String,Integer> counters = new HashMap<>();

    public UserData(UUID uuid,String playerName){
        this.uuid = uuid;
        this.playerName = playerName;
    }

    // フラグを立てる(追加する)
    public void addFlag(String flagName){
        setFlags.add(flagName);
    }

    // フラグを持っているか
    public boolean hasFlag(String flagName){
        return setFlags.contains(flagName);
    }

    // フラグを下げる(削除する)
    public void removeFlag(String flagName){
        setFlags.remove(flagName);
    }

    // カウンターをセットする
    public void setCounter(String counter,int count){
        counters.put(counter,count);
    }

    // カウンターを持っているか
    public boolean hasCounter(String counter){
        return counters.containsKey(counter);
    }

    // カウンターの数字を取得
    public int getCounter(String counter){
        if(hasCounter(counter)){
            return counters.get(counter);
        }
        return Integer.MIN_VALUE;
    }

    // カウンターを削除する
    public void removeCounter(String counter){
        counters.remove(counter);
    }


    // プレイヤーの固有ID
    public UUID getUUID() {
        return uuid;
    }

    // プレイヤー名
    public String getPlayerName() {
        return playerName;
    }
}
