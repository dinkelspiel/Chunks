package dev.keii.keiichunks.inventories;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class InventoryModifyChunk implements InventoryHolder {

    public static final Component Name = Component.text("\uF805\uEffc\uF80A\uF806\uF80C\uF822").color(NamedTextColor.WHITE);

    private ItemStack createGuiItem(String name)
    {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1000);
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    @NotNull
    public Inventory getInventory()
    {
        Inventory inventory = Bukkit.createInventory(this, 27, Name);

        for(int i = 0; i < 27; i++)
        {
            int slot = i;
            if(slot > 17)
            {
                slot -= 9;
            }
            if(slot > 8)
            {
                slot -= 9;
            }

            if(slot > 5)
            {
                inventory.setItem(i, createGuiItem("Unclaim"));
            } else if(slot > 2)
            {
                inventory.setItem(i, createGuiItem("Toggle TNT"));
            } else {
                inventory.setItem(i, createGuiItem("Modify Permissions"));
            }
        }

        return inventory;
    }
}
