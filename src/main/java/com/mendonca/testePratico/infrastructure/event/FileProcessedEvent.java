package com.mendonca.testePratico.infrastructure.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Duration;

@Getter
public class FileProcessedEvent extends ApplicationEvent {
    
	private static final long serialVersionUID = 1L;
	private final String fileId;
    private final boolean success;
    private final String result;
    private final Duration processingTime;
    
    public FileProcessedEvent(Object source, String fileId, boolean success, 
                             String result, Duration processingTime) {
        super(source);
        this.fileId = fileId;
        this.success = success;
        this.result = result;
        this.processingTime = processingTime;
    }
}