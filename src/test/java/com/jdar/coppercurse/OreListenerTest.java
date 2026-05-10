package com.jdar.coppercurse;

import com.jdar.coppercurse.listener.OreListener;
import com.jdar.coppercurse.util.Constants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.Bukkit;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OreListenerTest {

    @Mock private CopperCurse plugin;
    @Mock private FileConfiguration config;
    @Mock private Player player;
    @Mock private Block block;
    @Mock private World world;
    @Mock private PersistentDataContainer pdc;
    @Mock private BlockBreakEvent event;

    private OreListener listener;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Setup a dummy server if Bukkit.getServer() is null
        if (Bukkit.getServer() == null) {
            Server server = mock(Server.class);
            ItemFactory itemFactory = mock(ItemFactory.class);
            when(server.getItemFactory()).thenReturn(itemFactory);
            // Use reflection to set the server field if setServer is not available or behaves differently
            try {
                Bukkit.setServer(server);
            } catch (UnsupportedOperationException e) {
                Field serverField = Bukkit.class.getDeclaredField("server");
                serverField.setAccessible(true);
                serverField.set(null, server);
            }
        }

        listener = new OreListener(plugin, material -> mock(ItemStack.class));
        
        when(plugin.isPluginEnabled()).thenReturn(true);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("mining.diamond-limit", 5)).thenReturn(5);
        
        when(event.getPlayer()).thenReturn(player);
        when(event.getBlock()).thenReturn(block);
        when(block.getWorld()).thenReturn(world);
        when(block.getLocation()).thenReturn(mock(org.bukkit.Location.class));
        when(player.getPersistentDataContainer()).thenReturn(pdc);
    }

    @Test
    void testDiamondLimit_NotReached() {
        when(block.getType()).thenReturn(Material.DIAMOND_ORE);
        when(pdc.getOrDefault(eq(Constants.DIAMONDS_MINED_KEY), eq(PersistentDataType.INTEGER), anyInt())).thenReturn(0);

        listener.onOreBreak(event);

        verify(pdc).set(eq(Constants.DIAMONDS_MINED_KEY), eq(PersistentDataType.INTEGER), eq(1));
        verify(event, never()).setDropItems(false);
    }

    @Test
    void testDiamondLimit_Reached() {
        when(block.getType()).thenReturn(Material.DIAMOND_ORE);
        when(pdc.getOrDefault(eq(Constants.DIAMONDS_MINED_KEY), eq(PersistentDataType.INTEGER), anyInt())).thenReturn(5);

        listener.onOreBreak(event);

        verify(event).setDropItems(false);
        verify(player).sendMessage(contains("maldición"));
    }
}
