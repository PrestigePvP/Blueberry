package me.missionary.blueberry;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.missionary.blueberry.kit.KitManager;
import me.missionary.blueberry.profile.Profile;
import me.missionary.blueberry.profile.ProfileManager;
import me.missionary.blueberry.profile.ProfileSerializer;
import me.missionary.blueberry.utils.UUIDManager;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class Blueberry extends JavaPlugin {

    @Getter
    private static Blueberry plugin;

    @Getter
    private static Gson gson;

    @Getter
    private DatabaseConfiguration databaseConfiguration;

    @Getter
    private MongoClient mongoClient;

    @Getter
    private MongoDatabase mongoDatabase;

    @Getter
    private UUIDManager uuidManager;

    @Getter
    private ProfileManager profileManager;

    @Getter
    private KitManager kitManager;

    @Override
    public void onEnable() {
        plugin = this;
        gson = new GsonBuilder().registerTypeAdapter(Profile.class, new ProfileSerializer()).setPrettyPrinting().serializeNulls().create();
        initalizeMongo();
        initalizeManagers();
    }

    private void initalizeManagers() {
        uuidManager = new UUIDManager(this);
        profileManager = new ProfileManager(this);
        kitManager = new KitManager(this);
    }

    private void initalizeMongo() {
        databaseConfiguration = new DatabaseConfiguration(this);
        MongoClientOptions options = MongoClientOptions.builder().socketKeepAlive(true).build(); // TODO: 9/1/2017 Remove the deprecated method, was a quick fix for sockets timing out.
        mongoClient = new MongoClient(new ServerAddress(getDatabaseConfiguration().getHostName()), Collections.singletonList(MongoCredential.createCredential(getDatabaseConfiguration().getUser(), getDatabaseConfiguration().getDatabaseName(), getDatabaseConfiguration().getPassword().toCharArray())), options);
        mongoDatabase = mongoClient.getDatabase(getDatabaseConfiguration().getDatabaseName());
    }

    @Override
    public void onDisable() {
        kitManager.onDisable();
        profileManager.onDisable();
        uuidManager.save();
        gson = null;
        plugin = null;
    }
}
