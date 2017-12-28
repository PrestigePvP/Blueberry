package me.missionary.blueberry.utils.inventory.opener;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.utils.inventory.InventoryManager;
import me.missionary.blueberry.utils.inventory.SmartInventory;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class SpecialInventoryOpener implements InventoryOpener {

    private ImmutableList<InventoryType> supported = ImmutableList.of(InventoryType.FURNACE,
            InventoryType.WORKBENCH,
            InventoryType.DISPENSER,
            InventoryType.DROPPER,
            InventoryType.ENCHANTING,
            InventoryType.BREWING,
            InventoryType.ANVIL,
            InventoryType.BEACON,
            InventoryType.HOPPER
    );

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        InventoryManager manager = Blueberry.getPlugin().getInventoryManager();
        Inventory handle = Bukkit.createInventory(player, inv.getType(), inv.getTitle());

        fill(handle, manager.getContents(player).get());

        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return supported.contains(type);
    }

}
