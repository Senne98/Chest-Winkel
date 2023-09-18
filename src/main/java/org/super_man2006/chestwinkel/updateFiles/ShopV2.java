package org.super_man2006.chestwinkel.updateFiles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.shop.Shop;
import org.super_man2006.chestwinkel.utils.Coordinate;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;
import org.super_man2006.geldapi.currency.Currency;

import java.util.*;

@Deprecated
public final class ShopV2 implements ConfigurationSerializable {

    private Location location;
    private Location signLocation;
    private Material item;
    private int amount;
    private int price;
    private UUID ownerUuid;
    private UUID itemDisplay;
    private Currency currency;

    public ShopV2(ShopV1 oldShop, Currency currency) {
        this.location = oldShop.getLocation();
        this.signLocation = oldShop.getSignLocation();
        this.item = oldShop.getItem();
        this.amount = oldShop.getAmount();
        this.price = oldShop.getPrice();
        this.ownerUuid = oldShop.getOwnerUuid();
        this.itemDisplay = oldShop.getItemDisplay();
        this.currency = currency;

        Location loc = this.location;
        Location signLoc = this.signLocation;

        Sign sign = (Sign) new Location(loc.getWorld(), signLoc.getX(), signLoc.getY(), signLoc.getZ()).getBlock().getState();
        sign.line( 0, Component.text("=====SHOP!=====").color(NamedTextColor.WHITE));
        sign.line( 1, Component.text("amount: " + amount).color(NamedTextColor.GREEN));
        sign.line( 2, Component.text("price: " + price).append(currency.getSymbol()).color(NamedTextColor.DARK_RED));
        sign.line( 3, Component.text("===============").color(NamedTextColor.WHITE));
        sign.update();

        PersistentDataContainer data = loc.getChunk().getPersistentDataContainer();
        data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ())), new CoordinateDataType(), new Coordinate(loc));
        data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(signLoc.getBlockX()) + String.valueOf(signLoc.getBlockY()) + String.valueOf(signLoc.getBlockZ())), new CoordinateDataType(), new Coordinate(signLoc));

        new Shop(this);
    }
    public Location getLocation() {
        return location;
    }
    public Location getSignLocation() {
        return signLocation;
    }
    public Material getItem() {
        return item;
    }
    public int getAmount() {
        return amount;
    }
    public int getPrice() {
        return price;
    }
    public UUID getOwnerUuid() {
        return ownerUuid;
    }
    public UUID getItemDisplay() {
        return itemDisplay;
    }
    public Currency getCurrency() {
        return currency;
    }
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("location", location);
        data.put("signLocation", signLocation);
        data.put("item", item);
        data.put("amount", amount);
        data.put("price", price);
        data.put("ownerUuid", ownerUuid);
        data.put("itemDisplay", itemDisplay);
        data.put("currency", currency);
        return data;
    }
    public ShopV2(Map<String, Object> data) {
        this.location = (Location) data.get("location");
        this.signLocation = (Location) data.get("signLocation");
        this.item = (Material) data.get("item");
        this.amount = (int) data.get("amount");
        this.price = (int) data.get("price");
        this.ownerUuid = (UUID) data.get("ownerUuid");
        this.itemDisplay = (UUID) data.get("itemDisplay");
        this.currency = (Currency) data.get("currency");
    }
}
