package org.super_man2006.chestwinkel.place;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.data.Shop;
import org.super_man2006.geldapi.test.IsInt;

import java.util.ArrayList;
import java.util.List;

public class PlaceShop implements Listener {

    @EventHandler
    public void onEdit(SignChangeEvent e) {
        Player player = e.getPlayer();

        if (!(e.getBlock().getType() == Material.ACACIA_WALL_SIGN || e.getBlock().getType() == Material.OAK_WALL_SIGN ||
                e.getBlock().getType() == Material.DARK_OAK_WALL_SIGN || e.getBlock().getType() == Material.JUNGLE_WALL_SIGN ||
                e.getBlock().getType() == Material.BIRCH_WALL_SIGN  || e.getBlock().getType() == Material.WARPED_WALL_SIGN ||
                e.getBlock().getType() == Material.CRIMSON_WALL_SIGN  || e.getBlock().getType() == Material.MANGROVE_WALL_SIGN  ||
                e.getBlock().getType() == Material.SPRUCE_WALL_SIGN  || e.getBlock().getType() == Material.BAMBOO_WALL_SIGN  ||
                e.getBlock().getType() == Material.CHERRY_WALL_SIGN)) {
            return;
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

        if (!(barrelBlock.getType() == Material.BARREL) || ChestWinkel.unbreakable.contains(barrelBlock.getLocation())) {
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

        e.line( 0, Component.text("=====SHOP!=====").color(NamedTextColor.WHITE));
        e.line( 1, Component.text("amount: " + amount).color(NamedTextColor.GREEN));
        e.line( 2, Component.text("price: " + price).color(NamedTextColor.DARK_RED));
        e.line( 3, Component.text("===============").color(NamedTextColor.WHITE));

        ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(barrelBlock.getLocation().add(0.5, 1.5, 0.5), EntityType.ITEM_DISPLAY);
        itemDisplay.setItemStack(new ItemStack(mat));
        Transformation transformation = new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f().add(0.65f, 0.65f, 0.65f), new AxisAngle4f());
        itemDisplay.setTransformation(transformation);
        itemDisplay.setBillboard(Display.Billboard.VERTICAL);

        Shop shop = new Shop();
        shop.newShop(barrelBlock.getLocation(), sign.getLocation(), player.getUniqueId(), mat, amount, price, itemDisplay);

        ChestWinkel.shopList.add(shop);
        ChestWinkel.unbreakable.add(shop.getSignLocation());
        ChestWinkel.unbreakable.add(shop.getLocation());
    }
}
