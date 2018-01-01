package me.missionary.blueberry.spawn.commands;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.spawn.SpawnManager;
import me.missionary.blueberry.utils.commands.Command;
import me.missionary.blueberry.utils.commands.CommandArgs;
import me.missionary.blueberry.utils.commands.GenericArgument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/26/2017.
 */
public class SetSpawnPointCommand extends GenericArgument {

    public SetSpawnPointCommand(Blueberry plugin) {
        super(plugin);
    }

    @Command(name = "setspawnpoint", description = "Command used to set the spawn point.", permission = "blueberry.admin", inGameOnly = true)
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();
        SpawnManager spawnManager = getPlugin().getSpawnManager();
        spawnManager.setSpawnPoint(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Set the spawn point!");
    }
}
