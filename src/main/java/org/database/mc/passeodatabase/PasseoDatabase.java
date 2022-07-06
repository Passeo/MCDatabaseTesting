package org.database.mc.passeodatabase;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class PasseoDatabase extends JavaPlugin implements Listener {

    private final String url = "jdbc:mysql://localhost:3306/minecraft";
    private Connection connection;
    private static PasseoDatabase instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        try{
            connection = DriverManager.getConnection(url,"root", "");
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS minecraft_testing (UUID VARCHAR(36) NOT NULL);");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        PlayerData playerData = new PlayerData(e.getPlayer().getUniqueId());
        playerData.update();
    }

    public static PasseoDatabase getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
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
