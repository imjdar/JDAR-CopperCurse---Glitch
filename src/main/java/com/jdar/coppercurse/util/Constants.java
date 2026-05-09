package com.jdar.coppercurse.util;

import org.bukkit.NamespacedKey;
import com.jdar.coppercurse.CopperCurse;

/**
 * Global constants and NamespacedKeys for the plugin.
 */
public class Constants {
    public static final String NAMESPACE = "coppercurse";
    public static final NamespacedKey DIAMONDS_MINED_KEY = new NamespacedKey(NAMESPACE, "diamonds_mined");
    public static final NamespacedKey IRON_OBTAINED_KEY = new NamespacedKey(NAMESPACE, "iron_obtained");
    public static final NamespacedKey CUSTOM_ITEM_KEY = new NamespacedKey(NAMESPACE, "custom_item");
    
    // Custom Model Data values
    public static final int COPPER_INGOT_GEAR_CMD = 1001;
    public static final int COPPER_BLOCK_GEAR_CMD = 1002;
    public static final int COPPER_GLITCH_SHIELD_CMD = 1003;
    public static final int COPPER_APPLE_CMD = 1004;

    // Custom Item IDs
    public static final String HEAVY_COPPER_SWORD = "heavy_copper_sword";
    public static final String COPPER_GLITCH_SHIELD = "copper_glitch_shield";
}
