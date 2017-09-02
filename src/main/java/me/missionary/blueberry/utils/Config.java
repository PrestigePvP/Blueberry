package me.missionary.blueberry.utils;

import lombok.EqualsAndHashCode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Mystiflow
 */
@EqualsAndHashCode(callSuper = false)
public class Config extends YamlConfiguration {
    private final String fileName;
    private final JavaPlugin plugin;

    public Config(final JavaPlugin plugin, final String fileName) {
        this(plugin, fileName, ".yml");
    }

    public Config(final JavaPlugin plugin, final String fileName, final String fileExtension) {
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    public String getFileName() {
        return this.fileName;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    private void createFile() {
        final File folder = this.plugin.getDataFolder();
        try {
            final File file = new File(folder, this.fileName);
            if (!file.exists()) {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, false);
                } else {
                    this.save(file);
                }
            } else {
                this.load(file);
                this.save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        final File folder = this.plugin.getDataFolder();
        try {
            this.save(new File(folder, this.fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
