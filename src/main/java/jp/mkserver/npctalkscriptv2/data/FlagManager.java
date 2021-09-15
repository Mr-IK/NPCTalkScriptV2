package jp.mkserver.npctalkscriptv2.data;

import jp.mkserver.npctalkscriptv2.NPCTalkScriptV2;
import jp.mkserver.npctalkscriptv2.util.sql.Query;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class FlagManager {

    private NPCTalkScriptV2 plugin;
    public HashMap<UUID,UserData> userDataMap = new HashMap<>();

    public FlagManager(NPCTalkScriptV2  plugin){
        this.plugin = plugin;
    }

    // プレイヤーにフラグを追加
    public void addPlayerFlag(Player p, String flagName){
        if(playerHasFlag(p,flagName)){
            return;
        }
        plugin.sql.executeThread("INSERT INTO nts_flag (name,uuid,flag)  VALUES ('"+p.getName()+"','"+p.getUniqueId().toString()+"','"+flagName+"');",null);
        UserData uData;
        if(userDataMap.containsKey(p.getUniqueId())){
            uData = userDataMap.get(p.getUniqueId());
        }else{
            uData = new UserData(p.getUniqueId(),p.getName());
        }
        uData.addFlag(flagName);
        userDataMap.put(p.getUniqueId(),uData);
    }

    // プレイヤーからフラグを削除
    public void removePlayerFlag(Player p, String flagName){
        if(!playerHasFlag(p,flagName)){
            return;
        }
        plugin.sql.executeThread("DELETE FROM nts_flag WHERE uuid = '" + p.getUniqueId().toString() + "' AND flag = '"+flagName+"';",null);
        if(userDataMap.containsKey(p.getUniqueId())){
            UserData uData = userDataMap.get(p.getUniqueId());
            if(uData.hasFlag(flagName)){
                uData.removeFlag(flagName);
                userDataMap.put(p.getUniqueId(),uData);
            }
        }
    }

    // プレイヤーがflagNameのフラグを所持しているか
    public boolean playerHasFlag(Player p, String flagName){

        // キャッシュデータが存在しておりtrueならreturn(falseの場合は取得してないだけかもしれないので)
        if(userDataMap.containsKey(p.getUniqueId())&&userDataMap.get(p.getUniqueId()).hasCounter(flagName)){
            return true;
        }

        Query query = plugin.sql.query("SELECT * FROM nts_flag WHERE uuid = '"+p.getUniqueId().toString()+"' AND flag = '"+flagName+"';");
        ResultSet rs = query.getResultSet();
        if(rs==null){
            query.close();
            return false;
        }
        try {
            if(rs.next()){
                query.close();

                UserData uData;
                if(userDataMap.containsKey(p.getUniqueId())){
                    uData = userDataMap.get(p.getUniqueId());
                }else{
                    uData = new UserData(p.getUniqueId(),p.getName());
                }
                uData.addFlag(flagName);
                userDataMap.put(p.getUniqueId(),uData);

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
        return false;
    }


    // プレイヤーのカウンターをセット
    public void setCounter(Player p, String counterName, int count){
        if(hasCounter(p,counterName)){
            plugin.sql.execute("UPDATE nts_counter SET count = "+count+" WHERE uuid = '"+p.getUniqueId().toString()+"' AND c_name = '"+counterName+"';");
        }else{
            plugin.sql.execute("INSERT INTO nts_counter (name,uuid,c_name,count)  VALUES ('"+p.getName()+"','"+p.getUniqueId().toString()+"','"+counterName+"',"+count+");");
        }

        UserData uData;
        if(userDataMap.containsKey(p.getUniqueId())){
            uData = userDataMap.get(p.getUniqueId());
        }else{
            uData = new UserData(p.getUniqueId(),p.getName());
        }
        uData.setCounter(counterName,count);
        userDataMap.put(p.getUniqueId(),uData);
    }

    // プレイヤーのカウンターが存在するか
    public boolean hasCounter(Player p, String counterName){

        // キャッシュデータが存在しておりかつカウンタがtrueならreturn(falseの場合は取得していないだけかもしれないので)
        if(userDataMap.containsKey(p.getUniqueId())&&userDataMap.get(p.getUniqueId()).hasCounter(counterName)){
            return true;
        }

        Query query = plugin.sql.query("SELECT * FROM nts_counter WHERE uuid = '"+p.getUniqueId().toString()+"' AND c_name = '"+counterName+"';");
        ResultSet rs = query.getResultSet();
        if(rs==null){
            query.close();
            return false;
        }
        try {
            if(rs.next()){
                query.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
        return false;
    }

    // プレイヤーのカウンター数字を取得
    public int getCounter(Player p, String counterName){

        // キャッシュデータが存在していてかつカウンタが存在するならそれで対応
        if(userDataMap.containsKey(p.getUniqueId())&&userDataMap.get(p.getUniqueId()).hasCounter(counterName)){
            return userDataMap.get(p.getUniqueId()).getCounter(counterName);
        }

        Query query = plugin.sql.query("SELECT * FROM nts_counter WHERE uuid = '"+p.getUniqueId().toString()+"' AND c_name = '"+counterName+"';");
        ResultSet rs = query.getResultSet();
        if(rs==null){
            query.close();
            return Integer.MIN_VALUE;
        }
        try {
            if(rs.next()){
                int i = rs.getInt("count");
                query.close();

                UserData uData;
                if(userDataMap.containsKey(p.getUniqueId())){
                    uData = userDataMap.get(p.getUniqueId());
                }else{
                    uData = new UserData(p.getUniqueId(),p.getName());
                }
                uData.setCounter(counterName,i);
                userDataMap.put(p.getUniqueId(),uData);

                return i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
        return Integer.MIN_VALUE;
    }

    // プレイヤーのカウンターを削除
    public void removeCounter(Player p, String counterName){
        plugin.sql.execute("DELETE FROM nts_counter WHERE uuid = '"+p.getUniqueId().toString()+"' AND c_name = '" + counterName + "';");

        if(userDataMap.containsKey(p.getUniqueId())){
            UserData uData = userDataMap.get(p.getUniqueId());
            if(uData.hasCounter(counterName)){
                uData.removeCounter(counterName);
                userDataMap.put(p.getUniqueId(),uData);
            }
        }
    }

}
