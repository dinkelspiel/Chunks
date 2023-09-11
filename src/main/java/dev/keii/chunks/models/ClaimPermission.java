package dev.keii.chunks.models;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.error.Result;

import javax.annotation.Nullable;
import java.sql.Timestamp;

public class ClaimPermission {
    private int id;

    @Nullable
    private Integer userId;
    private int claimId;
    private boolean blockBreak;
    private boolean blockPlace;
    private boolean bucketEmpty;
    private boolean bucketFill;
    private boolean interact;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ClaimPermission(int id, Integer userId, int claimId, boolean blockBreak, boolean blockPlace, boolean bucketEmpty, boolean bucketFill, boolean interact, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.claimId = claimId;
        this.blockBreak = blockBreak;
        this.blockPlace = blockPlace;
        this.bucketEmpty = bucketEmpty;
        this.bucketFill = bucketFill;
        this.interact = interact;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Result<ClaimPermission, String> get(@Nullable User user, Claim claim)
    {
        for(ClaimPermission claimPermission : Chunks.claimPermissions)
        {
            if(user == null) {
                if (claim.getId() == claimPermission.getClaimId() && claimPermission.getUserId() == null)
                {
                    return Result.success(claimPermission);
                }
            } else {
                if(claim.getId() == claimPermission.getClaimId() && user.getId() == claimPermission.getUserId())
                {
                    return Result.success(claimPermission);
                }
            }
        }
        return Result.failure("No claim permission found");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public boolean getBlockBreak() {
        return blockBreak;
    }

    public void setBlockBreak(boolean blockBreak) {
        this.blockBreak = blockBreak;
    }

    public boolean getBlockPlace() {
        return blockPlace;
    }

    public void setBlockPlace(boolean blockPlace) {
        this.blockPlace = blockPlace;
    }

    public boolean getBucketEmpty() {
        return bucketEmpty;
    }

    public void setBucketEmpty(boolean bucketEmpty) {
        this.bucketEmpty = bucketEmpty;
    }

    public boolean getBucketFill() {
        return bucketFill;
    }

    public void setBucketFill(boolean bucketFill) {
        this.bucketFill = bucketFill;
    }

    public boolean getInteract() {
        return interact;
    }

    public void setInteract(boolean interact) {
        this.interact = interact;
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
}
