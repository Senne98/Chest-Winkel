package org.super_man2006.chestwinkel.shop.interact;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DestroyShop implements Listener {

    @EventHandler
    public void onBreak(BlockDestroyEvent e) {
        if (test(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        if (test(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTntBreak(EntityExplodeEvent e) {
        Location location = e.getLocation();
        PersistentDataContainer data = location.getChunk().getPersistentDataContainer();
        List<Location> locationList = new ArrayList<>();
        Set<NamespacedKey> keys = data.getKeys();

        keys.forEach(key -> {
            if (key.getKey().contains(ChestWinkel.unbreakableKey)) {
                locationList.add(data.get(key, new CoordinateDataType()).toLocation(location.getWorld()));
            }
        });

        e.blockList().removeIf(block -> locationList.contains(block.getLocation()));
    }

    private boolean test(Location location) {
        PersistentDataContainer data = location.getChunk().getPersistentDataContainer();
        List<Location> locationList = new ArrayList<>();
        Set<NamespacedKey> keys = data.getKeys();

        keys.forEach(key -> {
            if (key.getKey().contains(ChestWinkel.unbreakableKey)) {
                locationList.add(data.get(key, new CoordinateDataType()).toLocation(location.getWorld()));
            }
        });

        if (locationList.contains(location)) {
            return true;
        }

        return false;
    }
}
