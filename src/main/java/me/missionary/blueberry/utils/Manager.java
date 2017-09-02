package me.missionary.blueberry.utils;

import lombok.Getter;
import me.missionary.blueberry.Blueberry;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
@Getter
public abstract class Manager { // Makes thing look cleaner.

    private Blueberry plugin;

    public Manager(Blueberry plugin) {
        this.plugin = plugin;
    }

    protected Manager() {
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
