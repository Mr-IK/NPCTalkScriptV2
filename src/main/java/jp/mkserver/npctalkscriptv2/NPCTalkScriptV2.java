package jp.mkserver.npctalkscriptv2;

import jp.mkserver.npctalkscriptv2.data.FlagManager;
import jp.mkserver.npctalkscriptv2.util.VaultManager;
import jp.mkserver.npctalkscriptv2.util.sql.SQLManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class NPCTalkScriptV2 extends JavaPlugin {

    public SQLManager sql;
    public FileConfiguration config;
    public FlagManager flag;
    public VaultManager vault;
    public NPCScript script;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        sql = new SQLManager(this,"NTScriptV2");
        flag = new FlagManager(this);
        vault = new VaultManager(this);
        script = new NPCScript(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
