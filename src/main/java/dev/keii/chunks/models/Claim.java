package dev.keii.chunks.models;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.Database;
import dev.keii.chunks.error.Failure;
import dev.keii.chunks.error.Result;
import dev.keii.chunks.error.Success;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    public static Claim fromChunk(Chunk chunk)
    {
        for(Claim claim : Chunks.claims)
        {
            if(claim.getChunkX() == chunk.getX() && claim.getChunkZ() == chunk.getZ() && claim.getWorld().equals(chunk.getWorld().getName()))
            {
                return claim;
            }
        }
        return null;
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

    public static boolean claim(Player player, Chunk chunk)
    {
        if(Claim.fromChunk(chunk) != null)
        {
            return false;
        }

        User user = User.fromUuid(player.getUniqueId().toString());

        if(user == null)
        {
            return false;
        }

        Claim[] claims = user.getClaims();

        if(claims.length >= user.getClaimPower())
        {
            player.sendMessage(Component.text("Not enough power to claim chunk").color(NamedTextColor.RED));
            return false;
        }

        Claim claim = new Claim(Chunks.claimsAutoIncrement++, user.getId(), chunk.getX(), chunk.getZ(), chunk.getWorld(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), false);
        Chunks.claims.add(claim);
        claim.showChunkPerimeter(player);

        var result = claim.addChunkPermissionsForUser(player, null);

        if(result instanceof Failure)
        {
            player.sendMessage(Component.text(result.getMessage()));
        }

        return true;
    }

    public boolean unClaim(Player player)
    {
        User user = User.fromPlayer(player);

        if(user == null)
        {
            return false;
        }

        if(getUserID() != user.getId())
        {
            return false;
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
        return true;
    }

    public boolean toggleExplosionPolicy(Player player)
    {
        User user = User.fromPlayer(player);

        if(user == null)
        {
            return false;
        }

        if(getUserID() != user.getId())
        {
            return false;
        }

        setAllowExplosions(!getAllowExplosions());

        return true;
    }

    public Result addChunkPermissionsForUser(Player player, @Nullable User targetUser)
    {
        User user = User.fromUuid(player.getUniqueId().toString());

        if(user == null)
        {
            return new Failure("No user exists with uuid");
        }

        if(getUserID() != user.getId())
        {
            return new Failure("Player doesn't have permission to update chunk");
        }

        if(targetUser == null)
        {
            ClaimPermission claimPermission = ClaimPermission.get(null, this);
            if(claimPermission != null)
            {
                return new Failure("Everyone already exists on chunk");
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

            return new Success("Created everyone permission");
        }

        if(targetUser.getId() == user.getId())
        {
            return new Failure("Target can't be the same as user");
        }

        ClaimPermission claimPermission = ClaimPermission.get(targetUser, this);

        if(claimPermission != null)
        {
            return new Failure("Target permission already exists on chunk");
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

        return new Success("Created permission");
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

    public boolean setPermission(Player player, @Nullable User targetUser, ChunkPermission permission, boolean value)
    {
        User user = User.fromPlayer(player);

        if(user == null)
        {
            return false;
        }

        if(getUserID() != user.getId())
        {
            return false;
        }

        ClaimPermission claimPermission = ClaimPermission.get(targetUser, this);

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

        return true;
    }

    @Nullable
    public ClaimPermission getPermissionsForUser(User user)
    {
        return ClaimPermission.get(user, this);
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
