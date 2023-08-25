package org.super_man2006.chestwinkel.shop.place;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.Settings;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;
import org.super_man2006.geldapi.utils.IsInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlaceShop implements Listener {

    @EventHandler
    public void onEdit(SignChangeEvent e) {
        Player player = e.getPlayer();

        if (!Tag.WALL_SIGNS.isTagged(e.getBlock().getType())) {
            return;
        }

        if (Settings.shopLimit > -1) {
            PersistentDataContainer playerData = player.getPersistentDataContainer();
            if (playerData.has(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"))) {
                if (!(playerData.get(new NamespacedKey(ChestWinkel.plugin, "ChestShopAmount"), PersistentDataType.INTEGER) < Settings.shopLimit)) {
                    return;
                }
            }
        }

        Component line1 = Component.text("[shop]").color(NamedTextColor.WHITE);
        if (!(e.line(0).color(NamedTextColor.WHITE) == line1 || IsInt.IsInt(e.getLine(1)) || IsInt.IsInt(e.getLine(2)))) {
            return;
        }

        Sign sign = (Sign) e.getBlock().getState();
        BlockData signData = sign.getBlockData();
        Block block = e.getBlock();
        BlockFace rotation = ((Directional) signData).getFacing();
        Block barrelBlock;
        Location loc = block.getLocation();

        if (rotation == BlockFace.WEST) {
            barrelBlock = loc.add(1, 0, 0).getBlock();
        } else if (rotation == BlockFace.EAST) {
            barrelBlock = loc.add(-1, 0, 0).getBlock();
        }  else if (rotation == BlockFace.NORTH) {
            barrelBlock = loc.add(0, 0, 1).getBlock();
        }  else if (rotation == BlockFace.SOUTH) {
            barrelBlock = loc.add(0, 0, -1).getBlock();
        } else {
            return;

        }

        Location location = barrelBlock.getLocation();

        PersistentDataContainer data = location.getChunk().getPersistentDataContainer();
        List<Location> locationList = new ArrayList<>();
        Set<NamespacedKey> keys = data.getKeys();

        keys.forEach(key -> {
            if (key.getKey().contains(ChestWinkel.unbreakableKey)) {
                locationList.add(data.get(key, new CoordinateDataType()).toLocation(location.getWorld()));
            }
        });

        if (locationList.contains(location)) {
            return;
        }

        Barrel barrel = (Barrel) barrelBlock.getState();
        Inventory inv = barrel.getInventory();
        ItemStack[] invList = inv.getContents();
        List<ItemStack> invArray = new ArrayList<>();
        for (int i = 0; i < invList.length; i++) {
            if (invList[i] != null) {
                invArray.add(invList[i]);
            }
        }

        if (invArray.size() == 0) {
            e.line( 0, Component.text("===============").color(NamedTextColor.DARK_RED));
            e.line( 1, Component.text("Need an item").color(NamedTextColor.DARK_RED));
            e.line( 2, Component.text("in the barrel!").color(NamedTextColor.DARK_RED));
            e.line( 3, Component.text("===============").color(NamedTextColor.DARK_RED));
            return;
        }

        Material mat = invArray.get(0).getType();
        for (int i = 0; i < invArray.size(); i++) {
            if (mat != invArray.get(i).getType()) {
                e.line( 0, Component.text("===============").color(NamedTextColor.DARK_RED));
                e.line( 1, Component.text("Only one item").color(NamedTextColor.DARK_RED));
                e.line( 2, Component.text("allowed!").color(NamedTextColor.DARK_RED));
                e.line( 3, Component.text("===============").color(NamedTextColor.DARK_RED));
                return;
            }
        }

        int amount = Integer.parseInt(e.getLine(1));

        if (amount > 64 || amount < 1) {
            e.line( 0, Component.text("===============").color(NamedTextColor.DARK_RED));
            e.line( 1, Component.text("Amount is not").color(NamedTextColor.DARK_RED));
            e.line( 2, Component.text("allowed!").color(NamedTextColor.DARK_RED));
            e.line( 3, Component.text("===============").color(NamedTextColor.DARK_RED));
            return;
        }

        int price = Integer.parseInt(e.getLine(2));

        if (price < 0) {
            e.line( 0, Component.text("===============").color(NamedTextColor.DARK_RED));
            e.line( 1, Component.text("Price is not").color(NamedTextColor.DARK_RED));
            e.line( 2, Component.text("allowed!").color(NamedTextColor.DARK_RED));
            e.line( 3, Component.text("===============").color(NamedTextColor.DARK_RED));
            return;
        }

        ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(barrelBlock.getLocation().add(0.5, 1.5, 0.5), EntityType.ITEM_DISPLAY);
        itemDisplay.setItemStack(new ItemStack(mat));
        Transformation transformation = new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f().add(0.65f, 0.65f, 0.65f), new AxisAngle4f());
        itemDisplay.setTransformation(transformation);
        itemDisplay.setBillboard(Display.Billboard.VERTICAL);

        InventoryHolder gui = new CurrencySelection(1, barrelBlock.getLocation(), barrelBlock.getWorld().getUID(), e.getBlock().getLocation(), mat, amount, price, e.getPlayer().getUniqueId(), itemDisplay.getUniqueId());
        player.openInventory(gui.getInventory());
    }
}
