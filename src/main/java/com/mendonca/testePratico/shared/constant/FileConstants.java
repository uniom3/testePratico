package com.mendonca.testePratico.shared.constant;

public final class FileConstants {
    
    private FileConstants() {}
    
    public static final String HEADER_017 = "|0000|017|";
    public static final String HEADER_006 = "|0000|006|";
    public static final String SECOND_LINE = "|0001|0|";
    
    public static final int MAX_FILE_SIZE = 1024 * 1024 * 1024; // 1GB
    
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_ERROR = "ERROR";
}