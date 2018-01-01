package me.missionary.blueberry.spawn.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.spawn.SpawnManager;
import me.missionary.blueberry.utils.commands.Command;
import me.missionary.blueberry.utils.commands.CommandArgs;
import me.missionary.blueberry.utils.commands.GenericArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/26/2017.
 */
public class SetSpawnAreaCommand extends GenericArgument {

    private final WorldEditPlugin worldEdit;

    public SetSpawnAreaCommand(Blueberry plugin) {
        super(plugin);
        Plugin wep = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        worldEdit = wep instanceof WorldEditPlugin && wep.isEnabled() ? (WorldEditPlugin) wep : null;
    }

    @Command(name = "setspawnarea", description = "Command used to set the spawn area.", permission = "blueberry.admin", inGameOnly = true)
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();

        WorldEditPlugin worldEditPlugin = worldEdit;

        if (worldEditPlugin == null) {
            sender.sendMessage(ChatColor.RED + "WorldEdit must be installed to set the spawn area.");
            return;
        }

        Selection selection = worldEditPlugin.getSelection(sender);

        if (selection == null) {
            sender.sendMessage(ChatColor.RED + "You must make a WorldEdit selection to do this.");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Successfully set the spawn area.");
        SpawnManager spawnManager = getPlugin().getSpawnManager();
        spawnManager.setMin(selection.getMinimumPoint().toVector());
        spawnManager.setMax(selection.getMaximumPoint().toVector());
        spawnManager.setWorldName(selection.getWorld().getName());
        sender.sendMessage(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Have you set the spawn point yet? Do so with /setspawnpoint at the desired location!");
    }
}
