package com.jdar.coppercurse.listener;

import com.jdar.coppercurse.CopperCurse;
import com.jdar.coppercurse.util.Constants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;
import java.util.function.Function;

/**
 * Handles ore mining restrictions and diamond limits.
 */
public class OreListener implements Listener {

    private final CopperCurse plugin;

    private final Function<Material, ItemStack> itemStackFactory;

    public OreListener(CopperCurse plugin) {
        this(plugin, ItemStack::new);
    }

    public OreListener(CopperCurse plugin, Function<Material, ItemStack> factory) {
        this.plugin = plugin;
        this.itemStackFactory = factory;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOreBreak(BlockBreakEvent event) {
        if (!plugin.isPluginEnabled()) return;

        Block block = event.getBlock();
        Material type = block.getType();
        Player player = event.getPlayer();

        // Restricted Ores -> Copper
        Set<Material> restrictedOres = Set.of(
                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
                Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
                Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE
        );
        if (restrictedOres.contains(type)) {
            handleCopperReplacement(event, type);
            return;
        }

        // Diamond Limit Logic
        if (type == Material.DIAMOND_ORE || type == Material.DEEPSLATE_DIAMOND_ORE) {
            handleDiamondLimit(event, player);
        }
    }

    private void handleCopperReplacement(BlockBreakEvent event, Material type) {
        if (type == Material.IRON_ORE || type == Material.DEEPSLATE_IRON_ORE) {
            handleIronLimit(event, event.getPlayer());
            return;
        }

        event.setDropItems(false);
        Material drop = (type == Material.GOLD_ORE || type == Material.DEEPSLATE_GOLD_ORE) 
                ? Material.RAW_COPPER : Material.COPPER_INGOT;
        
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStackFactory.apply(drop));
    }

    private void handleIronLimit(BlockBreakEvent event, Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        int ironCount = pdc.getOrDefault(Constants.IRON_OBTAINED_KEY, PersistentDataType.INTEGER, 0);

        if (ironCount >= 6) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStackFactory.apply(Material.RAW_COPPER));
            player.sendMessage("§cLa maldición del cobre ha transformado el hierro... Ya tienes suficiente.");
        } else {
            // Allow iron drop and increment count
            pdc.set(Constants.IRON_OBTAINED_KEY, PersistentDataType.INTEGER, ironCount + 1);
            // Default iron drop is RAW_IRON
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStackFactory.apply(Material.RAW_IRON));
        }
    }

    private void handleDiamondLimit(BlockBreakEvent event, Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        int minedCount = pdc.getOrDefault(Constants.DIAMONDS_MINED_KEY, PersistentDataType.INTEGER, 0);

        if (minedCount >= plugin.getConfig().getInt("mining.diamond-limit", 5)) {
            // Evaporate and give copper
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStackFactory.apply(Material.RAW_COPPER));
            player.sendMessage("§cLa maldición del cobre ha evaporado el diamante...");
        } else {
            // Allow mining and increment count
            pdc.set(Constants.DIAMONDS_MINED_KEY, PersistentDataType.INTEGER, minedCount + 1);
        }
    }
}
