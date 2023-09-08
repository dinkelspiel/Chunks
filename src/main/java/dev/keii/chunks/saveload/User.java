package dev.keii.chunks.saveload;

import dev.keii.chunks.Database;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    public long id;
    public String uuid;
    public String timestamp;
    public int claimPower;

    public User(long id, String uuid, String timestamp, int claimPower)
    {
        this.id = id;
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
}
