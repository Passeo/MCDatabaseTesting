package org.database.mc.passeodatabase;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PasseoDatabase extends JavaPlugin implements Listener {

    private final String url = "jdbc:mysql://localhost:3306/minecraft";
    private Connection connection;
    private static PasseoDatabase instance;
    private Map<UUID, PlayerData> playerData;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        try{
            connection = DriverManager.getConnection(url,"root", "");
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS minecraft_testing (UUID VARCHAR(36) NOT NULL, block_break INTEGER);");
        }catch (SQLException e){
            e.printStackTrace();
        }
        playerData = new HashMap<>();
        updateAll();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        getPlayerData(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerBreak(final BlockBreakEvent e){
        Player player = e.getPlayer();
        getPlayerData(player.getUniqueId()).setBlockBreak(1);

    }

    public void updateAll(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                playerData.values().forEach(PlayerData::update);
            }
        },0,  5);
    }

    public static PasseoDatabase getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public PlayerData getPlayerData(UUID uuid){
        if(playerData.containsKey(uuid)){
            return playerData.get(uuid);
        }
        PlayerData pd = new PlayerData(uuid);
        playerData.put(uuid, pd);
        return pd;
    }

    @Override
    public void onDisable() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
