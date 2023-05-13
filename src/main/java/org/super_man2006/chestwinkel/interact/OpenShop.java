package org.super_man2006.chestwinkel.interact;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.data.Shop;
import org.super_man2006.chestwinkel.gui.ShopOwnerGui;
import org.super_man2006.geldapi.player.Balance;

import java.util.Objects;

import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class OpenShop implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if (e.getAction() != RIGHT_CLICK_BLOCK) {
            return;
        }
        Location clickLocation = e.getClickedBlock().getLocation().toBlockLocation();
        if (!(ChestWinkel.unbreakable.contains(clickLocation))) {
            return;
        }

        Shop shop = new Shop();
        int i = 0;
        boolean looking = true;
        boolean found = false;
        while (looking) {
            if (Objects.equals(ChestWinkel.shopList.get(i).getLocation().toString(), clickLocation.toString()) || Objects.equals(ChestWinkel.shopList.get(i).getSignLocation().toString(), clickLocation.toString())) {
                shop = ChestWinkel.shopList.get(i);
                found = true;
            }
            i++;
            if (i >= ChestWinkel.shopList.size() || found) {
                looking = false;
            }
        }
        if (!found) {
            return;
        }

        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (Objects.equals(shop.getOwnerUuid().toString(), player.getUniqueId().toString()) && block.getType() == Material.BARREL) {
        } else if (Objects.equals(shop.getOwnerUuid().toString(), player.getUniqueId().toString()) && Objects.equals(shop.getSignLocation().toString(), clickLocation.toString())) {
            ShopOwnerGui gui = new ShopOwnerGui();
            player.openInventory(gui.getInventory());
        } else if (!Objects.equals(shop.getOwnerUuid().toString(), player.getUniqueId().toString()) && block.getType() == Material.BARREL) {
            e.setCancelled(true);
        } else if (!Objects.equals(shop.getOwnerUuid().toString(), player.getUniqueId().toString()) && Objects.equals(shop.getSignLocation().toString(), clickLocation.toString())) {
            if (player.getInventory().firstEmpty() < 36 && player.getInventory().firstEmpty() > -1) {
                ItemStack item = new ItemStack(shop.getItem());
                item.setAmount(shop.getAmount());
                player.getInventory().addItem(item);
                Balance.add(shop.getOwnerUuid(), (long) shop.getPrice());
                Balance.add(player.getUniqueId(), (long) (shop.getPrice() * -1));
            } else {
                player.sendMessage(Component.text("You need a free slot in you inventory!", NamedTextColor.DARK_RED));
            }
        }
    }
}
