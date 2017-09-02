package me.missionary.blueberry.kit;

import lombok.Getter;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.kit.kits.NoDebuffKit;
import me.missionary.blueberry.utils.ItemBuilder;
import me.missionary.blueberry.utils.Manager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.EquipmentSetEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class KitManager extends Manager implements Listener {

    public static final ItemStack KIT_SELECTOR = new ItemBuilder(Material.WATCH).setName(ChatColor.AQUA + "Choose a Kit").toItemStack();

    @Getter
    private Map<Class<? extends Kit>, Kit> kits;

    @Getter
    private Map<UUID, Kit> equippedKits;

    public KitManager(Blueberry plugin) {
        super(plugin);
        this.kits = new HashMap<>();
        this.equippedKits = new HashMap<>();
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
        onDisable();
    }

    @Override
    public void onEnable() {
        kits.put(NoDebuffKit.class, new NoDebuffKit(getPlugin()));
    }

    public Kit getEquippedKit(Player player) {
        return equippedKits.get(player.getUniqueId());
    }

    public void setEquippedKit(Player player, @Nullable Kit kit) {
        if (kit == null) {
            Kit equipped = equippedKits.remove(player.getUniqueId());
            if (equipped != null) {
                player.getInventory().clear();
                player.getInventory().setArmorContents(new ItemStack[4]); // nothing
            }
        } else if (kit != this.getEquippedKit(player)) {
            equippedKits.put(player.getUniqueId(), kit);
            player.getInventory().clear();
            kit.giveItems(player);
        }
    }

    public boolean hasKitEquipped(Player player, Kit kit) {
        return getEquippedKit(player) == kit;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        getPlugin().getKitManager().setEquippedKit(event.getPlayer(), null);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEquipmentSet(EquipmentSetEvent event) {
        HumanEntity humanEntity = event.getHumanEntity();
        if (humanEntity instanceof Player) {
            attemptEquip((Player) humanEntity);
        }
    }

    private void attemptEquip(Player player) {
        Kit current = getPlugin().getKitManager().getEquippedKit(player);
        if (current != null) {
            if (current.isApplicable(player)) return;
            getPlugin().getKitManager().setEquippedKit(player, null);
        }
        kits.values().stream().filter(kit -> kit.isApplicable(player)).findFirst().ifPresent(kit -> setEquippedKit(player, kit));
    }

    public Kit getKitByIcon(ItemStack icon){
        return kits.values().stream().filter(kit -> kit.getIcon().isSimilar(icon)).findFirst().orElse(null);
    }

    @Override
    public void onDisable() {
        equippedKits.forEach((uuid, kit) -> setEquippedKit(getPlugin().getServer().getPlayer(uuid), null));
    }
}
