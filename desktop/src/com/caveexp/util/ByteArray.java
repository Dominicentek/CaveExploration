package com.caveexp.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteArray {
    private byte[] array;
    private int pointer = 0;
    public ByteArray(byte[] array) {
        this.array = array;
    }
    public ByteArray(int capacity) {
        this.array = new byte[capacity];
    }
    public ByteArray writeByte(byte value) {
        array[pointer] = value;
        pointer++;
        return this;
    }
    public ByteArray writeByte(int value) {
        writeByte((byte)value);
        return this;
    }
    public ByteArray writeShort(short value) {
        writeByte((value >> 8) & 0xFF);
        writeByte(value & 0xFF);
        return this;
    }
    public ByteArray writeShort(int value) {
        writeShort((short)value);
        return this;
    }
    public ByteArray writeInt(int value) {
        writeByte((value >> 24) & 0xFF);
        writeByte((value >> 16) & 0xFF);
        writeByte((value >> 8) & 0xFF);
        writeByte(value & 0xFF);
        return this;
    }
    public ByteArray writeLong(long value) {
        writeByte((byte)((value >> 56) & 0xFF));
        writeByte((byte)((value >> 48) & 0xFF));
        writeByte((byte)((value >> 40) & 0xFF));
        writeByte((byte)((value >> 32) & 0xFF));
        writeByte((byte)((value >> 24) & 0xFF));
        writeByte((byte)((value >> 16) & 0xFF));
        writeByte((byte)((value >> 8) & 0xFF));
        writeByte((byte)(value & 0xFF));
        return this;
    }
    public ByteArray writeFloat(float value) {
        writeInt(Float.floatToIntBits(value));
        return this;
    }
    public ByteArray writeFloat(double value) {
        writeLong(Double.doubleToLongBits(value));
        return this;
    }
    public ByteArray writeDouble(double value) {
        writeBuffer(ByteBuffer.allocate(8).putDouble(value));
        return this;
    }
    public ByteArray writeString(String string) {
        for (char character : string.toCharArray()) {
            writeCharacter(character);
        }
        return this;
    }
    public ByteArray writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
        return this;
    }
    public ByteArray writeBitArray(boolean[] value) {
        int bytes = (int)Math.ceil(value.length / 8.0);
        for (int i = 0; i < bytes; i++) {
            byte byteValue = 0;
            for (int j = 0; j < 8; j++) {
                if (i * 8 + j >= value.length) break;
                int shift = 7 - j;
                if (value[i * 8 + j]) byteValue += 1 << shift;
            }
            writeByte(byteValue);
        }
        return this;
    }
    public ByteArray writeCharacter(char character) {
        writeByte(character);
        return this;
    }
    public ByteArray writeBuffer(ByteBuffer buffer) {
        writeArray(buffer.array());
        return this;
    }
    public ByteArray writeArray(byte[] array) {
        for (byte value : array) {
            writeByte(value);
        }
        return this;
    }
    public ByteArray writeStream(InputStream stream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((bytesRead = stream.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        writeArray(out.toByteArray());
        return this;
    }
    public byte[] array() {
        return array;
    }
    public int length() {
        return array.length;
    }
    public void position(int position) {
        pointer = position;
    }
    public int position() {
        return pointer;
    }
    public byte readByte() {
        byte value = array[pointer];
        pointer++;
        return value;
    }
    public int readUnsignedByte() {
        return Byte.toUnsignedInt(readByte());
    }
    public short readShort() {
        return (short)((readUnsignedByte() << 8) + readUnsignedByte());
    }
    public int readUnsignedShort() {
        return Short.toUnsignedInt(readShort());
    }
    public int readInt() {
        return (readUnsignedShort() << 16) + readUnsignedShort();
    }
    public long readUnsignedInt() {
        return Integer.toUnsignedLong(readInt());
    }
    public long readLong() {
        return (readUnsignedInt() << 32) + readUnsignedInt();
    }
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }
    public char readChar() {
        return (char)readByte();
    }
    public String readString(int length) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < length; i++) {
            string.append(readChar());
        }
        return string.toString();
    }
    public boolean readBoolean() {
        return readByte() != 0;
    }
    public boolean[] readBitArray(int length) {
        int remainingLength = length;
        boolean[] bits = new boolean[length];
        int currentIndex = 0;
        while (remainingLength > 0) {
            byte value = readByte();
            for (int i = 0; i < 8; i++) {
                int shift = 7 - i;
                if (i + currentIndex >= length) break;
                bits[currentIndex + i] = ((value >> shift) & 1) == 1;
            }
            remainingLength -= 8;
            currentIndex += 8;
        }
        return bits;
    }
    public ByteArray writeToStream(OutputStream stream, int position, int length) throws IOException {
        stream.write(array, position, length);
        return this;
    }
    public ByteArray writeToStream(OutputStream stream) throws IOException {
        stream.write(array);
        return this;
    }
}