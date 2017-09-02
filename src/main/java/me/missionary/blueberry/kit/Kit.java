package me.missionary.blueberry.kit;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public abstract class Kit {

    @Getter
    protected String name;

    @Getter
    protected ItemStack icon;

    public Kit(String name, ItemStack icon) {
        this.name = name;
        this.icon = icon;
    }

    public abstract boolean isApplicable(Player player);

    public abstract void giveItems(Player player);
}
