package com.lzw.niodemo.service;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.function.Consumer;

public class LZWFileReader implements Closeable {
    public static final int CAPACITY = 100;
    private final FileChannel inChannel;

    public LZWFileReader(String fileName) throws FileNotFoundException {
        RandomAccessFile aFile = new RandomAccessFile(fileName, "r");
        inChannel = aFile.getChannel();
    }

    public String read() throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        int bytesRead = inChannel.read(buffer);
        while (bytesRead != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                contentBuilder.append((char) buffer.get());
            }
            buffer.clear();
            bytesRead = inChannel.read(buffer);
        }
        return contentBuilder.toString();
    }

    public void transferTo(FileChannel toChannel) throws IOException {
        inChannel.transferTo(0, inChannel.size(), toChannel);
    }

    public void writeStringTo(Consumer<String> consumer) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        int bytesRead = inChannel.read(buffer);
        while (bytesRead != -1) {
            buffer.flip();
            StringBuilder contentBuilder = new StringBuilder();
            while (buffer.hasRemaining()) {
                contentBuilder.append((char) buffer.get());
            }
            consumer.accept(contentBuilder.toString());
            buffer.clear();
            bytesRead = inChannel.read(buffer);
        }
    }

    public void writeByteBufferTo(Consumer<ByteBuffer> consumer) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        int bytesRead = inChannel.read(buffer);
        while (bytesRead != -1) {
            buffer.flip();
            consumer.accept(buffer);
            buffer.clear();
            bytesRead = inChannel.read(buffer);
        }
    }

    @Override
    public void close() throws IOException {
        if (inChannel != null && inChannel.isOpen())
            inChannel.close();
    }

    public FileChannel getInFileChannel() {
        return inChannel;
    }
}
