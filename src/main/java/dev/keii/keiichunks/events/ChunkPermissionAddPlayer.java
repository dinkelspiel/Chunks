package dev.keii.keiichunks.events;

import org.bukkit.Chunk;

public class ChunkPermissionAddPlayer extends ChatListener {
    Chunk chunk;

    public ChunkPermissionAddPlayer(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }
}