package org.super_man2006.chestwinkel.shop.interact;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.super_man2006.chestwinkel.shop.Shop;

public class MakeDouble implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        if (!(e.getBlockPlaced().getType().equals(Material.CHEST))) {
            return;
        }

        Location loc = e.getBlock().getLocation();
        if ((!Shop.isShop(new Location(loc.getWorld(), loc.blockX(), loc.blockY(), loc.blockZ()).add(1, 0, 0))) && (!Shop.isShop(new Location(loc.getWorld(), loc.blockX(), loc.blockY(), loc.blockZ()).add(-1, 0, 0))) && (!Shop.isShop(new Location(loc.getWorld(), loc.blockX(), loc.blockY(), loc.blockZ()).add(0, 0, 1))) && (!Shop.isShop(new Location(loc.getWorld(), loc.blockX(), loc.blockY(), loc.blockZ()).add(0, 0, -1)))) {            return;
        }

        e.setCancelled(true);

        Chest chest = (Chest) e.getBlock().getState();

        final boolean[] cancel = {false};

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (chest.getInventory().getSize() == 54) {
                    cancel[0] = true;
                }
            }
        };

        //runnable.runTaskLater(ChestWinkel.plugin, 1L);

        //e.setCancelled(cancel[0]);
    }
}
