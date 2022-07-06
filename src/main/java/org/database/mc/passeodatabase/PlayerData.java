package org.database.mc.passeodatabase;

import com.mysql.cj.protocol.Resultset;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public record PlayerData(UUID uuid) {

    public void update() {
        Connection connection = PasseoDatabase.getInstance().getConnection();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT UUID FROM minecraft_testing WHERE UUID = '" + uuid.toString() + "';");
            if (!rs.next()) {
                connection.createStatement().executeUpdate("INSERT INTO minecraft_testing (UUID) VALUES ('" + uuid.toString() + "');");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
