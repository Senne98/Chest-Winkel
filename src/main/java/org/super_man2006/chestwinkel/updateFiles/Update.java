package org.super_man2006.chestwinkel.updateFiles;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.utils.Coordinate;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;
import org.super_man2006.geldapi.currency.Currency;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Update {

    public static List<Shop> shopOldList = new ArrayList<>();
    public static void update() {
        LoadSaveOld.load();

        if (!shopOldList.isEmpty()) {
            shopOldList.forEach(shopOld -> {
                PersistentDataContainer data = shopOld.getLocation().getChunk().getPersistentDataContainer();
                Location loc = shopOld.getLocation();
                Location signLoc = shopOld.getSignLocation();
                data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ())), new CoordinateDataType(), new Coordinate(loc));
                data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(signLoc.getBlockX()) + String.valueOf(signLoc.getBlockY()) + String.valueOf(signLoc.getBlockZ())), new CoordinateDataType(), new Coordinate(signLoc));

                org.super_man2006.chestwinkel.shop.Shop shop = new org.super_man2006.chestwinkel.shop.Shop(shopOld, Currency.Currency(ChestWinkel.currencys.get(0)));
            });
        }

        try {
            FileWriter writer = new FileWriter(ChestWinkel.versionFile);

            String version = ChestWinkel.plugin.getPluginMeta().getVersion();

            writer.write(version);
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
