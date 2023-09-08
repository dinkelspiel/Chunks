package dev.keii.chunks;

import dev.keii.chunks.saveload.Claim;
import dev.keii.chunks.saveload.ClaimPermission;
import dev.keii.chunks.saveload.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.joml.Vector2i;

import java.sql.*;

public class Database {
    public static String createDatabaseSQL =
            """
                    START TRANSACTION;
                    CREATE TABLE IF NOT EXISTS `claim` (
                      `id` bigint(20) UNSIGNED NOT NULL,
                      `user_id` bigint(20) UNSIGNED NOT NULL,
                      `chunk_x` smallint(6) NOT NULL,
                      `chunk_z` smallint(6) NOT NULL,
                      `world` varchar(32) NOT NULL,
                      `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
                      `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),
                      `allow_explosions` tinyint(1) NOT NULL DEFAULT 0
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    CREATE TABLE IF NOT EXISTS `claim_permission` (
                      `id` bigint(20) UNSIGNED NOT NULL,
                      `user_id` bigint(20) UNSIGNED DEFAULT NULL,
                      `claim_id` bigint(20) UNSIGNED NOT NULL,
                      `block_break` tinyint(1) NOT NULL DEFAULT 0,
                      `block_place` tinyint(1) NOT NULL DEFAULT 0,
                      `bucket_empty` tinyint(1) NOT NULL DEFAULT 0,
                      `bucket_fill` tinyint(1) NOT NULL DEFAULT 0,
                      `interact` tinyint(1) NOT NULL DEFAULT 0,
                      `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
                      `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    CREATE TABLE IF NOT EXISTS `user` (
                      `id` bigint(20) UNSIGNED NOT NULL,
                      `nickname` TEXT NOT NULL,
                      `uuid` varchar(36) NOT NULL,
                      `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
                      `claim_power` int(10) UNSIGNED NOT NULL DEFAULT 10
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    ALTER TABLE `claim`
                      ADD PRIMARY KEY (`id`),
                      ADD KEY `claim_user_id_foreign` (`user_id`);
                    ALTER TABLE `claim_permission`
                      ADD PRIMARY KEY (`id`),
                      ADD KEY `claim_permission_user_id_foreign` (`user_id`),
                      ADD KEY `claim_permission_claim_id_foreign` (`claim_id`);
                    ALTER TABLE `user`
                      ADD PRIMARY KEY (`id`);
                    ALTER TABLE `claim`
                      MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;
                    ALTER TABLE `claim_permission`
                      MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;
                    ALTER TABLE `user`
                      MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
                    ALTER TABLE `claim`
                      ADD CONSTRAINT `claim_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
                    ALTER TABLE `claim_permission`
                      ADD CONSTRAINT `claim_permission_claim_id_foreign` FOREIGN KEY (`claim_id`) REFERENCES `claim` (`id`),
                      ADD CONSTRAINT `claim_permission_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
                    COMMIT;""";

    static String DB_URL = null;
    static String DB_NAME = null;
    static String DB_USER = null;
    static String DB_PASSWORD = null;

    public static void initializeDatabase()
    {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            Chunks.sendMessageToStaff(Component.text("Database initialization succeeded").color(NamedTextColor.GREEN));

            statement.execute(createDatabaseSQL);

        } catch (SQLException e) {
            Chunks.sendMessageToStaff(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
        }
    };

    public static Connection getConnection() {
        try {
            final String url = DB_URL + DB_NAME;

            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            Chunks.sendMessageToStaff(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
            return null;
        }
    }
}