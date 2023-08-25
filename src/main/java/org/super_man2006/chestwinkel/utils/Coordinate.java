package org.super_man2006.chestwinkel.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Coordinate {
    int x;
    int y;
    int z;

    public Coordinate (int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate (Location location) {
        this.x = location.blockX();
        this.y = location.blockY();
        this.z = location.blockZ();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }
}
