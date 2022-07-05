package com.video.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class UploadFile {

    public void doUploadFile(String path, String fileName, byte[] bytes) throws IOException {
        Files.write(Paths.get(path + File.separator + fileName), bytes);
    }
}
