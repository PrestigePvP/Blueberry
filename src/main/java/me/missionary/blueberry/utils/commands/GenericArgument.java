package me.missionary.blueberry.utils.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/26/2017.
 */
public abstract class GenericArgument {

    @Getter
    private final Blueberry plugin;

    public GenericArgument(Blueberry plugin) {
        this.plugin = plugin;
        plugin.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs args);
}
