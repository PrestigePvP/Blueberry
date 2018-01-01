package me.missionary.blueberry.kit;

import lombok.Getter;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.kit.kits.NoDebuffKit;
import me.missionary.blueberry.utils.ItemBuilder;
import me.missionary.blueberry.utils.Manager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class KitManager extends Manager implements Listener {

    public static final ItemStack KIT_SELECTOR = new ItemBuilder(Material.WATCH).setName(ChatColor.AQUA + "Choose a Kit").toItemStack();

    @Getter
    private List<Kit> kits;

    @Getter
    private Map<UUID, Kit> equippedKits;

    public KitManager(Blueberry plugin) {
        super(plugin);
        this.kits = new ArrayList<>();
        this.equippedKits = new HashMap<>();
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @Override
    public void onEnable() {
        kits.add(new NoDebuffKit(getPlugin()));
    }

    public Optional<Kit> getEquippedKit(Player player) {
        return Optional.ofNullable(equippedKits.getOrDefault(player.getUniqueId(), null));
    }

    private Kit getLocalEquippedKit(Player player) {
        return equippedKits.getOrDefault(player.getUniqueId(), null);
    }

    public void setEquippedKit(Player player, @Nullable Kit kit) {
        if (kit == null) {
            Kit equipped = equippedKits.remove(player.getUniqueId());
        } else if (kit != this.getLocalEquippedKit(player)) {
            equippedKits.put(player.getUniqueId(), kit);
            player.getInventory().clear();
            kit.giveItems(player);
        }
    }

    public boolean hasKitEquipped(Player player, Kit kit) {
        return getLocalEquippedKit(player) == kit;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        getPlugin().getKitManager().setEquippedKit(event.getPlayer(), null);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                attemptEquip(player);
            }
        }.runTaskTimer(getPlugin(), 20L, 20L);
    }

    private void attemptEquip(Player player) {
        Kit current = getPlugin().getKitManager().getLocalEquippedKit(player);
        if (current != null) {
            if (current.isApplicable(player)) return;
            getPlugin().getKitManager().setEquippedKit(player, null);
        }
        kits.stream().filter(kit -> kit.isApplicable(player)).findFirst().ifPresent(kit -> setEquippedKit(player, kit));
    }

    public Kit getKitByIcon(ItemStack icon){
        return kits.stream().filter(kit -> kit.getIcon().isSimilar(icon)).findFirst().orElse(null);
    }

    @Override
    public void onDisable() {
    }
}
