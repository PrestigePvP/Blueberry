package me.missionary.blueberry.profile.commands;

import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.profile.inventory.StatisticsInventory;
import me.missionary.blueberry.utils.commands.Command;
import me.missionary.blueberry.utils.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/31/17.
 */
@RequiredArgsConstructor
public class StatsCommand {

    private final Blueberry plugin;

    @Command(name = "stats", aliases = {"statistics", "ezggnoob"}, inGameOnly = true, description = "View the statistics of a player.")
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();

        if (args.length() > 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /stats <playerName>");
            sender.sendMessage(ChatColor.RED + "Too many arguments provided.");
            return;
        }

        plugin.getProfileManager().getProfile(plugin.getUuidManager().getUUID(args.getArgs(0))).ifPresent(profile -> new StatisticsInventory(sender, profile));
    }
}
