package org.super_man2006.chestwinkel.interact;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.super_man2006.chestwinkel.ChestWinkel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DestroyShop implements Listener {

    @EventHandler
    public void onBreak(BlockDestroyEvent e) {
        if (ChestWinkel.unbreakable.size() == 0) {
            return;
        }
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

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        if (ChestWinkel.unbreakable.size() == 0) {
            return;
        }
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

    @EventHandler
    public void onTntBreak(EntityExplodeEvent e) {
        if (ChestWinkel.unbreakable.size() == 0) {
            return;
        }

        List<String> unbreakString = new ArrayList<>();

        for (int i = 0; i < ChestWinkel.unbreakable.size(); i ++) {
            unbreakString.add(ChestWinkel.unbreakable.get(i).toString());
        }

        e.blockList().removeIf(block -> unbreakString.contains(block.getLocation().toString()));
    }
}
