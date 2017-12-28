package me.missionary.blueberry.profile;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class Profile { // TODO: 9/1/2017 Add kills, deaths, killstreaks.

    @Getter
    private final UUID uuid;

    @Getter
    @Setter
    private int kills;

    @Getter
    @Setter
    private int deaths;

    @Getter
    @Setter
    private TimeZone timeZone;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.timeZone = TimeZone.getDefault();
    }

    public boolean isProfilePlayerOnline() {
        return Bukkit.getPlayer(this.uuid) != null;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }
}
