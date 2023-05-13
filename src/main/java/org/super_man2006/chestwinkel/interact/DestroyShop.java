package org.super_man2006.chestwinkel.interact;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.super_man2006.chestwinkel.ChestWinkel;

import java.util.Objects;

public class DestroyShop implements Listener {

    @EventHandler
    public void onBreak(BlockDestroyEvent e) {
        int i = 0;
        int locNum = 0;
        boolean looking = true;
        boolean found = false;
        while (looking) {
            if (Objects.equals(ChestWinkel.unbreakable.get(i).toString(), e.getBlock().getLocation().toBlockLocation().toString())) {
                locNum = i;
                looking = false;
                found = true;
            }
            i++;
        }
        if (found) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        int i = 0;
        int locNum = 0;
        boolean looking = true;
        boolean found = false;
        while (looking) {
            if (i >= ChestWinkel.unbreakable.size()) {
                looking = false;
            } else if (Objects.equals(ChestWinkel.unbreakable.get(i).toString(), e.getBlock().getLocation().toBlockLocation().toString())) {
                locNum = i;
                looking = false;
                found = true;
            }
            i++;
        }
        if (found) {
            e.setCancelled(true);
        }
    }
}
