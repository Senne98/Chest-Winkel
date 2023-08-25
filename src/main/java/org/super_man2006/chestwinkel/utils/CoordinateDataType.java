package org.super_man2006.chestwinkel.utils;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;

public class CoordinateDataType implements PersistentDataType<byte[], Coordinate> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Coordinate> getComplexType() {
        return Coordinate.class;
    }

    @Override
    public byte[] toPrimitive(Coordinate complex, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
        bb.putInt(complex.getX());
        bb.putInt(complex.getY());
        bb.putInt(complex.getZ());
        return bb.array();
    }

    @Override
    public Coordinate fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        return new Coordinate(bb.getInt(), bb.getInt(), bb.getInt());
    }
}
