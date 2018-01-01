package me.missionary.blueberry.kit.kits;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.kit.Kit;
import me.missionary.blueberry.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.stream.IntStream;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class NoDebuffKit extends Kit {

    private final Blueberry plugin;

    public NoDebuffKit(Blueberry plugin) {
        super("NoDebuff", new ItemBuilder(Material.DIAMOND_SWORD).setName("NoDebuff").toItemStack());
        this.plugin = plugin;
    }

    @Override
    public boolean isApplicable(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        ItemStack helmet = playerInventory.getHelmet();
        if (helmet == null || helmet.getType() != Material.DIAMOND_HELMET) {
            return false;
        }

        ItemStack chestplate = playerInventory.getChestplate();
        if (chestplate == null || chestplate.getType() != Material.DIAMOND_CHESTPLATE) {
            return false;
        }

        ItemStack leggings = playerInventory.getLeggings();
        if (leggings == null || leggings.getType() != Material.DIAMOND_LEGGINGS) {
            return false;
        }

        ItemStack boots = playerInventory.getBoots();
        return !(boots == null || boots.getType() != Material.DIAMOND_BOOTS);
    }

    @Override
    public void giveItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        final ItemStack speedII = new ItemBuilder(Material.POTION).setDurability((short) 8226).toItemStack();
        inventory.setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).setUnbreakable().toItemStack());
        inventory.setHelmet(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable().toItemStack());
        inventory.setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable().toItemStack());
        inventory.setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable().toItemStack());
        inventory.setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.PROTECTION_FALL, 3).setUnbreakable().toItemStack());
        inventory.setItem(1, new ItemBuilder(Material.ENDER_PEARL, 16).toItemStack());
        inventory.setItem(8, new ItemBuilder(Material.GOLDEN_CARROT, 32).toItemStack());
        IntStream.range(0, 35).mapToObj(i -> new ItemBuilder(Material.POTION).setDurability((short) 16421).toItemStack()).forEach(inventory::addItem);
        inventory.setItem(34, speedII);
        inventory.setItem(35, speedII);
        inventory.setItem(26, speedII);
        inventory.setItem(17, speedII);
        player.closeInventory();
    }
}
