package me.missionary.blueberry;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.missionary.blueberry.combatlogger.LoggerManager;
import me.missionary.blueberry.combatlogger.LogoutListener;
import me.missionary.blueberry.combatlogger.LogoutTaskManager;
import me.missionary.blueberry.combatlogger.commands.LogoutCommand;
import me.missionary.blueberry.kit.KitManager;
import me.missionary.blueberry.listeners.CombatTagListener;
import me.missionary.blueberry.listeners.EnderpearlListener;
import me.missionary.blueberry.listeners.KillsDeathsListener;
import me.missionary.blueberry.profile.Profile;
import me.missionary.blueberry.profile.ProfileManager;
import me.missionary.blueberry.profile.ProfileSerializer;
import me.missionary.blueberry.scoreboard.Options;
import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.scoreboard.impl.BlueberryImpl;
import me.missionary.blueberry.spawn.SpawnListeners;
import me.missionary.blueberry.spawn.SpawnManager;
import me.missionary.blueberry.spawn.commands.SetSpawnAreaCommand;
import me.missionary.blueberry.spawn.commands.SetSpawnPointCommand;
import me.missionary.blueberry.utils.Manager;
import me.missionary.blueberry.utils.UUIDManager;
import me.missionary.blueberry.utils.commands.CommandFramework;
import me.missionary.blueberry.utils.inventory.InventoryManager;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

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
    private InventoryManager inventoryManager;

    @Getter
    private CommandFramework commandFramework;

    @Getter
    private KitManager kitManager;

    @Getter
    private SpawnManager spawnManager;

    @Getter
    private LogoutTaskManager logoutTaskManager;

    @Getter
    private LoggerManager loggerManager;

    @Override
    public void onEnable() {
        plugin = this;
        initalizeMongo();
        saveDefaultConfig();
        gson = new GsonBuilder().registerTypeAdapter(Profile.class, new ProfileSerializer()).setPrettyPrinting().serializeNulls().create();
        initalizeManagers();
        initalizeListeners();
        initalizeCommands();
    }

    private void initalizeManagers() {
        uuidManager = new UUIDManager(this);
        profileManager = new ProfileManager(this);
        inventoryManager = new InventoryManager(this);
        commandFramework = new CommandFramework(this);
        kitManager = new KitManager(this);
        spawnManager = new SpawnManager(this);
        logoutTaskManager = new LogoutTaskManager(this);
        loggerManager = new LoggerManager(this);
        new Scoreboard(this, new Options(), new BlueberryImpl());
        Arrays.asList(profileManager, kitManager, spawnManager).forEach(Manager::onEnable); // A shitty workaround to fix a race condition.
    }

    private void initalizeMongo() {
        databaseConfiguration = new DatabaseConfiguration(this);
     /*   MongoClientOptions options = MongoClientOptions.builder().socketKeepAlive(true).build(); // TODO: 9/1/2017 Remove the deprecated method, was a quick fix for sockets timing out.
        mongoClient = new MongoClient(new ServerAddress(getDatabaseConfiguration().getHostName()), Collections.singletonList(MongoCredential.createCredential(getDatabaseConfiguration().getUser(), getDatabaseConfiguration().getDatabaseName(), getDatabaseConfiguration().getPassword().toCharArray())), options);
        mongoDatabase = mongoClient.getDatabase(getDatabaseConfiguration().getDatabaseName());*/
        mongoClient = new MongoClient(new MongoClientURI("mongodb://user:blueberrypassword@cluster0-shard-00-00-tpqqx.mongodb.net:27017,cluster0-shard-00-01-tpqqx.mongodb.net:27017,cluster0-shard-00-02-tpqqx.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"));
        mongoDatabase = mongoClient.getDatabase("test");
    }

    private void initalizeListeners() {
        Arrays.asList(new CombatTagListener(),
                new LogoutListener(this),
                new KitManager(this),
                new SpawnListeners(this),
                new EnderpearlListener(),
                new KillsDeathsListener(this))
                .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void initalizeCommands() {
        Arrays.asList(new LogoutCommand(this),
                new SetSpawnAreaCommand(this),
                new SetSpawnPointCommand(this))
                .forEach(genericArgument -> commandFramework.registerCommands(genericArgument));
    }

    @Override
    public void onDisable() {
        kitManager.onDisable();
        profileManager.onDisable();
        uuidManager.save();
        spawnManager.onDisable();
        gson = null;
        plugin = null;
    }
}
