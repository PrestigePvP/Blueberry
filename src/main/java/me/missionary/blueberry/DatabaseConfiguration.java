package me.missionary.blueberry;

import lombok.Getter;
import me.missionary.blueberry.utils.Config;

/**
 * Created by Missionary on 7/13/2017.
 */
@Getter
public class DatabaseConfiguration {

    private final Blueberry plugin;

    private Config config;

    public String hostName;

    public String databaseName;

    public int port;

    public String user;

    public String password;

    public DatabaseConfiguration(Blueberry plugin){
        this.plugin = plugin;
        config = new Config(plugin, "database");
        load();
    }

    private void load(){
        hostName = config.getString("database.host");
        databaseName = config.getString("database.database-name");
        port = config.getInt("database.port");
        password = config.getString("database.password");
        user = config.getString("database.user");
    }
}
