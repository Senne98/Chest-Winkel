package org.super_man2006.chestwinkel.shop.place;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.utils.Coordinate;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;
import org.super_man2006.chestwinkel.shop.Shop;
import org.super_man2006.chestwinkel.utils.UUIDDataType;
import org.super_man2006.geldapi.currency.Currency;

import java.util.UUID;

public class SelectCurrencyClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getClickedInventory().getHolder() instanceof CurrencySelection)) {
            return;
        }
        e.setCancelled(true);

        PersistentDataContainer data = e.getClickedInventory().getItem(52).getItemMeta().getPersistentDataContainer();

        UUID itemDisplay = data.get(new NamespacedKey(ChestWinkel.plugin, "display"), new UUIDDataType());
        UUID owner = data.get(new NamespacedKey(ChestWinkel.plugin, "owner"), new UUIDDataType());
        int price = data.get(new NamespacedKey(ChestWinkel.plugin, "price"), PersistentDataType.INTEGER);
        int amount = data.get(new NamespacedKey(ChestWinkel.plugin, "amount"), PersistentDataType.INTEGER);
        Material item = ItemStack.deserializeBytes(data.get(new NamespacedKey(ChestWinkel.plugin, "item"), PersistentDataType.BYTE_ARRAY)).getType();
        Coordinate signLoc = data.get(new NamespacedKey(ChestWinkel.plugin, "signlocation"), new CoordinateDataType());
        UUID worldUUID = data.get(new NamespacedKey(ChestWinkel.plugin, "world"), new UUIDDataType());
        Coordinate loc = data.get(new NamespacedKey(ChestWinkel.plugin, "location"), new CoordinateDataType());
        World world = Bukkit.getServer().getWorld(worldUUID);

        Material material = e.getClickedInventory().getItem(e.getSlot()).getType();
        if (material.equals(Material.SUNFLOWER)) {
            PersistentDataContainer dataClick = e.getClickedInventory().getItem(e.getSlot()).getItemMeta().getPersistentDataContainer();
            Currency currency = Currency.Currency(NamespacedKey.fromString(dataClick.get(new NamespacedKey(ChestWinkel.plugin, "currency"), PersistentDataType.STRING)));

            Sign sign = (Sign) new Location(world, signLoc.getX(), signLoc.getY(), signLoc.getZ()).getBlock().getState();
            sign.line( 0, Component.text("=====SHOP!=====").color(NamedTextColor.WHITE));
            sign.line( 1, Component.text("amount: " + amount).color(NamedTextColor.GREEN));
            sign.line( 2, Component.text("price: " + price).append(currency.getSymbol()).color(NamedTextColor.DARK_RED));
            sign.line( 3, Component.text("===============").color(NamedTextColor.WHITE));
            sign.setEditable(false);
            sign.update();

            new Shop(loc.toLocation(world), signLoc.toLocation(world), item, amount, price, owner, itemDisplay, currency);
            e.getWhoClicked().closeInventory();
            return;
        }

        if (material.equals(Material.ARROW)) {
            ItemStack itemStack = e.getClickedInventory().getItem(e.getSlot());
            ItemMeta meta = itemStack.getItemMeta();
            PersistentDataContainer dataClick = meta.getPersistentDataContainer();
            InventoryHolder gui = new CurrencySelection(dataClick.get(new NamespacedKey(ChestWinkel.plugin, "tab"), PersistentDataType.INTEGER), loc.toLocation(world), worldUUID, signLoc.toLocation(world), material, amount, price, owner, itemDisplay);
            e.getWhoClicked().openInventory(gui.getInventory());
            return;
        }
    }
}
