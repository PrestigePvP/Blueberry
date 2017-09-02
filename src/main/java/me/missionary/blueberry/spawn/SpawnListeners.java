package me.missionary.blueberry.spawn;

import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.kit.KitManager;
import me.missionary.blueberry.utils.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
@RequiredArgsConstructor
public class SpawnListeners implements Listener {

    private final Blueberry plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (!plugin.getProfileManager().getProfiles().containsKey(event.getPlayer().getUniqueId())){
            plugin.getProfileManager().createNewProfile(event.getPlayer().getUniqueId());
        }
        Player player = event.getPlayer();
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.sendMessage(ChatColor.AQUA + "Welcome to NoDebuff FFA!");
        player.getInventory().setItem(0, KitManager.KIT_SELECTOR);
    }

    @EventHandler
    public void onClickItem(PlayerInteractEvent event){
        if (event.getItem() != null && event.getAction().name().contains("RIGHT") && event.getItem().isSimilar(KitManager.KIT_SELECTOR)){
            Menu menu = new Menu("Select a kit", 1);
            plugin.getKitManager().getKits().values().forEach(kit -> menu.addItem(kit.getIcon()));
            menu.runWhenEmpty(false);
            menu.build();
            menu.setGlobalAction((player1, inventory, itemStack, slot, inventoryAction) -> plugin.getKitManager().setEquippedKit(player1, plugin.getKitManager().getKitByIcon(itemStack)));
            menu.showMenu(event.getPlayer());
        }
    }
}
