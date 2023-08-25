package org.super_man2006.chestwinkel.shop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.utils.Coordinate;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;
import org.super_man2006.geldapi.currency.Currency;

import java.util.*;

public final class Shop implements ConfigurationSerializable {

    private Location location;
    private Location signLocation;
    private Material item;
    private int amount;
    private int price;
    private UUID ownerUuid;
    private UUID itemDisplay;
    private Currency currency;

    public Shop(Location loc, Location signLoc, Material item, int amount, int price, UUID owner, UUID itemDisplay, Currency currency) {
        this.location = loc;
        this.signLocation = signLoc;
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.ownerUuid = owner;
        this.itemDisplay = itemDisplay;
        this.currency = currency;

        ChestWinkel.shopList.add(this);

        Player player = Bukkit.getPlayer(owner);
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        if (playerData.has(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"))) {
            int previous = playerData.get(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"), PersistentDataType.INTEGER);
            playerData.set(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"), PersistentDataType.INTEGER, previous + 1);
        } else {
            playerData.set(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"), PersistentDataType.INTEGER, 1);
        }

        PersistentDataContainer data = loc.getChunk().getPersistentDataContainer();
        data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ())), new CoordinateDataType(), new Coordinate(loc));
        data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(signLoc.getBlockX()) + String.valueOf(signLoc.getBlockY()) + String.valueOf(signLoc.getBlockZ())), new CoordinateDataType(), new Coordinate(signLoc));
    }

    public Shop(org.super_man2006.chestwinkel.updateFiles.Shop oldShop, Currency currency) {
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

        ChestWinkel.shopList.add(this);

        PersistentDataContainer data = loc.getChunk().getPersistentDataContainer();
        data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ())), new CoordinateDataType(), new Coordinate(loc));
        data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(signLoc.getBlockX()) + String.valueOf(signLoc.getBlockY()) + String.valueOf(signLoc.getBlockZ())), new CoordinateDataType(), new Coordinate(signLoc));
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public void setSignLocation(Location signLocation) {
        this.signLocation = signLocation;
    }

    public Material getItem() {
        return item;
    }

    public void setItem(Material item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public UUID getItemDisplay() {
        return itemDisplay;
    }

    public void setItemDisplay(ItemDisplay itemDisplay) {
        this.itemDisplay = itemDisplay.getUniqueId();
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void newShop(Location location, Location signLocation, UUID uuid, Material item, int amount, int price, ItemDisplay itemDisplay) {
        this.location = location.toBlockLocation();
        this.signLocation = signLocation.toBlockLocation();
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.ownerUuid = uuid;
        this.itemDisplay = itemDisplay.getUniqueId();
    }

    public static boolean isShop(Location location) {
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

    public static Shop getShop(Location loc) {
        int i = 0;
        int locNum = 0;
        boolean looking = true;
        boolean found = false;
        while (looking) {
            if (i >= ChestWinkel.shopList.size()) {
                looking = false;
            } else if (Objects.equals(ChestWinkel.shopList.get(i).getLocation().toString(),loc.toString()) || Objects.equals(ChestWinkel.shopList.get(i).getSignLocation().toString(),loc.toString())) {
                locNum = i;
                looking = false;
                return ChestWinkel.shopList.get(i);
            }
            i++;
        }
        return null;
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
    public Shop(Map<String, Object> data) {
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
