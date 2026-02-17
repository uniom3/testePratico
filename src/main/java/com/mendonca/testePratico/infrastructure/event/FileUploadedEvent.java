package com.mendonca.testePratico.infrastructure.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FileUploadedEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;
	private final String fileId;
    private final String filename;
    private final long fileSize;
    
    public FileUploadedEvent(Object source, String fileId, String filename, long fileSize) {
        super(source);
        this.fileId = fileId;
        this.filename = filename;
        this.fileSize = fileSize;
    }
}