package dev.keii.chunks.models;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.error.Result;
import kotlin.ResultKt;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Claim {
    private int id;
    private int userID;
    private int chunkX;
    private int chunkZ;
    private World world;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean allowExplosions;

    public Claim(int id, int userID, int chunkX, int chunkZ, World world, Timestamp createdAt, Timestamp updatedAt, boolean allowExplosions) {
        this.id = id;
        this.userID = userID;
        this.chunkZ = chunkZ;
        this.chunkX = chunkX;
        this.world = world;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.allowExplosions = allowExplosions;
    }

    @Nullable
    public static Result<Claim, String> fromChunk(Chunk chunk)
    {
        for(Claim claim : Chunks.claims)
        {
            if(claim.getChunkX() == chunk.getX() && claim.getChunkZ() == chunk.getZ() && claim.getWorld().getName().equals(chunk.getWorld().getName()))
            {
                return Result.success(claim);
            }
        }
        return Result.failure("Couldn't find claim");
    }

    public static boolean isPlayerOwner(Player player, Chunk chunk)
    {
        User user = User.fromUuid(player.getUniqueId().toString());

        if(user == null)
        {
            return false;
        }

        Claim claim = fromChunk(chunk);

        if(claim == null)
        {
            return false;
        }

        if(claim.getUserID() == user.getId())
        {
            return true;
        }
        return false;
    }

    @NotNull
    public User getOwner()
    {
        for(User user : Chunks.users)
        {
            if(user.getId() == getUserID())
            {
                return user;
            }
        }
        return null;
    }

    public static Result<String, String> claim(Player player, Chunk chunk)
    {
        if(Claim.fromChunk(chunk) != null)
        {
            return Result.failure("Claim already exists at chunk");
        }

        User user = User.fromPlayer(player);

        if(user == null)
        {
            return Result.failure("No user found for player");
        }

        Claim[] claims = user.getClaims();

        if(claims.length >= user.getClaimPower())
        {
            return Result.failure("Not enough power to claim chunk" + claims.length + " " + user.getClaimPower());
        }

        Claim claim = new Claim(Chunks.claimsAutoIncrement++, user.getId(), chunk.getX(), chunk.getZ(), chunk.getWorld(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), false);
        Chunks.claims.add(claim);
        claim.showChunkPerimeter(player);

        claim.addChunkPermissionsForUser(player, null).match(
                success -> true,
                failure -> {
                    player.sendMessage(Component.text(failure));
                    return true;
                }
        );

        return Result.success("Claimed chunk");
    }

    public Result<String, String> unClaim(Player player)
    {
        User user = User.fromPlayer(player);

        if(user == null)
        {
            return Result.failure("No user found for player");
        }

        if(getUserID() != user.getId())
        {
            return Result.failure("User isn't owner for chunk");
        }

        List<Integer> removeList = new ArrayList<>();
        for(var i = 0; i < Chunks.claimPermissions.size(); i++)
        {
            if(Chunks.claimPermissions.get(i).getClaimId() == getId())
            {
                removeList.add(i);
            }
        }

        removeList.sort(Comparator.reverseOrder());
        for(Integer idx : removeList)
        {
            Chunks.claimPermissions.remove((int)idx);
        }

        Chunks.claims.removeIf((Claim c) -> c.getUserID() == user.getId() && c.getChunkX() == getChunkX() && c.getChunkZ() == getChunkZ() && c.getWorld().getName().equals(getWorld().getName()));
        return Result.success("Unclaimed chunk");
    }

    public Result<Boolean, String> toggleExplosionPolicy(Player player)
    {
        User user = User.fromPlayer(player);

        if(user == null)
        {
            return Result.failure("No user found for player");
        }

        if(getUserID() != user.getId())
        {
            return Result.failure("User isn't owner for chunk");
        }

        setAllowExplosions(!getAllowExplosions());

        return Result.success(getAllowExplosions());
    }

    public Result<String, String> addChunkPermissionsForUser(Player player, @Nullable User targetUser)
    {
        User user = User.fromUuid(player.getUniqueId().toString());

        if(user == null)
        {
            return Result.failure("No user exists with uuid");
        }

        if(getUserID() != user.getId())
        {
            return Result.failure("Player doesn't have permission to update chunk");
        }

        if(targetUser == null)
        {
            ClaimPermission claimPermission = ClaimPermission.get(null, this).match(
                    success -> success,
                    failure -> null
            );
            if(claimPermission != null)
            {
                return Result.failure("Everyone already exists on chunk");
            }

            Chunks.claimPermissions.add(new ClaimPermission(
                    Chunks.claimPermissionsAutoIncrement++,
                    null,
                    getId(),
                    false,
                    false,
                    false,
                    false,
                    false,
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis())
            ));

            return Result.success("Created everyone permission");
        }

        if(targetUser.getId() == user.getId())
        {
            return Result.failure("Target can't be the same as user");
        }

        ClaimPermission claimPermission = ClaimPermission.get(targetUser, this).match(
                success -> success,
                failure -> null
        );

        if(claimPermission != null)
        {
            return Result.failure("Target permission already exists on chunk");
        }

        Chunks.claimPermissions.add(new ClaimPermission(
                Chunks.claimPermissionsAutoIncrement++,
                targetUser.getId(),
                getId(),
                false,
                false,
                false,
                false,
                false,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));

        return Result.success("Created permission");
    }

    private void showChunkPerimeter(Player player) {
        int worldX = getChunkX() * 16;
        int worldZ = getChunkZ() * 16;

        HashMap<Location, BlockData> blockChanges = new HashMap<>();

        for (var i = 0; i < 16; i++) {
            if(i > 1 || i < 16)
            {
                continue;
            }

            int x = worldX + i;
            int z = worldZ;
            int y = getWorld().getHighestBlockYAt(x, z);

            BlockData bd = Material.GOLD_BLOCK.createBlockData();

            blockChanges.put(new Location(getWorld(), x, y, z), bd);
        }

        for (var i = 0; i < 15; i++) {
            if(i > 1 || i < 16)
            {
                continue;
            }

            int x = worldX + i;
            int z = worldZ + 15;
            int y = getWorld().getHighestBlockYAt(x, z);

            BlockData bd = Material.GOLD_BLOCK.createBlockData();

            blockChanges.put(new Location(getWorld(), x, y, z), bd);
        }

        for (var i = 0; i < 15; i++) {
            if(i > 1 || i < 16)
            {
                continue;
            }

            int x = worldX;
            int z = worldZ + i;
            int y = getWorld().getHighestBlockYAt(x, z);

            BlockData bd = Material.GOLD_BLOCK.createBlockData();

            blockChanges.put(new Location(getWorld(), x, y, z), bd);
        }

        for (var i = 0; i < 16; i++) {
            if(i > 1 || i < 16)
            {
                continue;
            }

            int x = worldX + 15;
            int z = worldZ + i;
            int y = getWorld().getHighestBlockYAt(x, z);

            BlockData bd = Material.GOLD_BLOCK.createBlockData();

            blockChanges.put(new Location(getWorld(), x, y, z), bd);
        }

        player.sendMultiBlockChange(blockChanges);
    }

    public enum ChunkPermission {
        BlockBreak,
        BlockPlace,
        BucketEmpty,
        BucketFill,
        Interact
    }

    public static String getChunkPermissionString(ChunkPermission perm)
    {
        switch(perm) {
            case BucketEmpty -> {
                return "bucket_empty";
            }
            case BucketFill -> {
                return "bucket_fill";
            }
            case Interact -> {
                return "interact";
            }
            case BlockBreak -> {
                return "block_break";
            }
            case BlockPlace -> {
                return "block_place";
            }
        }
        return "";
    }

    public Result<String, String> setPermission(Player player, @Nullable User targetUser, ChunkPermission permission, boolean value)
    {
        User user = User.fromPlayer(player);

        if(user == null)
        {
            return Result.failure("No user found for player");
        }

        if(getUserID() != user.getId())
        {
            return Result.failure("User isn't chunk owner");
        }

        ClaimPermission claimPermission = ClaimPermission.get(targetUser, this).match(
                success -> success,
                failure -> null
        );

        if(claimPermission == null)
        {
            claimPermission = new ClaimPermission(
                    Chunks.claimPermissionsAutoIncrement++,
                    targetUser != null ? targetUser.getId() : null,
                    getId(),
                    false,
                    false,
                    false,
                    false,
                    false,
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis())
            );
            Chunks.claimPermissions.add(claimPermission);
        }

        switch(permission) {
            case Interact -> claimPermission.setInteract(value);
            case BlockBreak -> claimPermission.setBlockBreak(value);
            case BlockPlace -> claimPermission.setBlockPlace(value);
            case BucketEmpty -> claimPermission.setBucketEmpty(value);
            case BucketFill -> claimPermission.setBucketFill(value);
        }

        return Result.success("Added permissions for user");
    }

    @Nullable
    public ClaimPermission getPermissionsForUser(User user)
    {
        return ClaimPermission.get(user, this).match(
                success -> success,
                failure -> null
        );
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

    public World getWorld()
    {
        return world;
    }

    public Chunk getChunk()
    {
        return getWorld().getChunkAt(getChunkX(), getChunkZ());
    }

    public void setWorld(World world)
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
