package org.super_man2006.chestwinkel.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.geldapi.tools.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadSave {

    private static File file;

    public static void file() {
        file = ChestWinkel.shopsFileStatic;
    }

    public static void save(){
        file();
        if (ChestWinkel.shopList == null) {
            return;
        }
        List<String> base64List = new ArrayList<>();
        for (int i = 0; i < ChestWinkel.shopList.size(); i++) {
            base64List.add(Base64.encode(ChestWinkel.shopList.get(i)));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
            gson.toJson(gson.toJsonTree(base64List, new TypeToken<List<String>>(){}.getType()), writer);
            writer.flush();

        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("[Chest-Winkel] Failed to save all chest shops.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            Bukkit.getLogger().warning("[Chest-Winkel] Failed to save all chest shops.");
            throw new RuntimeException(e);
        }
    }

    public static void load(){
        file();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(!file.canRead()) { return; }
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            List<String> base64List;
            JsonElement element = gson.fromJson(reader, JsonElement.class);
            base64List = gson.fromJson(element, new TypeToken<List<String>>(){}.getType());

            if (base64List == null) {
                return;
            }

            for (int i = 0; i < base64List.size(); i++) {
                Shop shop = (Shop) Base64.decode(base64List.get(i));
                ChestWinkel.shopList.add(shop);
                ChestWinkel.unbreakable.add(shop.getLocation());
                ChestWinkel.unbreakable.add(shop.getSignLocation());
            }

        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("[Chest-Winkel] Failed to load all chest shops.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            Bukkit.getLogger().warning("[Chest-Winkel] Failed to load all chest shops.");
            throw new RuntimeException(e);
        }
    }
}
