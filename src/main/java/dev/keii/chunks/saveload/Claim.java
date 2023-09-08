package dev.keii.chunks.saveload;

public class Claim {
    public long id;
    public long userId;
    public long chunkX;
    public long chunkZ;
    public String createdAt;
    public String updatedAt;
    public boolean allowExplosions;

    public Claim(long id, long userId, long chunkX, long chunkZ, String createdAt, String updatedAt, boolean allowExplosions)
    {
        this.id = id;
        this.userId = userId;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.allowExplosions = allowExplosions;
    }
}
