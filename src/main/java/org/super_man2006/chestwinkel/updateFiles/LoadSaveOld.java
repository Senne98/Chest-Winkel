package org.super_man2006.chestwinkel.updateFiles;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.super_man2006.chestwinkel.ChestWinkel;
import org.super_man2006.geldapi.utils.Base64;

import java.io.*;
import java.util.List;

@Deprecated
public class LoadSaveOld {

    public static void load(){
        File file = ChestWinkel.shopsFile;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(!file.canRead()) { return; }
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            List<String> base64List;
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            base64List = gson.fromJson(jsonArray, new TypeToken<List<String>>(){}.getType());

            if (base64List == null || base64List.isEmpty()) {
                return;
            }

            base64List.forEach( s -> {
                try {
                    Update.shopOldList.add((Shop) Base64.decode(s));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("[Chest-Winkel] Failed to load all chest shops.");
            throw new RuntimeException(e);
        }
    }

}
