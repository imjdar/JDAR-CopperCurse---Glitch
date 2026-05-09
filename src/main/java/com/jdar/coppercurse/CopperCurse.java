package com.jdar.coppercurse;

import com.jdar.coppercurse.listener.CombatListener;
import com.jdar.coppercurse.listener.LootListener;
import com.jdar.coppercurse.listener.OreListener;
import com.jdar.coppercurse.manager.CommandManager;
import com.jdar.coppercurse.manager.CraftingManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Main class for the JDAR-CopperCurse plugin.
 * Handles initialization and module coordination.
 */
public class CopperCurse extends JavaPlugin {

    private static CopperCurse instance;
    private CraftingManager craftingManager;
    private boolean pluginEnabled;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.pluginEnabled = getConfig().getBoolean("enabled", true);

        // Initialize Managers
        this.craftingManager = new CraftingManager(this);
        
        // Register Commands
        getCommand("coppercurse").setExecutor(new CommandManager(this));

        // Register Listeners
        getServer().getPluginManager().registerEvents(new OreListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new LootListener(this), this);

        // Load Recipes
        if (pluginEnabled) {
            craftingManager.registerRecipes();
        }

        getLogger().info("JDAR-CopperCurse has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("JDAR-CopperCurse has been disabled!");
    }

    public static CopperCurse getInstance() {
        return instance;
    }

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
        if (pluginEnabled) {
            craftingManager.registerRecipes();
        } else {
            // Recalculating recipes or removing them is tricky in Paper without a reload/restart
            // but we can block them in CraftItemEvent if needed.
        }
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }
}
