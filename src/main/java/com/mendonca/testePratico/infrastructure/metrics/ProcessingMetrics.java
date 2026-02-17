package com.mendonca.testePratico.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessingMetrics {
    
    private final Counter uploadCounter;
    private final Counter successCounter;
    private final Counter errorCounter;
    private final Timer processingTimer;
    
    public ProcessingMetrics(MeterRegistry registry) {
        this.uploadCounter = Counter.builder("file.upload.total")
                .description("Total number of file uploads")
                .register(registry);
        
        this.successCounter = Counter.builder("file.process.success")
                .description("Number of successfully processed files")
                .register(registry);
        
        this.errorCounter = Counter.builder("file.process.error")
                .description("Number of failed file processings")
                .register(registry);
        
        this.processingTimer = Timer.builder("file.process.duration")
                .description("Time taken to process files")
                .register(registry);
    }
    
    public void recordUpload() {
        uploadCounter.increment();
    }
    
    public void recordSuccess() {
        successCounter.increment();
    }
    
    public void recordError() {
        errorCounter.increment();
    }
    
    public Timer.Sample startTimer() {
        return Timer.start();
    }
    
    public void stopTimer(Timer.Sample sample) {
        sample.stop(processingTimer);
    }
}