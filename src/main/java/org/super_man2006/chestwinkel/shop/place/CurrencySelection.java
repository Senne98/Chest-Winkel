package org.super_man2006.chestwinkel.shop.place;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.utils.Coordinate;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;
import org.super_man2006.chestwinkel.utils.UUIDDataType;
import org.super_man2006.geldapi.currency.Currency;

import java.util.UUID;

public class CurrencySelection implements InventoryHolder {

    Inventory inv;

    public CurrencySelection(int tab, Location loc, UUID world, Location signLoc, Material item, int amount, int price, UUID owner, UUID itemDisplay) {
        inv = Bukkit.createInventory(this, 54, Component.text("Select Currency"));

        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text(" "));
        itemStack.setItemMeta(meta);

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, itemStack);
        }

        itemStack = new ItemStack(Material.SUNFLOWER);

        int items;
        if (ChestWinkel.currencys.size() > 45 + (45 * (tab - 1))) {
            items = 45 + (45 * (tab - 1));
        } else {
            items = ChestWinkel.currencys.size();
        }

        for (int i = (45 * (tab - 1)); i < items; i++) {
            meta = itemStack.getItemMeta();
            meta.displayName(Currency.Currency(ChestWinkel.currencys.get(i)).getName().decoration(TextDecoration.ITALIC, false));
            PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey(ChestWinkel.plugin, "currency"), PersistentDataType.STRING, ChestWinkel.currencys.get(i).asString());
            itemStack.setItemMeta(meta);

            inv.setItem(i, itemStack);
        }

        if (tab > 1) {
            itemStack = new ItemStack(Material.ARROW);
            meta = itemStack.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(ChestWinkel.plugin, "tab"), PersistentDataType.INTEGER, tab - 1 );
            itemStack.setItemMeta(meta);

            inv.setItem(45, itemStack);
        }

        itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        meta = itemStack.getItemMeta();
        meta.displayName(Component.text(" "));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(ChestWinkel.plugin, "location"), new CoordinateDataType(), new Coordinate(loc.blockX(), loc.blockY(), loc.blockZ()));
        container.set(new NamespacedKey(ChestWinkel.plugin, "world"), new UUIDDataType(), world);
        container.set(new NamespacedKey(ChestWinkel.plugin, "signlocation"), new CoordinateDataType(), new Coordinate(signLoc.blockX(), signLoc.blockY(), signLoc.blockZ()));
        container.set(new NamespacedKey(ChestWinkel.plugin, "item"), PersistentDataType.BYTE_ARRAY, new ItemStack(item).serializeAsBytes());
        container.set(new NamespacedKey(ChestWinkel.plugin, "amount"), PersistentDataType.INTEGER, amount);
        container.set(new NamespacedKey(ChestWinkel.plugin, "price"), PersistentDataType.INTEGER, price);
        container.set(new NamespacedKey(ChestWinkel.plugin, "owner"), new UUIDDataType(), owner);
        container.set(new NamespacedKey(ChestWinkel.plugin, "display"), new UUIDDataType(), itemDisplay);
        itemStack.setItemMeta(meta);

        inv.setItem(52, itemStack);

        if (ChestWinkel.currencys.size() <= 45 + (45 * (tab - 1))) {
            return;
        }

        itemStack = new ItemStack(Material.ARROW);
        meta = itemStack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(ChestWinkel.plugin, "tab"), PersistentDataType.INTEGER, tab + 1);
        itemStack.setItemMeta(meta);

        inv.setItem(53, itemStack);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
