package org.super_man2006.chestwinkel;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.super_man2006.chestwinkel.shop.interact.*;
import org.super_man2006.chestwinkel.updateFiles.ShopV1;
import org.super_man2006.chestwinkel.updateFiles.Update;
import org.super_man2006.chestwinkel.utils.LoadSave;
import org.super_man2006.chestwinkel.shop.Shop;
import org.super_man2006.chestwinkel.shop.place.CurrencyList;
import org.super_man2006.chestwinkel.shop.place.PlaceShop;
import org.super_man2006.chestwinkel.shop.place.SelectCurrencyClick;
import org.super_man2006.geldapi.Geld_API;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ChestWinkel extends JavaPlugin {

    public static List<Shop> shopList = new ArrayList<>();
    public static List<NamespacedKey> currencys = new ArrayList<>();
    public static ChestWinkel plugin;
    public static String unbreakableKey = "unbreakable";

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        // You can find the plugin ids of your plugins on the page https://bstats.org/what-is-my-plugin-id
        int pluginId = 19041; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        //resources
        // Geld_API.currencyList.forEach((namespacedKey, currency) -> currencys.add(namespacedKey));

        settingsFile = new File(getDataFolder(), "settings.json");
        versionFile = new File(getDataFolder(), "version.txt");
        shopsFile = new File(getDataFolder(), "Shops.json");

        saveResource("settings.json", false);
        Settings.load();

        if (!versionFile.exists()) {
            saveResource("version.txt", false);
            if (shopsFile.exists()) {
                ConfigurationSerialization.registerClass(ShopV1.class, "org.super_man2006.chestwinkel.data.Shop");
                Update.update();
            } else {
                saveResource("Shops.json", false);
            }
        } else {
            ConfigurationSerialization.registerClass(Shop.class);
            LoadSave.load();
        }

        Geld_API.currencyList.forEach((namespacedKey, currency) -> currencys.add(namespacedKey));

        //Events
        getServer().getPluginManager().registerEvents(new PlaceShop(), this);
        getServer().getPluginManager().registerEvents(new MakeDouble(), this);
        getServer().getPluginManager().registerEvents(new DestroyShop(), this);
        getServer().getPluginManager().registerEvents(new OpenShop(), this);
        getServer().getPluginManager().registerEvents(new ShopOwnerGui(), this);
        getServer().getPluginManager().registerEvents(new FillShop(), this);
        getServer().getPluginManager().registerEvents(new CurrencyList(), this);
        getServer().getPluginManager().registerEvents(new SelectCurrencyClick(), this);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                LoadSave.save();
            }
        };

        runnable.runTaskTimer(this, 720000, 720000);
    }
    public static File shopsFile;
    public static File versionFile;
    public static File settingsFile;

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //save shops
        LoadSave.save();
    }
}
