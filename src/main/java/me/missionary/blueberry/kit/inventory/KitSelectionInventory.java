package me.missionary.blueberry.kit.inventory;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.kit.Kit;
import me.missionary.blueberry.kit.KitManager;
import me.missionary.blueberry.utils.ItemBuilder;
import me.missionary.blueberry.utils.inventory.ClickableItem;
import me.missionary.blueberry.utils.inventory.SmartInventory;
import me.missionary.blueberry.utils.inventory.content.InventoryContents;
import me.missionary.blueberry.utils.inventory.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/26/2017.
 */
public class KitSelectionInventory implements InventoryProvider {

    private SmartInventory smartInventory;

    public KitSelectionInventory(Player player) {
        this.smartInventory = SmartInventory.builder().id(player.getName() + "-kitselection").provider(this).size(3, 9).title(ChatColor.LIGHT_PURPLE + "Select a kit").build();
        smartInventory.open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {

      Blueberry.getPlugin().getKitManager().getKits().forEach((aClass, kit) -> contents.add(ClickableItem.of(kit.getIcon(), inventoryClickEvent -> {
          Kit selectedKit = Blueberry.getPlugin().getKitManager().getKitByIcon(inventoryClickEvent.getCurrentItem());
          selectedKit.giveItems(player);
      })));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                if (!contents.get(i, j).isPresent()) {
                    contents.set(i, j, ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).setName(ChatColor.RED + "").setDurability((short) ItemBuilder.Pane.GRAY.value()).toItemStack()));
                }
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
