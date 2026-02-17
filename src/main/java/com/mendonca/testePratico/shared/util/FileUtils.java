package com.mendonca.testePratico.shared.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public final class FileUtils {
    
    private FileUtils() {}
    
    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    public static boolean isValidFileSize(long size, long maxSize) {
        return size <= maxSize;
    }
    
    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                log.error("Error closing resource", e);
            }
        }
    }
}