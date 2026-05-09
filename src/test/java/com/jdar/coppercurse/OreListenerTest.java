package com.jdar.coppercurse;

import com.jdar.coppercurse.listener.OreListener;
import com.jdar.coppercurse.util.Constants;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listener = new OreListener(plugin);
        
        when(plugin.isPluginEnabled()).thenReturn(true);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("mining.diamond-limit", 5)).thenReturn(5);
        
        when(event.getPlayer()).thenReturn(player);
        when(event.getBlock()).thenReturn(block);
        when(block.getWorld()).thenReturn(world);
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
        verify(world).dropItemNaturally(any(), argThat(item -> item.getType() == Material.RAW_COPPER));
        verify(player).sendMessage(contains("maldición"));
    }
}
