package com.adminlogs.adminlogs;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AdminLogs extends JavaPlugin {

    private static AdminLogs instance;
    private DataManager dataManager;
    private GuiManager guiManager;
    private String viewerUUID;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config if not present
        saveDefaultConfig();
        loadViewerUUID();

        // Init data manager (handles persistent YAML storage)
        dataManager = new DataManager(this);
        dataManager.load();

        // Init GUI manager as an instance (not static) so state is tied to this plugin lifecycle
        guiManager = new GuiManager(this);
        getServer().getPluginManager().registerEvents(guiManager, this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);

        // Register command
        AdminLogsCommand cmd = new AdminLogsCommand(this);
        getCommand("adminlogs").setExecutor(cmd);
        getCommand("adminlogs").setTabCompleter(cmd);

        getLogger().info("AdminLogs enabled. Viewer UUID: " + viewerUUID);
    }

    @Override
    public void onDisable() {
        // Clear any open GUI state to prevent stale data on reload
        if (guiManager != null) {
            guiManager.clearAllGuis();
        }
        if (dataManager != null) {
            dataManager.save();
        }
        getLogger().info("AdminLogs disabled. Data saved.");
    }

    public void loadViewerUUID() {
        viewerUUID = getConfig().getString("viewer-uuid", "PASTE-UUID-HERE");
    }

    public String getViewerUUID() {
        return viewerUUID;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public static AdminLogs getInstance() {
        return instance;
    }
}
