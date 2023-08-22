package dev.keii.keiichunks.saveload;

public class ClaimPermission {
    public long id;
    public long userId;
    public long claimId;
    public boolean blockBreak;
    public boolean blockPlace;
    public boolean bucketEmpty;
    public boolean bucketFill;
    public boolean interact;
    public String createdAt;
    public String updatedAt;

    public ClaimPermission(long id, long userId, long claimId, boolean blockBreak, boolean blockPlace, boolean bucketEmpty, boolean bucketFill, boolean interact, String createdAt, String updatedAt)
    {
        this.id = id;
        this.userId = userId;
        this.claimId = claimId;
        this.blockBreak = blockBreak;
        this.bucketEmpty = bucketEmpty;
        this.bucketFill = bucketFill;
        this.interact = interact;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
