package org.super_man2006.chestwinkel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.super_man2006.geldapi.utils.IsInt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class Settings {

    public static int shopLimit;
    public static boolean allowChests;

    public static void load() {
        File file = ChestWinkel.settingsFile;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if(!file.canRead()) { return; }

        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            HashMap<String, String> settings;
            JsonElement element = gson.fromJson(reader, JsonElement.class);
            settings = gson.fromJson(element, new TypeToken<HashMap<String, String>>(){}.getType());

            if (settings == null) {
                return;
            }

            if (settings.containsKey("shop_limit") && IsInt.IsInt(settings.get("shop_limit"))) {
                shopLimit = Integer.parseInt(settings.get("shop_limit"));

                if (shopLimit < 0) {
                    shopLimit = -1;
                }
            } else {
                shopLimit = -1;
            }

            if (settings.containsKey("allow_chests") && ( settings.get("allow_chests").toLowerCase().equals("true") || settings.get("allow_chests").toLowerCase().equals("false"))) {
                allowChests = Boolean.parseBoolean(settings.get("allow_chests"));
            } else {
                allowChests = false;
            }

        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("[Chest-Winkel] Failed to load settings.");
            throw new RuntimeException(e);
        }
    }
}
