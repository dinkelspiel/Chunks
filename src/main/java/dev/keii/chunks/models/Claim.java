package dev.keii.chunks.models;

import dev.keii.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Timestamp;

public class Claim {
    private int id;
    private int userID;
    private int chunkX;
    private int chunkZ;
    private String world;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean allowExplosions;

    public Claim(int id, int userID, int chunkX, int chunkZ, String world, Timestamp createdAt, Timestamp updatedAt, boolean allowExplosions) {
        this.id = id;
        this.userID = userID;
        this.chunkZ = chunkZ;
        this.chunkX = chunkX;
        this.world = world;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.allowExplosions = allowExplosions;
    }

    public static boolean isPlayerOwner(Player player, Chunk chunk)
    {
        User user = User.fromUuid(player.getUniqueId().toString());

        if(user == null)
        {
            return false;
        }

        for(Claim claim : Chunks.claims)
        {
            if(claim.getChunkX() == chunk.getX() && claim.getChunkZ() == chunk.getZ() && claim.getWorld().equals(chunk.getWorld().getName()) && claim.getUserID() == user.getId())
            {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static User getOwner(Chunk chunk)
    {
        for(Claim claim : Chunks.claims)
        {
            if(claim.getChunkX() == chunk.getX() && claim.getChunkZ() == chunk.getZ() && claim.getWorld().equals(chunk.getWorld().getName()))
            {
                for(User user : )
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public String getWorld()
    {
        return world;
    }

    public void setWorld(String world)
    {
        this.world = world;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean getAllowExplosions() {
        return allowExplosions;
    }

    public void setAllowExplosions(boolean allowExplosions) {
        this.allowExplosions = allowExplosions;
    }
}
