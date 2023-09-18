package org.super_man2006.chestwinkel.updateFiles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Deprecated
public class ShopV1 implements ConfigurationSerializable {
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

    public static ShopV1 deserialize(Map<String, Object> data) {
        return new ShopV1(data);
    }

    public ShopV1(Map<String, Object> data) {
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
