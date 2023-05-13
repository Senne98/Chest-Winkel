package org.super_man2006.chestwinkel;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.super_man2006.chestwinkel.data.LoadSave;
import org.super_man2006.chestwinkel.data.Shop;
import org.super_man2006.chestwinkel.gui.ShopOwnerGui;
import org.super_man2006.chestwinkel.interact.FillShop;
import org.super_man2006.chestwinkel.interact.OpenShop;
import org.super_man2006.chestwinkel.place.PlaceShop;
import org.super_man2006.chestwinkel.interact.DestroyShop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ChestWinkel extends JavaPlugin {

    public static List<Shop> shopList = new ArrayList<>();
    public static List<Location> unbreakable = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().sendMessage(Component.text("[Chest-Winkel] ").append(
                Component.text("enabling plugin" , NamedTextColor.GREEN)));

        //resources
        shopsFileStatic = shopsFile;
        if(!shopsFile.exists()) {
            saveResource("Shops.json", false);
            Component logText = Component.text("[Chest-Winkel] generated Shops.json");
            getServer().sendMessage(logText);
        } else {
            //load shops
            ConfigurationSerialization.registerClass(Shop.class);
            LoadSave.load();
        }

        //Events
        getServer().getPluginManager().registerEvents(new PlaceShop(), this);
        getServer().getPluginManager().registerEvents(new DestroyShop(), this);
        getServer().getPluginManager().registerEvents(new OpenShop(), this);
        getServer().getPluginManager().registerEvents(new ShopOwnerGui(), this);
        getServer().getPluginManager().registerEvents(new FillShop(), this);

        getServer().sendMessage(Component.text("[Chest-Winkel] ").append(
                Component.text("plugin enabled" , NamedTextColor.GREEN)));
    }

    private File shopsFile = new File(getDataFolder(), "Shops.json");
    public File getShopsFile() {
        return shopsFile;
    }
    public static File shopsFileStatic;

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().sendMessage(Component.text("[Chest-Winkel] ").append(
                Component.text("disabling plugin" , NamedTextColor.RED)));

        //save shops
        LoadSave.save();

        getServer().sendMessage(Component.text("[Chest-Winkel] ").append(
                Component.text("plugin disabled" , NamedTextColor.RED)));
    }
}
