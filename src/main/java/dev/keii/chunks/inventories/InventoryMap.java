package dev.keii.chunks.inventories;

import dev.keii.chunks.Database;
import dev.keii.chunks.models.Claim;
import dev.keii.chunks.models.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InventoryMap implements InventoryHolder {
    public HashMap<String, Integer> MapLookup = new HashMap<>();

    Player player;

    public InventoryMap(Player player)
    {
        MapLookup.put("wgswo", 1000);
        MapLookup.put("wggge", 1001);
        MapLookup.put("gssgo", 1002);
        MapLookup.put("sgswn", 1003);
        MapLookup.put("swgso", 1004);
        MapLookup.put("gwggo", 1005);
        MapLookup.put("wgsge", 1006);
        MapLookup.put("gwsge", 1007);
        MapLookup.put("wswge", 1008);
        MapLookup.put("gssse", 1009);
        MapLookup.put("sswsn", 1010);
        MapLookup.put("ggswo", 1011);
        MapLookup.put("sssge", 1012);
        MapLookup.put("gggwe", 1013);
        MapLookup.put("wwgwn", 1014);
        MapLookup.put("wwsgo", 1015);
        MapLookup.put("wwgso", 1016);
        MapLookup.put("swsgo", 1017);
        MapLookup.put("sggwn", 1018);
        MapLookup.put("wgwso", 1019);
        MapLookup.put("swswe", 1020);
        MapLookup.put("gsgwn", 1021);
        MapLookup.put("wggwn", 1022);
        MapLookup.put("sgwwo", 1023);
        MapLookup.put("sgwso", 1024);
        MapLookup.put("sgssn", 1025);
        MapLookup.put("gwwwe", 1026);
        MapLookup.put("gsgsn", 1027);
        MapLookup.put("wgwwn", 1028);
        MapLookup.put("wwssn", 1029);
        MapLookup.put("swgwo", 1030);
        MapLookup.put("sgsge", 1031);
        MapLookup.put("swswn", 1032);
        MapLookup.put("wssge", 1033);
        MapLookup.put("gwwge", 1034);
        MapLookup.put("gwssn", 1035);
        MapLookup.put("wggse", 1036);
        MapLookup.put("sssse", 1037);
        MapLookup.put("ssgge", 1038);
        MapLookup.put("sgsgn", 1039);
        MapLookup.put("ggwge", 1040);
        MapLookup.put("wwswn", 1041);
        MapLookup.put("wgwsn", 1042);
        MapLookup.put("wwsse", 1043);
        MapLookup.put("sgwgo", 1044);
        MapLookup.put("gwsgo", 1045);
        MapLookup.put("ggggo", 1046);
        MapLookup.put("ggswn", 1047);
        MapLookup.put("wwsgn", 1048);
        MapLookup.put("gggse", 1049);
        MapLookup.put("sswwn", 1050);
        MapLookup.put("wssso", 1051);
        MapLookup.put("swsge", 1052);
        MapLookup.put("gwsso", 1053);
        MapLookup.put("swwse", 1054);
        MapLookup.put("gwswe", 1055);
        MapLookup.put("gsggn", 1056);
        MapLookup.put("gsgse", 1057);
        MapLookup.put("gwsgn", 1058);
        MapLookup.put("wswso", 1059);
        MapLookup.put("ggsgn", 1060);
        MapLookup.put("sswgo", 1061);
        MapLookup.put("wsgwo", 1062);
        MapLookup.put("wwwgn", 1063);
        MapLookup.put("sgsgo", 1064);
        MapLookup.put("wssgn", 1065);
        MapLookup.put("wsgsn", 1066);
        MapLookup.put("swwge", 1067);
        MapLookup.put("swsse", 1068);
        MapLookup.put("wgsse", 1069);
        MapLookup.put("wggso", 1070);
        MapLookup.put("gssgn", 1071);
        MapLookup.put("ssggo", 1072);
        MapLookup.put("wwgwo", 1073);
        MapLookup.put("gwgsn", 1074);
        MapLookup.put("ggwso", 1075);
        MapLookup.put("ssgwe", 1076);
        MapLookup.put("swsgn", 1077);
        MapLookup.put("wgswe", 1078);
        MapLookup.put("swwwe", 1079);
        MapLookup.put("wwswo", 1080);
        MapLookup.put("sswwe", 1081);
        MapLookup.put("ssggn", 1082);
        MapLookup.put("ggwsn", 1083);
        MapLookup.put("wwwwn", 1084);
        MapLookup.put("wgwwo", 1085);
        MapLookup.put("sswge", 1086);
        MapLookup.put("ggwwe", 1087);
        MapLookup.put("ssswe", 1088);
        MapLookup.put("wsggn", 1089);
        MapLookup.put("gswwn", 1090);
        MapLookup.put("gsgge", 1091);
        MapLookup.put("wwwse", 1092);
        MapLookup.put("gwwsn", 1093);
        MapLookup.put("ggssn", 1094);
        MapLookup.put("wswwo", 1095);
        MapLookup.put("wgwgn", 1096);
        MapLookup.put("gwwse", 1097);
        MapLookup.put("wssgo", 1098);
        MapLookup.put("sggse", 1099);
        MapLookup.put("wgsgn", 1100);
        MapLookup.put("gwwgo", 1101);
        MapLookup.put("swwso", 1102);
        MapLookup.put("sggwo", 1103);
        MapLookup.put("wsgwe", 1104);
        MapLookup.put("gwwgn", 1105);
        MapLookup.put("gwsse", 1106);
        MapLookup.put("gsswn", 1107);
        MapLookup.put("gsgwo", 1108);
        MapLookup.put("sggwe", 1109);
        MapLookup.put("wswsn", 1110);
        MapLookup.put("swsso", 1111);
        MapLookup.put("wsswn", 1112);
        MapLookup.put("wwgse", 1113);
        MapLookup.put("swswo", 1114);
        MapLookup.put("wswwn", 1115);
        MapLookup.put("wwggo", 1116);
        MapLookup.put("gsswo", 1117);
        MapLookup.put("wsswo", 1118);
        MapLookup.put("ggwgo", 1119);
        MapLookup.put("swwsn", 1120);
        MapLookup.put("sgggn", 1121);
        MapLookup.put("gwwwo", 1122);
        MapLookup.put("sggso", 1123);
        MapLookup.put("wsggo", 1124);
        MapLookup.put("wgwgo", 1125);
        MapLookup.put("wssse", 1126);
        MapLookup.put("ggggn", 1127);
        MapLookup.put("swgsn", 1128);
        MapLookup.put("sgwwn", 1129);
        MapLookup.put("gwwwn", 1130);
        MapLookup.put("gswse", 1131);
        MapLookup.put("gswgo", 1132);
        MapLookup.put("gwwso", 1133);
        MapLookup.put("ssgwo", 1134);
        MapLookup.put("swggn", 1135);
        MapLookup.put("wwwsn", 1136);
        MapLookup.put("sgswo", 1137);
        MapLookup.put("wggwe", 1138);
        MapLookup.put("gwgge", 1139);
        MapLookup.put("sswso", 1140);
        MapLookup.put("ggswe", 1141);
        MapLookup.put("wwgwe", 1142);
        MapLookup.put("gsssn", 1143);
        MapLookup.put("sgswe", 1144);
        MapLookup.put("sssgo", 1145);
        MapLookup.put("sggsn", 1146);
        MapLookup.put("gwgse", 1147);
        MapLookup.put("swgwe", 1148);
        MapLookup.put("wwgsn", 1149);
        MapLookup.put("wggwo", 1150);
        MapLookup.put("wwwge", 1151);
        MapLookup.put("ssswn", 1152);
        MapLookup.put("ssswo", 1153);
        MapLookup.put("wgsgo", 1154);
        MapLookup.put("gggsn", 1155);
        MapLookup.put("gggwn", 1156);
        MapLookup.put("wwggn", 1157);
        MapLookup.put("ggwwo", 1158);
        MapLookup.put("gwggn", 1159);
        MapLookup.put("wgsso", 1160);
        MapLookup.put("wswwe", 1161);
        MapLookup.put("wsgge", 1162);
        MapLookup.put("wswgn", 1163);
        MapLookup.put("gswwo", 1164);
        MapLookup.put("gsggo", 1165);
        MapLookup.put("wgwge", 1166);
        MapLookup.put("swggo", 1167);
        MapLookup.put("gwswn", 1168);
        MapLookup.put("sgwsn", 1169);
        MapLookup.put("gsgwe", 1170);
        MapLookup.put("wwsge", 1171);
        MapLookup.put("gswso", 1172);
        MapLookup.put("wswse", 1173);
        MapLookup.put("sgwgn", 1174);
        MapLookup.put("ggsge", 1175);
        MapLookup.put("gsgso", 1176);
        MapLookup.put("wwsso", 1177);
        MapLookup.put("sgsso", 1178);
        MapLookup.put("gwgso", 1179);
        MapLookup.put("wsgse", 1180);
        MapLookup.put("swgse", 1181);
        MapLookup.put("gswgn", 1182);
        MapLookup.put("ggwwn", 1183);
        MapLookup.put("swwwn", 1184);
        MapLookup.put("swgwn", 1185);
        MapLookup.put("wwswe", 1186);
        MapLookup.put("sgwse", 1187);
        MapLookup.put("sswwo", 1188);
        MapLookup.put("wwwgo", 1189);
        MapLookup.put("sgsse", 1190);
        MapLookup.put("wgwwe", 1191);
        MapLookup.put("gswwe", 1192);
        MapLookup.put("gswge", 1193);
        MapLookup.put("ggsso", 1194);
        MapLookup.put("wswgo", 1195);
        MapLookup.put("gggge", 1196);
        MapLookup.put("sssso", 1197);
        MapLookup.put("wgswn", 1198);
        MapLookup.put("wgggo", 1199);
        MapLookup.put("ssssn", 1200);
        MapLookup.put("ssgso", 1201);
        MapLookup.put("wsgwn", 1202);
        MapLookup.put("wwwwe", 1203);
        MapLookup.put("ggwse", 1204);
        MapLookup.put("sggge", 1205);
        MapLookup.put("ssgse", 1206);
        MapLookup.put("swwwo", 1207);
        MapLookup.put("wwgge", 1208);
        MapLookup.put("wggsn", 1209);
        MapLookup.put("wgssn", 1210);
        MapLookup.put("wsgso", 1211);
        MapLookup.put("sswse", 1212);
        MapLookup.put("gggwo", 1213);
        MapLookup.put("sgggo", 1214);
        MapLookup.put("ggwgn", 1215);
        MapLookup.put("gswsn", 1216);
        MapLookup.put("wsssn", 1217);
        MapLookup.put("gggso", 1218);
        MapLookup.put("wgwse", 1219);
        MapLookup.put("gssso", 1220);
        MapLookup.put("swgge", 1221);
        MapLookup.put("ggsse", 1222);
        MapLookup.put("swssn", 1223);
        MapLookup.put("wwwso", 1224);
        MapLookup.put("sgwge", 1225);
        MapLookup.put("gwgwn", 1226);
        MapLookup.put("ggsgo", 1227);
        MapLookup.put("ssgsn", 1228);
        MapLookup.put("wwwwo", 1229);
        MapLookup.put("gwgwo", 1230);
        MapLookup.put("sgwwe", 1231);
        MapLookup.put("gwgwe", 1232);
        MapLookup.put("gwswo", 1233);
        MapLookup.put("gssge", 1234);
        MapLookup.put("wgggn", 1235);
        MapLookup.put("swwgn", 1236);
        MapLookup.put("ssgwn", 1237);
        MapLookup.put("sssgn", 1238);
        MapLookup.put("swwgo", 1239);
        MapLookup.put("gsswe", 1240);
        MapLookup.put("wsswe", 1241);
        MapLookup.put("sswgn", 1242);

        this.player = player;
    }

    public static final String Name = "\uF809\uEff3\uF80B\uF80B\uF80A\uF809\uF806";

    public char getLetterOfBiome(Block block)
    {
        Material highestBlockType = block.getWorld().getBlockAt(block.getX(), block.getWorld().getHighestBlockYAt(block.getLocation()), block.getZ()).getType();
        if(highestBlockType == Material.WATER || highestBlockType == Material.ICE || highestBlockType == Material.PACKED_ICE) {
            return 'w';
        } else if(highestBlockType == Material.SAND) {
            return 's';
        }

        Biome biome = block.getBiome();

        Biome[] waterBiomes = new Biome[] {Biome.RIVER, Biome.FROZEN_RIVER, Biome.WARM_OCEAN, Biome.LUKEWARM_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.OCEAN, Biome.DEEP_OCEAN, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.FROZEN_OCEAN, Biome.DEEP_FROZEN_OCEAN};
        Biome[] sandBiomes = new Biome[] {Biome.BADLANDS, Biome.DESERT, Biome.ERODED_BADLANDS, Biome.BEACH, Biome.SNOWY_BEACH};

        if(Arrays.stream(waterBiomes).toList().contains(biome))
        {
            return 'w';
        }

        if(Arrays.stream(sandBiomes).toList().contains(biome))
        {
            return 's';
        }

        return 'g';
    }

    public String getImageRepresentationOfChunk(Chunk chunk)
    {
        // Image pixels representation order
        // 1 2
        // 3 4

        String representation = "";

        representation += getLetterOfBiome(chunk.getBlock(12, 62, 12));
        representation += getLetterOfBiome(chunk.getBlock(3, 62, 12));
        representation += getLetterOfBiome(chunk.getBlock(12, 62, 3));
        representation += getLetterOfBiome(chunk.getBlock(3, 62, 3));

        Claim claim = Claim.fromChunk(chunk);

        User user = User.fromPlayer(player);

        if(claim == null)
        {
            representation += "n";
            return representation;
        }

        assert user != null;

        if(claim.getOwner().getId() == user.getId())
        {
            representation += "o";
        } else {
            representation += "e";
        }

        return representation;
    }

    public String getDirectionMarker() {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return "\ueff6";
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            return "\ueff7";
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return "\ueff8";
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            return "\ueff9";
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return "\ueffa";
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
            return "\ueffb";
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return "\ueff4";
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return "\ueff5";
        }
        if ((337.5D <= rotation) && (rotation < 360.0D)) {
            return "\ueff6";
        }
        return null;
    }

    private Vector2i getClaimPower() {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String userQuery = "SELECT id FROM user WHERE uuid = \"" + player.getUniqueId() + "\"";
            ResultSet userResultSet = statement.executeQuery(userQuery);

            if(!userResultSet.next()) {
                userResultSet.close();
                statement.close();
                connection.close();
                return new Vector2i(0, 0);
            }

            int userId = userResultSet.getInt("id");

            ResultSet claimPowerResultSet = statement.executeQuery("SELECT claim_power FROM user WHERE id = " + userId);

            claimPowerResultSet.next();

            int claimPower = claimPowerResultSet.getInt("claim_power");

            claimPowerResultSet.close();

            claimPowerResultSet = statement.executeQuery("SELECT COUNT(id) as count FROM `claim` WHERE user_id = " + userId);
            claimPowerResultSet.next();

            return new Vector2i(claimPowerResultSet.getInt("count"), claimPower);
        } catch (SQLException e) {
            Bukkit.getServer().broadcast(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
        }

        return new Vector2i(0, 0);
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        // eff4
        // effb

        Vector2i claimPower = getClaimPower();

        Inventory mapInventory = Bukkit.createInventory(this, 54, Component.text(Name + getDirectionMarker()).color(NamedTextColor.WHITE).append(Component.text(" Power " + claimPower.x + "/" + claimPower.y).color(NamedTextColor.BLACK)));

        for(int i = 0; i < 54; i++)
        {
            int x = i % 9 - 4;
            int y = i / 9 - 3;

            int playerChunkX = player.getChunk().getX();
            int playerChunkY = player.getChunk().getZ();

            int chunkX = playerChunkX - x;
            int chunkY = playerChunkY - y;

            int chunkWorldX = chunkX * 16;
            int chunkWorldY = chunkY * 16;

            Chunk chunk = player.getWorld().getChunkAt(new Location(player.getWorld(), chunkWorldX, 0, chunkWorldY));

            String stringRepresentation = getImageRepresentationOfChunk(chunk);
            int representation = MapLookup.get(stringRepresentation);

            ItemStack mapItem = new ItemStack(Material.MAP);
            ItemMeta mapItemMeta = mapItem.getItemMeta();
            mapItemMeta.setCustomModelData(representation);

            List<Component> lore = new ArrayList<>();

            lore.add(Component.text("Biome: ").append(Component.translatable(chunk.getBlock(0, 0, 0).getBiome().translationKey())));
            lore.add(Component.text("ChunkX: " + chunkX + ", ChunkZ: " + chunkY));
            lore.add(Component.text("WorldX: " + chunkWorldX + ", WorldZ: " + chunkWorldY));

            Claim claim = Claim.fromChunk(chunk);

            if(claim != null) {
                switch (stringRepresentation.charAt(stringRepresentation.length() - 1)) {
                    case 'n' -> mapItemMeta.displayName(Component.text("Click to claim!").color(NamedTextColor.YELLOW));
                    case 'o' -> {
                        mapItemMeta.displayName(Component.text("You own this chunk").color(NamedTextColor.AQUA));
                        lore.add(0, Component.text("Click to modify").color(NamedTextColor.YELLOW));
                    }
                    case 'e' -> mapItemMeta.displayName(Component.text(claim.getOwner().getNickname() + "'s chunk").color(NamedTextColor.RED));
                }
            } else {
                mapItemMeta.displayName(Component.text("Click to claim!").color(NamedTextColor.YELLOW));
            }

            if(i == 31)
            {
                lore.add(0, Component.text("You are here!").color(NamedTextColor.RED));
            }

            mapItemMeta.lore(lore);
            mapItem.setItemMeta(mapItemMeta);
            mapInventory.setItem(i, mapItem);
        }

        return mapInventory;
    }
}
