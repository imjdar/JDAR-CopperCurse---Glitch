package com.jdar.coppercurse.listener;

import com.jdar.coppercurse.CopperCurse;
import com.jdar.coppercurse.util.Constants;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.ListIterator;

/**
 * Intercepts loot generation to replace valuable items with copper equivalents.
 */
public class LootListener implements Listener {

    private final CopperCurse plugin;

    public LootListener(CopperCurse plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        if (!plugin.isPluginEnabled()) return;
        if (!plugin.getConfig().getBoolean("loot.override-global", true)) return;

        List<ItemStack> loot = event.getLoot();
        ListIterator<ItemStack> iterator = loot.listIterator();

        while (iterator.hasNext()) {
            ItemStack item = iterator.next();
            if (item == null || item.getType().isAir()) continue;

            ItemStack replacement = getReplacement(item);
            if (replacement != null) {
                iterator.set(replacement);
            }
        }
    }

    private ItemStack getReplacement(ItemStack original) {
        Material type = original.getType();

        // Ingots
        if (type == Material.IRON_INGOT || type == Material.GOLD_INGOT) {
            return new ItemStack(Material.COPPER_INGOT, original.getAmount());
        }

        // Diamonds
        if (type == Material.DIAMOND) {
            return new ItemStack(Material.RAW_COPPER, original.getAmount());
        }

        // Golden Apples -> Copper Apple
        if (type == Material.GOLDEN_APPLE) {
            ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, original.getAmount());
            ItemMeta meta = apple.getItemMeta();
            meta.setDisplayName("§6Manzana de Cobre");
            meta.setCustomModelData(Constants.COPPER_APPLE_CMD);
            apple.setItemMeta(meta);
            return apple;
        }

        // Gear (Simple replacement with copper ingots or generic copper gear if needed)
        if (isValuableGear(type)) {
            return new ItemStack(Material.COPPER_INGOT, 2);
        }

        return null;
    }

    private boolean isValuableGear(Material type) {
        String name = type.name();
        return name.contains("IRON_") || name.contains("GOLDEN_") || name.contains("DIAMOND_") || name.contains("NETHERITE_");
    }
}
