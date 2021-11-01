package ru.mail.polis.homework.io.blocking;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * Вам нужно реализовать StructureInputStream, который умеет читать данные из файла.
 * Читать поля нужно ручками, с помощью массива байт и методов {@link #read()}, {@link #read(byte[])} и так далее
 * 3 балла
 */
public class StructureInputStream extends FileInputStream {

    private static final int COUNT_FLAGS = 4;

    private final List<Structure> structures = new ArrayList<>();

    public StructureInputStream(File fileName) throws FileNotFoundException {
        super(fileName);
    }

    /**
     * Метод должен вернуть следующую прочитанную структуру.
     * Если структур в файле больше нет, то вернуть null
     */
    public Structure readStructure() throws IOException {
        if (available() == 0) {
            return null;
        }
        Structure inputStructure = new Structure();
        inputStructure.setId(readLong());
        inputStructure.setName(readString());
        inputStructure.setSubStructures(readSubStructures());
        inputStructure.setCoeff(readFloat());
        boolean[] flags = readFlags();
        inputStructure.setFlag1(flags[0]);
        inputStructure.setFlag2(flags[1]);
        inputStructure.setFlag3(flags[2]);
        inputStructure.setFlag4(flags[3]);
        inputStructure.setParam((byte) read());

        structures.add(inputStructure);
        return inputStructure;
    }

    /**
     * Метод должен вернуть все структуры, которые есть в файле.
     * Если файл уже прочитан, но возвращается полный массив.
     */
    public Structure[] readStructures() throws IOException {
        if (available() == 0) {
            return structures.toArray(new Structure[0]);
        }
        while (available() != 0) {
            readStructure();
        }
        return structures.toArray(new Structure[0]);
    }

    private SubStructure readSubStructure() throws IOException {
        if (available() == 0) {
            return null;
        }
        int id = readInt();
        String name = readString();
        boolean flag = readBoolean();
        double score = readDouble();
        return new SubStructure(id, name, flag, score);
    }

    private SubStructure[] readSubStructures() throws IOException {
        if (available() == 0) {
            return null;
        }
        int size = readInt();
        if (size == -1) {
            return null;
        }
        SubStructure[] subStructures = new SubStructure[size];
        for (int i = 0; i < size; i++) {
            subStructures[i] = readSubStructure();
        }
        return subStructures;
    }

    private long readLong() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        byte[] bytes = new byte[Long.BYTES];
        read(bytes);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    private int readInt() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        byte[] bytes = new byte[Integer.BYTES];
        read(bytes);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    private double readDouble() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
        byte[] bytes = new byte[Double.BYTES];
        read(bytes);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getDouble();
    }

    private float readFloat() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
        byte[] bytes = new byte[Float.BYTES];
        read(bytes);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getFloat();
    }

    private String readString() throws IOException {
        int length = readInt();
        if (length == -1) {
            return null;
        }
        byte[] bytes = new byte[length];
        read(bytes);
        return new String(bytes);
    }

    private boolean[] readFlags() throws IOException {
        boolean[] flags = new boolean[COUNT_FLAGS];
        int inputFlags = read();
        for (int i = 0; i < COUNT_FLAGS; i++) {
            flags[i] = ((inputFlags >> i) & 1) == 1;
        }
        return flags;
    }

    private boolean readBoolean() throws IOException {
        return read() == 1;
    }

}