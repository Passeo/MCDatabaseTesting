package org.database.mc.passeodatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
https://github.com/BetonQuest/BetonQuest/blob/master/src/main/java/org/betonquest/betonquest/database/Connector.java
*/
public class DBConnector {

    private Connection connection;

    public DBConnector() {
        connection = PasseoDatabase.getInstance().getConnection();
    }

    public ResultSet getTypes(final QueryTypes queryTypes, String... args){
        final String types = switch (queryTypes) {
            case GET_UUID -> "SELECT UUID FROM minecraft_testing WHERE UUID = ?";

            case GET_BLOCK_BREAK -> "SELECT block_break FROM minecraft_testing WHERE UUID = ?";

            default -> "SELECT 1";
        };

        try {
            final PreparedStatement statement = connection.prepareStatement(types);
            for (int i = 0; i < args.length; i++) {
                statement.setString(i + 1, args[i]);
            }
            return statement.executeQuery();
        } catch (final SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SetTypes(final QueryTypes queryTypes, String... args){
        final String types = switch (queryTypes) {

            //adding UUID data to SQL
            case ADD_UUID -> "INSERT INTO minecraft_testing (UUID, block_break) VALUES (?, ?);";

            case SET_BLOCK_BREAK -> "UPDATE minecraft_testing SET block_break = ? WHERE UUID = ?;";

            default -> "SELECT 1";
        };

        try (PreparedStatement statement = connection.prepareStatement(types)) {
            for (int i = 0; i < args.length; i++) {
                statement.setString(i + 1, args[i]);
            }
            statement.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }

    }

    //Still use 1 enum class because I lazy to create another one
    public enum QueryTypes{
        GET_UUID, GET_BLOCK_BREAK,

        ADD_UUID, SET_BLOCK_BREAK
    }
}
