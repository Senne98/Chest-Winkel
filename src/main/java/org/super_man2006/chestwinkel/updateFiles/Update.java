package org.super_man2006.chestwinkel.updateFiles;

import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.persistence.PersistentDataContainer;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.chestwinkel.utils.Coordinate;
import org.super_man2006.chestwinkel.utils.CoordinateDataType;
import org.super_man2006.chestwinkel.utils.LoadSave;
import org.super_man2006.geldapi.currency.Currency;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Update {

    public static List<ShopV1> shopV1List = new ArrayList<>();
    public static List<ShopV2> shopV2List = new ArrayList<>();
    public static void update() {
        if (!ChestWinkel.versionFile.exists()) {

            ChestWinkel.plugin.saveResource("version.txt", false);

            if (ChestWinkel.shopsFile.exists()) {
                updateV1();
                return;
            }

            ChestWinkel.plugin.saveResource("Shops.json", false);
            writeVersion();
            return;

        }

        try {
            List<String> lines = FileUtils.readLines(ChestWinkel.versionFile, Charset.defaultCharset());
            String version = lines.get(0);

            if (Objects.equals(version, "5.0-SNAPSHOT") || Objects.equals(version, "6.0")) {

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ConfigurationSerialization.registerClass(org.super_man2006.chestwinkel.shop.Shop.class);
            LoadSave.load();
    }

    private static void writeVersion() {
        try {
            FileWriter writer = new FileWriter(ChestWinkel.versionFile);

            String version = ChestWinkel.plugin.getPluginMeta().getVersion();

            writer.write(version);
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateV1() {
        ConfigurationSerialization.registerClass(ShopV1.class, "org.super_man2006.chestwinkel.data.Shop");
        LoadSaveOld.loadV1();

        if (!shopV1List.isEmpty()) {
            shopV1List.forEach(shopOld -> {
                PersistentDataContainer data = shopOld.getLocation().getChunk().getPersistentDataContainer();
                Location loc = shopOld.getLocation();
                Location signLoc = shopOld.getSignLocation();
                data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ())), new CoordinateDataType(), new Coordinate(loc));
                data.set(new NamespacedKey(ChestWinkel.plugin, ChestWinkel.unbreakableKey + String.valueOf(signLoc.getBlockX()) + String.valueOf(signLoc.getBlockY()) + String.valueOf(signLoc.getBlockZ())), new CoordinateDataType(), new Coordinate(signLoc));

                ShopV2 shop = new ShopV2(shopOld, Currency.Currency(ChestWinkel.currencys.get(0)));
            });
        }

        writeVersion();
    }

    private static void updateV2() {
        ConfigurationSerialization.registerClass(ShopV2.class, "org.super_man2006.chestwinkel.shop");
        LoadSaveOld.loadV2();

        if (!shopV2List.isEmpty()) {
            shopV2List.forEach(shopOld -> new org.super_man2006.chestwinkel.shop.Shop(shopOld));
        }

        writeVersion();
    }
}
