package com.jdar.coppercurse.manager;

import com.jdar.coppercurse.CopperCurse;
import com.jdar.coppercurse.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Manages custom recipes and blocks vanilla gear crafting.
 */
public class CraftingManager implements Listener {

    private final CopperCurse plugin;
    private final Set<Material> blockedGear = Set.of(
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            Material.GOLDEN_SWORD, Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
            Material.NETHERITE_SWORD, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE
    );

    public CraftingManager(CopperCurse plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void registerRecipes() {
        registerCopperIngotGear();
        registerHeavyCopperGear();
        registerCopperGlitchShield();
        registerCopperApple();
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (!plugin.isPluginEnabled()) return;
        
        Recipe recipe = event.getRecipe();
        if (recipe == null) return;

        ItemStack result = recipe.getResult();
        Material type = result.getType();

        // If the item is blocked but IS NOT one of our custom items, block it.
        if (blockedGear.contains(type)) {
            ItemMeta meta = result.getItemMeta();
            if (meta == null || !meta.hasCustomModelData()) {
                event.getInventory().setResult(null);
            }
        }
    }

    private void registerCopperIngotGear() {
        // Tools
        registerRecipe("copper_sword", createCopperItem(Material.GOLDEN_SWORD, "§fEspada de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "C", "C", "S");
        registerRecipe("copper_pickaxe", createCopperItem(Material.GOLDEN_PICKAXE, "§fPico de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "CCC", " S ", " S ");
        registerRecipe("copper_axe", createCopperItem(Material.GOLDEN_AXE, "§fHacha de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "CC", "CS", " S");
        registerRecipe("copper_shovel", createCopperItem(Material.GOLDEN_SHOVEL, "§fPala de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "C", "S", "S");
        registerRecipe("copper_hoe", createCopperItem(Material.GOLDEN_HOE, "§fAzada de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "CC", " S", " S");

        // Armor
        registerRecipe("copper_helmet", createCopperItem(Material.GOLDEN_HELMET, "§fCasco de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "CCC", "C C");
        registerRecipe("copper_chestplate", createCopperItem(Material.GOLDEN_CHESTPLATE, "§fPechera de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "C C", "CCC", "CCC");
        registerRecipe("copper_leggings", createCopperItem(Material.GOLDEN_LEGGINGS, "§fGrebas de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "CCC", "C C", "C C");
        registerRecipe("copper_boots", createCopperItem(Material.GOLDEN_BOOTS, "§fBotas de Cobre", Constants.COPPER_INGOT_GEAR_CMD), "C C", "C C");
    }

    private void registerRecipe(String key, ItemStack result, String... shape) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, key), result);
        recipe.shape(shape);
        recipe.setIngredient('C', Material.COPPER_INGOT);
        
        boolean hasStick = false;
        for (String row : shape) {
            if (row.contains("S")) {
                hasStick = true;
                break;
            }
        }
        if (hasStick) {
            recipe.setIngredient('S', Material.STICK);
        }
        
        Bukkit.addRecipe(recipe);
    }

    private void registerHeavyCopperGear() {
        // Heavy Tools (Blocks)
        registerHeavyRecipe("heavy_copper_sword", createHeavyCopperItem(Material.IRON_SWORD, "§6Espada Pesada de Cobre"), "B", "B", "S");
        registerHeavyRecipe("heavy_copper_pickaxe", createHeavyCopperItem(Material.IRON_PICKAXE, "§6Pico Pesado de Cobre"), "BBB", " S ", " S ");
        registerHeavyRecipe("heavy_copper_axe", createHeavyCopperItem(Material.IRON_AXE, "§6Hacha Pesada de Cobre"), "BB", "BS", " S");
        registerHeavyRecipe("heavy_copper_shovel", createHeavyCopperItem(Material.IRON_SHOVEL, "§6Pala Pesada de Cobre"), "B", "S", "S");
        registerHeavyRecipe("heavy_copper_hoe", createHeavyCopperItem(Material.IRON_HOE, "§6Azada Pesada de Cobre"), "BB", " S", " S");

        // Heavy Armor (Blocks)
        registerHeavyRecipe("heavy_copper_helmet", createHeavyCopperItem(Material.IRON_HELMET, "§6Casco Pesado de Cobre"), "BBB", "B B");
        registerHeavyRecipe("heavy_copper_chestplate", createHeavyCopperItem(Material.IRON_CHESTPLATE, "§6Pechera Pesada de Cobre"), "B B", "BBB", "BBB");
        registerHeavyRecipe("heavy_copper_leggings", createHeavyCopperItem(Material.IRON_LEGGINGS, "§6Grebas Pesadas de Cobre"), "BBB", "B B", "B B");
        registerHeavyRecipe("heavy_copper_boots", createHeavyCopperItem(Material.IRON_BOOTS, "§6Botas Pesadas de Cobre"), "B B", "B B");
    }

    private void registerHeavyRecipe(String key, ItemStack result, String... shape) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, key), result);
        recipe.shape(shape);
        recipe.setIngredient('B', Material.COPPER_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        Bukkit.addRecipe(recipe);
    }

    private ItemStack createHeavyCopperItem(Material material, String name) {
        ItemStack item = createCopperItem(material, name, Constants.COPPER_BLOCK_GEAR_CMD);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Constants.CUSTOM_ITEM_KEY, PersistentDataType.STRING, Constants.HEAVY_COPPER_SWORD); // Generic ID for passives
        List<String> lore = new ArrayList<>();
        lore.add("§7Pasiva: §eGolpe Oxidante");
        lore.add("§8Efectos debilitantes al impactar.");
        meta.setLore(lore);
        
        // Slightly better attributes than standard copper
        applyHeavyCopperAttributes(meta, material);
        
        item.setItemMeta(meta);
        return item;
    }

    private void applyHeavyCopperAttributes(ItemMeta meta, Material type) {
        String name = type.name();
        if (name.contains("SWORD")) addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 6.5, "heavy_copper_damage");
        else if (name.contains("ARMOR") || name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS")) {
            addAttribute(meta, Attribute.GENERIC_ARMOR_TOUGHNESS, 1.0, "heavy_copper_toughness");
        }
    }

    private void registerCopperGlitchShield() {
        ItemStack shield = createCopperItem(Material.SHIELD, "§dEscudo Glitch de Cobre", Constants.COPPER_GLITCH_SHIELD_CMD);
        ItemMeta meta = shield.getItemMeta();
        meta.getPersistentDataContainer().set(Constants.CUSTOM_ITEM_KEY, PersistentDataType.STRING, Constants.COPPER_GLITCH_SHIELD);
        shield.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "copper_glitch_shield"), shield);
        recipe.shape("WBW", "WWW", " W ");
        recipe.setIngredient('W', Material.OAK_PLANKS);
        recipe.setIngredient('B', Material.COPPER_BLOCK);
        Bukkit.addRecipe(recipe);
    }

    private void registerCopperApple() {
        ItemStack apple = createCopperItem(Material.GOLDEN_APPLE, "§6Manzana de Cobre", Constants.COPPER_APPLE_CMD);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "copper_apple"), apple);
        recipe.shape("CCC", "CAC", "CCC");
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(recipe);
    }

    private ItemStack createCopperItem(Material material, String name, int cmd) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setCustomModelData(cmd);

        // Apply intermediate attributes
        applyCopperAttributes(meta, material);

        item.setItemMeta(meta);
        return item;
    }

    private void applyCopperAttributes(ItemMeta meta, Material type) {
        String name = type.name();
        
        if (name.contains("SWORD")) {
            addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 5.0, "copper_damage");
            addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, -2.4, "copper_speed");
        } else if (name.contains("PICKAXE") || name.contains("AXE") || name.contains("SHOVEL") || name.contains("HOE")) {
            addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 3.0, "copper_tool_damage");
        } else if (name.contains("HELMET")) {
            addAttribute(meta, Attribute.GENERIC_ARMOR, 2.0, "copper_armor");
        } else if (name.contains("CHESTPLATE")) {
            addAttribute(meta, Attribute.GENERIC_ARMOR, 5.0, "copper_armor");
        } else if (name.contains("LEGGINGS")) {
            addAttribute(meta, Attribute.GENERIC_ARMOR, 4.0, "copper_armor");
        } else if (name.contains("BOOTS")) {
            addAttribute(meta, Attribute.GENERIC_ARMOR, 1.0, "copper_armor");
        }
    }

    private void addAttribute(ItemMeta meta, Attribute attr, double value, String key) {
        AttributeModifier modifier = new AttributeModifier(
                new NamespacedKey(plugin, key),
                value,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.ANY
        );
        meta.addAttributeModifier(attr, modifier);
    }
}
