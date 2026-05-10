package com.jdar.coppercurse.listener;

import com.jdar.coppercurse.CopperCurse;
import com.jdar.coppercurse.util.Constants;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Handles combat passives for custom copper items.
 */
public class CombatListener implements Listener {

    private final CopperCurse plugin;
    private final Random random = new Random();

    public CombatListener(CopperCurse plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCombat(EntityDamageByEntityEvent event) {
        if (!plugin.isPluginEnabled()) return;

        // Oxidizing Strike Logic (Attacker)
        if (event.getDamager() instanceof Player attacker) {
            handleOxidizingStrike(attacker, event.getEntity());
        }

        // Glitch Shield Logic (Defender)
        if (event.getEntity() instanceof Player defender && defender.isBlocking()) {
            handleGlitchShield(defender, event.getDamager());
        }
    }

    private void handleOxidizingStrike(Player attacker, org.bukkit.entity.Entity victim) {
        ItemStack item = attacker.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return;
        
        String customId = item.getItemMeta().getPersistentDataContainer().get(Constants.CUSTOM_ITEM_KEY, PersistentDataType.STRING);
        if (Constants.HEAVY_COPPER_SWORD.equals(customId)) {
            double chance = plugin.getConfig().getDouble("passives.oxidizing-strike.chance", 0.15);
            if (random.nextDouble() < chance && victim instanceof LivingEntity livingVictim) {
                int duration = plugin.getConfig().getInt("passives.oxidizing-strike.slowness-duration", 60);
                livingVictim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 1));
                livingVictim.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, duration, 1));
                attacker.sendMessage("§6¡Golpe Oxidante aplicado!");
            }
        }
    }

    private void handleGlitchShield(Player defender, org.bukkit.entity.Entity attacker) {
        ItemStack item = defender.getActiveItem();
        if (item == null || item.getType().isAir()) return;

        String customId = item.getItemMeta().getPersistentDataContainer().get(Constants.CUSTOM_ITEM_KEY, PersistentDataType.STRING);
        if (Constants.COPPER_GLITCH_SHIELD.equals(customId)) {
            // Particles
            spawnGlitchParticles(defender);

            // Repel
            Vector direction = attacker.getLocation().toVector().subtract(defender.getLocation().toVector()).normalize();
            double force = plugin.getConfig().getDouble("shield.repel-force", 0.5);
            attacker.setVelocity(direction.multiply(force));
        }
    }

    private void spawnGlitchParticles(Player player) {
        player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 
                new Particle.DustOptions(Color.ORANGE, 1));
        player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 
                new Particle.DustOptions(Color.PURPLE, 1));
    }
}
