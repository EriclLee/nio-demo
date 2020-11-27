package com.lzw.niodemo.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileReaderTest {

    @Test
    public void read_test() throws IOException {
        String rootPath = this.getClass().getResource("/read_test").getFile();
        LZWFileReader LZWFileReader = new LZWFileReader(rootPath);
        String content = LZWFileReader.read();
        assertEquals(content.length(), 881);
    }

    @Test
    public void read_and_write_byte_buffer_test() throws IOException {
        String srcFile = this.getClass().getResource("/read_test").getFile();
        String tagFile = this.getClass().getResource("/read_test_to").getFile();
        LZWFileReader LZWFileReader = new LZWFileReader(srcFile);
        LZWFileWriter fileWriter = new LZWFileWriter(tagFile);
        LZWFileReader.writeByteBufferTo(fileWriter::write);
    }

    @Test
    public void read_and_write_string_test() throws IOException {
        String srcFile = this.getClass().getResource("/read_test").getFile();
        String tagFile = this.getClass().getResource("/read_test_to").getFile();
        LZWFileReader LZWFileReader = new LZWFileReader(srcFile);
        LZWFileWriter fileWriter = new LZWFileWriter(tagFile);
        LZWFileReader.writeStringTo(fileWriter::write);
    }

    @Test
    public void transfer_to_test() throws IOException {
        String srcFile = this.getClass().getResource("/read_test").getFile();
        String tagFile = this.getClass().getResource("/read_test_to").getFile();
        try (LZWFileWriter fileWriter = new LZWFileWriter(tagFile);
             LZWFileReader LZWFileReader = new LZWFileReader(srcFile)) {
            LZWFileReader.transferTo(fileWriter.getOutFileChannel());
        }
    }

    @Test
    public void transfer_from_test() throws IOException {
        String srcFile = this.getClass().getResource("/read_test").getFile();
        String tagFile = this.getClass().getResource("/read_test_to").getFile();
        try (LZWFileWriter fileWriter = new LZWFileWriter(tagFile);
             LZWFileReader LZWFileReader = new LZWFileReader(srcFile)) {
            fileWriter.transferFrom(LZWFileReader.getInFileChannel());
        }
    }
}