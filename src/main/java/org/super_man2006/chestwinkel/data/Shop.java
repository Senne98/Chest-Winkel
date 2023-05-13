package org.super_man2006.chestwinkel.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Shop implements ConfigurationSerializable {

    private Location location;
    private Location signLocation;
    private Material item;
    private int amount;
    private int price;
    private UUID ownerUuid;
    private UUID itemDisplay;
    private String world;

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

    public ItemDisplay getItemDisplay() {
        return (ItemDisplay) Bukkit.getWorld(world).getEntity(itemDisplay);
    }

    public void setItemDisplay(ItemDisplay itemDisplay) {
        this.itemDisplay = itemDisplay.getUniqueId();
        this.world = itemDisplay.getWorld().getName();
    }

    public void newShop(Location location, Location signLocation, UUID uuid, Material item, int amount, int price, ItemDisplay itemDisplay) {
        this.location = location.toBlockLocation();
        this.signLocation = signLocation.toBlockLocation();
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.ownerUuid = uuid;
        this.itemDisplay = itemDisplay.getUniqueId();
        this.world = itemDisplay.getWorld().getName();
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
        data.put("world", world);
        return data;
    }

    public static Shop deserialize(Map<String, Object> data) {
        return new Shop(data);
    }

    public Shop() {

    }

    public Shop(Map<String, Object> data) {
        this.location = (Location) data.get("location");
        this.signLocation = (Location) data.get("signLocation");
        this.item = (Material) data.get("item");
        this.amount = (int) data.get("amount");
        this.price = (int) data.get("price");
        this.ownerUuid = (UUID) data.get("ownerUuid");
        this.itemDisplay = (UUID) data.get("itemDisplay");
        this.world = (String) data.get("world");
    }
}
