package org.database.mc.passeodatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerData {

    private final DBConnector connector;
    private final UUID uuid;
    private int blockBreak;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.connector = new DBConnector();
    }

    public void update() {
        try {
            ResultSet rs = connector.getTypes(DBConnector.QueryTypes.GET_UUID, uuid.toString());
            if (!rs.next()) {
                connector.SetTypes(DBConnector.QueryTypes.ADD_UUID, uuid.toString(), "0");
            }
            ResultSet rs1 = connector.getTypes(DBConnector.QueryTypes.GET_BLOCK_BREAK, uuid.toString());
            while(rs1.next()){
                int i = rs1.getInt("block_break");
                setBlockBreak(i);
                connector.SetTypes(DBConnector.QueryTypes.SET_BLOCK_BREAK, String.valueOf(getBlockBreak()), uuid.toString());
                blockBreak = 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBlockBreak() {
        return blockBreak;
    }

    public void setBlockBreak(int blockBreak) {
        this.blockBreak += blockBreak;
    }
}
