package dev.keii.chunks.database;

import dev.keii.chunks.Database;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.UUID;

public class User {
    private int id;
    private String nickname;
    private UUID uuid;
    private Timestamp timestamp;
    private int claimPower;

    public User(int id, String nickname, UUID uuid, Timestamp timestamp, int claimPower) {
        this.id = id;
        this.nickname = nickname;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.claimPower = claimPower;
    }


    @Nullable
    public static String getUUIDFromID(int id)
    {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM user WHERE id = " + id;
            ResultSet userResultSet = statement.executeQuery(sql);

            if (!userResultSet.next()) {
                userResultSet.close();
                statement.close();
                connection.close();
                return null;
            }

            String uuid = userResultSet.getString("uuid");

            userResultSet.close();
            statement.close();
            connection.close();
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    public static String getNicknameFromId(int id)
    {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM user WHERE id = " + id;
            ResultSet userResultSet = statement.executeQuery(sql);

            if (!userResultSet.next()) {
                userResultSet.close();
                statement.close();
                connection.close();
                return null;
            }

            String nickname = userResultSet.getString("nickname");

            userResultSet.close();
            statement.close();
            connection.close();
            return nickname;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    public static String getNicknameFromUUID(String uuid)
    {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM user WHERE uuid = \"" + uuid + "\"";
            ResultSet userResultSet = statement.executeQuery(sql);

            if (!userResultSet.next()) {
                userResultSet.close();
                statement.close();
                connection.close();
                return null;
            }

            String nickname = userResultSet.getString("nickname");

            userResultSet.close();
            statement.close();
            connection.close();
            return nickname;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getClaimPower() {
        return claimPower;
    }

    public void setClaimPower(int claimPower) {
        this.claimPower = claimPower;
    }
}
