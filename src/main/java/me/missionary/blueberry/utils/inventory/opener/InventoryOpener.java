package me.missionary.blueberry.utils.inventory.opener;

import me.missionary.blueberry.utils.inventory.ClickableItem;
import me.missionary.blueberry.utils.inventory.SmartInventory;
import me.missionary.blueberry.utils.inventory.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface InventoryOpener {

    Inventory open(SmartInventory inv, Player player);

    boolean supports(InventoryType type);

    default void fill(Inventory handle, InventoryContents contents) {
        ClickableItem[][] items = contents.all();

        for (int row = 0; row < items.length; row++) {
            for (int column = 0; column < items[row].length; column++) {
                if (items[row][column] != null) {
                    handle.setItem(9 * row + column, items[row][column].getItem());
                }
            }
        }
    }

    enum Priority {
        LOWEST, LOW, NORMAL, HIGH, HIGHEST
    }

}
