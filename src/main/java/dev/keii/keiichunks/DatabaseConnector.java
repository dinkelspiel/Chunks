package dev.keii.keiichunks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    public static String createDatabaseSqlSqlLite = "PRAGMA foreign_keys = ON;\n" +
            "CREATE TABLE IF NOT EXISTS `claim` (\n" +
            "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "  `user_id` INTEGER NOT NULL,\n" +
            "  `chunk_x` INTEGER NOT NULL,\n" +
            "  `chunk_z` INTEGER NOT NULL,\n" +
            "  `created_at` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `updated_at` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `allow_explosions` INTEGER NOT NULL DEFAULT 0\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS `claim_permission` (\n" +
            "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "  `user_id` INTEGER,\n" +
            "  `claim_id` INTEGER NOT NULL,\n" +
            "  `block_break` INTEGER NOT NULL DEFAULT 0,\n" +
            "  `block_place` INTEGER NOT NULL DEFAULT 0,\n" +
            "  `bucket_empty` INTEGER NOT NULL DEFAULT 0,\n" +
            "  `bucket_fill` INTEGER NOT NULL DEFAULT 0,\n" +
            "  `interact` INTEGER NOT NULL DEFAULT 0,\n" +
            "  `created_at` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `updated_at` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),\n" +
            "  FOREIGN KEY (`claim_id`) REFERENCES `claim`(`id`)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS `user` (\n" +
            "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "  `uuid` TEXT NOT NULL,\n" +
            "  `timestamp` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `claim_power` INTEGER NOT NULL DEFAULT 10\n" +
            ");\n" +
            "COMMIT;";

    public static String createDatabaseSqlMySQL =
            "START TRANSACTION;\n" +
            "CREATE TABLE IF NOT EXISTS `claim` (\n" +
            "  `id` bigint(20) UNSIGNED NOT NULL,\n" +
            "  `user_id` bigint(20) UNSIGNED NOT NULL,\n" +
            "  `chunk_x` smallint(6) NOT NULL,\n" +
            "  `chunk_z` smallint(6) NOT NULL,\n" +
            "  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),\n" +
            "  `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),\n" +
            "  `allow_explosions` tinyint(1) NOT NULL DEFAULT 0\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n" +
            "CREATE TABLE IF NOT EXISTS `claim_permission` (\n" +
            "  `id` bigint(20) UNSIGNED NOT NULL,\n" +
            "  `user_id` bigint(20) UNSIGNED DEFAULT NULL,\n" +
            "  `claim_id` bigint(20) UNSIGNED NOT NULL,\n" +
            "  `block_break` tinyint(1) NOT NULL DEFAULT 0,\n" +
            "  `block_place` tinyint(1) NOT NULL DEFAULT 0,\n" +
            "  `bucket_empty` tinyint(1) NOT NULL DEFAULT 0,\n" +
            "  `bucket_fill` tinyint(1) NOT NULL DEFAULT 0,\n" +
            "  `interact` tinyint(1) NOT NULL DEFAULT 0,\n" +
            "  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),\n" +
            "  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n" +
            "CREATE TABLE IF NOT EXISTS `user` (\n" +
            "  `id` bigint(20) UNSIGNED NOT NULL,\n" +
            "  `nickname` TEXT NOT NULL,\n" +
            "  `uuid` varchar(36) NOT NULL,\n" +
            "  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),\n" +
            "  `claim_power` int(10) UNSIGNED NOT NULL DEFAULT 10\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n" +

            "ALTER TABLE `claim`\n" +
            "  ADD PRIMARY KEY (`id`),\n" +
            "  ADD KEY `claim_user_id_foreign` (`user_id`);\n" +
            "ALTER TABLE `claim_permission`\n" +
            "  ADD PRIMARY KEY (`id`),\n" +
            "  ADD KEY `claim_permission_user_id_foreign` (`user_id`),\n" +
            "  ADD KEY `claim_permission_claim_id_foreign` (`claim_id`);\n" +
            "ALTER TABLE `user`\n" +
            "  ADD PRIMARY KEY (`id`);\n" +
            "ALTER TABLE `claim`\n" +
            "  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;\n" +
            "ALTER TABLE `claim_permission`\n" +
            "  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;\n" +
            "ALTER TABLE `user`\n" +
            "  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;\n" +
            "ALTER TABLE `claim`\n" +
            "  ADD CONSTRAINT `claim_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);\n" +
            "ALTER TABLE `claim_permission`\n" +
            "  ADD CONSTRAINT `claim_permission_claim_id_foreign` FOREIGN KEY (`claim_id`) REFERENCES `claim` (`id`),\n" +
            "  ADD CONSTRAINT `claim_permission_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);\n" +
            "COMMIT;";

//    public static boolean InitializeDatabase()
//    {
//        Connection connection = getConnection();
//
//        try {
//            Statement statement = connection.createStatement();
//
//            System.out.println("Database initialization succeeded!");
//
//            statement.execute(createDatabaseSqlMySQL);
//
//            return true;
//        } catch (SQLException e) {
//            Bukkit.getServer().sendMessage(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
//            return false;
//        }
//    };

//    public static Connection getConnection() {
//        try {
//            Class.forName("org.sqlite.JDBC");
//
//            final String url = "jdbc:sqlite:./plugins/KeiiChunks/database.db";
//
//            return DriverManager.getConnection(url);
//        } catch(Exception e)
//        {
//            Bukkit.getServer().sendMessage(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
//
//            return null;
//        }
//    }

    static String DB_URL = null;
    static String DB_NAME = null;
    static String DB_USER = null;
    static String DB_PASSWORD = null;

    public static boolean InitializeDatabase()
    {
        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();

            Bukkit.getServer().sendMessage(Component.text("Database initialization succeeded").color(NamedTextColor.GREEN));

            statement.execute(createDatabaseSqlMySQL);

            return true;
        } catch (SQLException e) {
            Bukkit.getServer().sendMessage(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
            return false;
        }
    };

    public static Connection getConnection() {
        try {
            final String url = DB_URL + DB_NAME;

            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            Bukkit.getServer().sendMessage(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
            return null;
        }
    }
}