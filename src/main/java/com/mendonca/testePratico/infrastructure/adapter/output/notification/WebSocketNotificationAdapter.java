package com.mendonca.testePratico.infrastructure.adapter.output.notification;

import com.mendonca.testePratico.application.port.output.NotificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketNotificationAdapter implements NotificationPort {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    @Override
    public void notifyProgress(String fileId, int progress) {
        messagingTemplate.convertAndSend(
            "/topic/progress/" + fileId,
            new ProgressUpdate(fileId, progress)
        );
    }
    
    @Override
    public void notifyComplete(String fileId) {
        messagingTemplate.convertAndSend(
            "/topic/complete/" + fileId,
            new CompleteNotification(fileId, "Processing completed")
        );
    }
    
    @Override
    public void notifyError(String fileId, String error) {
        messagingTemplate.convertAndSend(
            "/topic/error/" + fileId,
            new ErrorNotification(fileId, error)
        );
    }
    
    record ProgressUpdate(String fileId, int progress) {}
    record CompleteNotification(String fileId, String message) {}
    record ErrorNotification(String fileId, String error) {}
	@Override
	public void notifyCompletion(String fileId, String result) {
		
	}
}