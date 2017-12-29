package me.missionary.blueberry.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.utils.Manager;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;
import net.minecraft.util.org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class ProfileManager extends Manager implements IProfileManager {

    private static final String GEO_IP_BASE_URL = "http://freegeoip.net/json/";

    private final MongoCollection<Document> mongoCollection;

    private final Blueberry plugin;

    @Getter
    private final Map<UUID, Profile> profiles;

    public ProfileManager(Blueberry plugin) {
        super(plugin);
        this.plugin = plugin;
        this.mongoCollection = getPlugin().getMongoDatabase().getCollection("blueberryProfiles");
        profiles = new ConcurrentHashMap<>(); // The scoreboard runs async, need this for thread safety.
    }

    @Override
    public void onEnable() {
        int i = 0;
        long start = System.currentTimeMillis();
        for (Document document : mongoCollection.find()) {
            Profile profile = Blueberry.getGson().fromJson(document.toJson(), Profile.class);
            profiles.put(profile.getUuid(), profile);
            i++;
        }
        getPlugin().getLogger().info("Loaded " + i + " profiles in " + (System.currentTimeMillis() - start) + "ms.");
    }

    @Override
    public void createProfile(UUID uuid) {
        Profile profile = new Profile(uuid);
        profiles.put(uuid, profile);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> mongoCollection.insertOne(Document.parse(Blueberry.getGson().toJson(profile))));
    }

    @Override
    public void onDisable() {
        profiles.forEach((uuid, profile) -> mongoCollection.replaceOne(Filters.eq("uniqueId", profile.getUuid().toString()), Document.parse(Blueberry.getGson().toJson(profile)), new UpdateOptions().upsert(true)));
    }

    @Override
    public Optional<Profile> getProfile(UUID uuid) {
        for (Map.Entry<UUID, Profile> profileEntry : profiles.entrySet()) {
            if (profileEntry.getKey().equals(uuid)) {
                return Optional.of(profileEntry.getValue());
            }
        }
        return Optional.empty();
    }

    public void getPlayerTimeZone(Profile profile){
        if (!profile.getPlayer().isPresent()){
            Bukkit.getLogger().info("Whilst pulling the TimeZone for " + profile.getUuid() + " the player was not online, cancelling.");
            return;
        }
        JsonObject object;
        try {
           object = new JsonParser().parse(IOUtils.toString(new URL(GEO_IP_BASE_URL + profile.getPlayer().get().getAddress().getAddress().getHostAddress()))).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        TimeZone zone = TimeZone.getTimeZone(object.get("time_zone").getAsString());
        profile.setTimeZone(zone);
    }
}
