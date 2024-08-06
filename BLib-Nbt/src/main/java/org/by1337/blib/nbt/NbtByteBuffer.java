package org.by1337.blib.nbt;

public interface NbtByteBuffer {
    void writeByte(byte b);
    void writeByte(int b);
    void writeDouble(double b);
    double readDouble();
    long readVarLong();
    int readVarInt();
    void writeVarLong(long l);
    void writeVarInt(int i);
    void writeShort(int value);
    short readShort();
    void writeUtf(String string);
    String readUtf();
    void writeFloat(float f);
    float readFloat();
    byte readByte();
    byte[] toByteArray();
    void readBytes(byte[] arr);
    void writeBytes(byte[] bytes);

}

