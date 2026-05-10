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
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OreListenerTest {

    private ServerMock server;
    private CopperCurse plugin;
    private OreListener listener;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = mock(CopperCurse.class);
        listener = new OreListener(plugin);
        
        when(plugin.isPluginEnabled()).thenReturn(true);
        FileConfiguration config = new YamlConfiguration();
        config.set("mining.diamond-limit", 5);
        when(plugin.getConfig()).thenReturn(config);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testDiamondLimit_NotReached() {
        PlayerMock player = server.addPlayer();
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(Material.DIAMOND_ORE);
        when(block.getWorld()).thenReturn(player.getWorld());
        when(block.getLocation()).thenReturn(player.getLocation());

        BlockBreakEvent event = new BlockBreakEvent(block, player);
        listener.onOreBreak(event);

        int count = player.getPersistentDataContainer().getOrDefault(Constants.DIAMONDS_MINED_KEY, PersistentDataType.INTEGER, 0);
        assert count == 1;
    }

    @Test
    void testDiamondLimit_Reached() {
        PlayerMock player = server.addPlayer();
        player.getPersistentDataContainer().set(Constants.DIAMONDS_MINED_KEY, PersistentDataType.INTEGER, 5);
        
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(Material.DIAMOND_ORE);
        when(block.getWorld()).thenReturn(player.getWorld());
        when(block.getLocation()).thenReturn(player.getLocation());

        BlockBreakEvent event = new BlockBreakEvent(block, player);
        listener.onOreBreak(event);

        assert event.isDropItems() == false;
        // Verify message was sent
        player.assertSaid("§cLa maldición del cobre ha evaporado el diamante...");
    }
}
