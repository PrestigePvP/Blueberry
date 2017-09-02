package me.missionary.blueberry.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UUIDManager implements Listener {
    private final JavaPlugin plugin;

    private Config config;
    private final BiMap<UUID, String> uuidNameMap;

    public UUIDManager(JavaPlugin plugin) {
        this.plugin = plugin;

        uuidNameMap = HashBiMap.create();
        reload();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void reload() {
        this.config = new Config(plugin, "users");
        Object object = config.get("uuid");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection) object;
            Set<String> keys = section.getKeys(false);
            keys.forEach(id -> uuidNameMap.put(UUID.fromString(id), config.getString("uuid." + id)));
        }
    }

    public void save() {
        final Set<Map.Entry<UUID, String>> uuid = uuidNameMap.entrySet();
        final Map<String, String> saveMap = uuid.stream().collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue, (a, b) -> b, () -> new LinkedHashMap<>(uuid.size())));
        config.set("uuid", saveMap);
        config.save();
    }

    public String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();//uuidMap.get(uuid);
    }

    public UUID getUUID(String name) {
        return Bukkit.getPlayer(name) != null ? Bukkit.getPlayer(name).getUniqueId() : Bukkit.getOfflinePlayer(name).getUniqueId();// uuidMap.entrySet().stream().filter(foundName -> foundName.getValue().equalsIgnoreCase(name)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        config.set(event.getUniqueId().toString(), event.getName());
        uuidNameMap.forcePut(event.getUniqueId(), event.getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (uuidNameMap.containsKey(event.getPlayer().getUniqueId())) {
            uuidNameMap.remove(event.getPlayer().getUniqueId());
        }
    }
}
