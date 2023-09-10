package dev.keii.chunks.database;

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
