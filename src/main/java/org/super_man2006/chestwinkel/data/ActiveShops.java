package org.super_man2006.chestwinkel.data;

import org.bukkit.Location;
import org.super_man2006.chestwinkel.ChestWinkel;

import java.util.Objects;

public class ActiveShops {

    public static boolean contains(Location loc) {
        int i = 0;
        int locNum = 0;
        boolean looking = true;
        boolean found = false;
        while (looking) {
            if (i >= ChestWinkel.unbreakable.size()) {
                looking = false;
            } else if (Objects.equals(ChestWinkel.unbreakable.get(i).toString(),loc.toString())) {
                locNum = i;
                looking = false;
                return true;
            }
            i++;
        }
        return false;
    }

    public static Shop getShop(Location loc) {
        int i = 0;
        int locNum = 0;
        boolean looking = true;
        boolean found = false;
        while (looking) {
            if (i >= ChestWinkel.shopList.size()) {
                looking = false;
            } else if (Objects.equals(ChestWinkel.shopList.get(i).getLocation().toString(),loc.toString()) || Objects.equals(ChestWinkel.shopList.get(i).getSignLocation().toString(),loc.toString())) {
                locNum = i;
                looking = false;
                return ChestWinkel.shopList.get(i);
            }
            i++;
        }
        return null;
    }
}
