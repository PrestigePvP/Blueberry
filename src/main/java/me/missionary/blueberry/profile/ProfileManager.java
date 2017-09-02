package me.missionary.blueberry.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.utils.Manager;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class ProfileManager extends Manager { // TODO: 9/1/2017 This could be re-done, but for a public release because I'm bored this will suffice.

    private final MongoCollection<Document> mongoCollection;

    @Getter
    private final Map<UUID, Profile> profiles;

    public ProfileManager(Blueberry plugin) {
        super(plugin);
        this.mongoCollection = getPlugin().getMongoDatabase().getCollection("blueberryProfiles");
        profiles = new ConcurrentHashMap<>(); // The scoreboard runs async, need this for thread safety.
        onEnable();
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
        getPlugin().getLogger().info("Loaded " + i + "profiles in " + (System.currentTimeMillis() - start) + "ms.");
    }

    public void createNewProfile(UUID uuid){
        Profile profile = new Profile(uuid);
        profiles.put(uuid, profile);
    }

    @Override
    public void onDisable() {
        profiles.forEach((uuid, profile) -> mongoCollection.replaceOne(Filters.eq("uniqueId", profile.getUuid().toString()), Document.parse(Blueberry.getGson().toJson(profile)), new UpdateOptions().upsert(true)));
    }
}
