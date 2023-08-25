package org.super_man2006.chestwinkel.shop.interact;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.super_man2006.chestwinkel.ChestWinkel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopOwnerGui implements InventoryHolder, Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getClickedInventory().getHolder() instanceof ShopOwnerGui)) {
            return;
        }
        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        Block block = player.getTargetBlockExact(5, FluidCollisionMode.NEVER);
        Location location = block.getLocation().toBlockLocation();
        int shopNum = 0;
        int i = 0;
        boolean looking = true;
        boolean found = false;
        while (looking) {
            if (Objects.equals(ChestWinkel.shopList.get(i).getLocation().toString(), location.toString()) || Objects.equals(ChestWinkel.shopList.get(i).getSignLocation().toString(), location.toString())) {
                shopNum = i;
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

        int slot = e.getSlot();

        if (slot == 4) {
            Location barrelLoc = ChestWinkel.shopList.get(shopNum).getLocation();
            Location signLoc = ChestWinkel.shopList.get(shopNum).getSignLocation();
            e.getWhoClicked().getWorld().getEntity(ChestWinkel.shopList.get(shopNum).getItemDisplay()).remove();
            ChestWinkel.shopList.remove(shopNum);

            PersistentDataContainer data = barrelLoc.getChunk().getPersistentDataContainer();
            data.remove(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(barrelLoc.getBlockX()) + String.valueOf(barrelLoc.getBlockY()) + String.valueOf(barrelLoc.getBlockZ())));
            data.remove(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(signLoc.getBlockX()) + String.valueOf(signLoc.getBlockY()) + String.valueOf(signLoc.getBlockZ())));

            PersistentDataContainer playerData = player.getPersistentDataContainer();

            if (playerData.has(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"), PersistentDataType.INTEGER)) {
                int previous = playerData.get(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"), PersistentDataType.INTEGER);
                if (previous != 0) {
                    playerData.set(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"), PersistentDataType.INTEGER, previous - 1);
                }
            }

            player.breakBlock(block);
            player.closeInventory();
        }
    }

    private Inventory inv;

    public ShopOwnerGui() {
        inv = Bukkit.createInventory(this, 9, Component.text("Shop: owner gui"));
        init();
    }

    private void init(){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        meta.displayName(Component.text(""));
        item.setItemMeta(meta);

        for (int i = 0; i < 9; i++){
            inv.setItem(i, item);
        }

        item = new ItemStack(Material.TNT);
        meta = item.getItemMeta();
        meta.displayName(Component.text("DESTROY SHOP", NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        inv.setItem(4, item);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
