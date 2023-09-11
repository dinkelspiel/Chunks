package dev.keii.chunks.models;

import dev.keii.chunks.Chunks;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    public static User fromId(int id)
    {
        for(User user : Chunks.users)
        {
            if(user.getId() == id)
            {
                return user;
            }
        }
        return null;
    }

    @Nullable
    public static User fromNickname(String nickname)
    {
        for(User user : Chunks.users)
        {
            if(user.getNickname().equals(nickname))
            {
                return user;
            }
        }
        return null;
    }

    @Nullable
    public static User fromUuid(String uuid)
    {
        for(User user : Chunks.users)
        {
            if(user.getUuid().toString().equals(uuid))
            {
                return user;
            }
        }
        return null;
    }

    @Nullable
    public static User fromPlayer(Player player)
    {
        for(User user : Chunks.users)
        {
            if(user.getUuid().toString().equals(player.getUniqueId().toString()))
            {
                return user;
            }
        }
        return null;
    }

    public Claim[] getClaims()
    {
        List<Claim> claims = new ArrayList<>();
        for(Claim claim : Chunks.claims)
        {
            if(claim.getUserID() == getId())
            {
                claims.add(claim);
            }
        }

        return claims.toArray(Claim[]::new);
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
