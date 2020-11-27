package com.lzw.niodemo.service;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class LZWFileWriter implements Closeable {
    public static final int CAPACITY = 100;
    private FileOutputStream outputStream;

    public LZWFileWriter(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (file.exists() && file.delete()) {
            outputStream = new FileOutputStream(fileName, true);
            log.info("File deleted!");
        }
    }

    public void write(String content) {
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        buffer.put(content.getBytes());
        buffer.flip();
        try (FileChannel outChannel = outputStream.getChannel()) {
            outChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(ByteBuffer buffer) {
        buffer.mark();
        try (FileChannel outChannel = outputStream.getChannel()) {
            outChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.reset();
    }

    public FileChannel getOutFileChannel() {
        FileChannel outChannel = null;
        try {
            outChannel = outputStream.getChannel();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outChannel;
    }

    public void transferFrom(FileChannel inFileChannel) {
        try (FileChannel outChannel = outputStream.getChannel()) {
            outChannel.transferFrom(inFileChannel, 0, inFileChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
