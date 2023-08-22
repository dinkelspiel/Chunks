package dev.keii.keiichunks.api;

import java.sql.Timestamp;

public class ApiChunk {
    private long id;
    private long userID;
    private int chunkX;
    private int chunkZ;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean allowExplosions;

    public ApiChunk(long id, long userID, int chunkX, int chunkZ, Timestamp createdAt, Timestamp updatedAt, boolean allowExplosions) {
        this.id = id;
        this.userID = userID;
        this.chunkZ = chunkZ;
        this.chunkX = chunkX;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.allowExplosions = allowExplosions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
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

    public boolean isAllowExplosions() {
        return allowExplosions;
    }

    public void setAllowExplosions(boolean allowExplosions) {
        this.allowExplosions = allowExplosions;
    }
}
