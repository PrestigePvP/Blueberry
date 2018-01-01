package me.missionary.blueberry.profile.inventory;

import lombok.NonNull;
import me.missionary.blueberry.profile.Profile;
import me.missionary.blueberry.utils.ItemBuilder;
import me.missionary.blueberry.utils.inventory.ClickableItem;
import me.missionary.blueberry.utils.inventory.SmartInventory;
import me.missionary.blueberry.utils.inventory.content.InventoryContents;
import me.missionary.blueberry.utils.inventory.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/31/17.
 */
public class StatisticsInventory implements InventoryProvider {

    private final SmartInventory smartInventory;
    private final Profile profile;

    public StatisticsInventory(Player player, @NonNull Profile profile) {
        this.smartInventory = SmartInventory.builder().id(player.getName() + "-statsinventory").provider(this).size(3, 9).title(ChatColor.LIGHT_PURPLE + "Stats" + ChatColor.GRAY + ":" + ChatColor.WHITE + player.getName()).build();
        this.profile = profile;
        smartInventory.open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 3, ClickableItem.empty(new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(ChatColor.GREEN + "Kills")
                .setLore(Arrays.asList(
                        ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------",
                        ChatColor.YELLOW + "Total Kills" + ChatColor.GRAY + ": " + ChatColor.WHITE + profile.getKills(),
                        ChatColor.YELLOW + " This game" + ChatColor.GRAY + ": " + ChatColor.WHITE + profile.getKillsThisGame(),
                        ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------"
                )).toItemStack()));
        contents.set(1, 5, ClickableItem.empty(new ItemBuilder(Material.SKULL)
                .setName(ChatColor.RED + "Deaths")
                .setLore(Arrays.asList(
                        ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------",
                        ChatColor.YELLOW + "Deaths" + ChatColor.GRAY + ": " + ChatColor.WHITE + profile.getDeaths(),
                        ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------"
                )).toItemStack()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
