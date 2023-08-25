package org.super_man2006.chestwinkel.shop.interact;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.super_man2006.chestwinkel.shop.Shop;

import java.util.Objects;

public class FillShop implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        Location loc = player.getOpenInventory().getTopInventory().getLocation();
        if (loc == null) {
            return;
        }

        if (!Shop.isShop(loc)) {
            return;
        }
        Shop shop = Shop.getShop(loc);
        if (!Objects.equals(loc.toString(), shop.getLocation().toString())) {
            return;
        }



        if (e.getClick() == ClickType.UNKNOWN || e.getClick() == ClickType.WINDOW_BORDER_LEFT || e.getClick() == ClickType.WINDOW_BORDER_RIGHT) {
            return;
        }
        if (!(e.getCursor().getType() == shop.getItem() || e.getCursor().getType().isAir())) {
            e.setCancelled(true);
            return;
        }
        if (e.getClickedInventory().getItem(e.getSlot()) == null) {
            return;
        }
        if (!(e.getClickedInventory().getItem(e.getSlot()).getType() == shop.getItem() || e.getClickedInventory().getItem(e.getSlot()).getType().isAir())) {
            e.setCancelled(true);
            return;
        }
        if (e.getClick().isShiftClick() || e.getClick().isKeyboardClick() || e.getClick() == ClickType.SWAP_OFFHAND) {
            e.setCancelled(true);
            return;
        }
    }
}
