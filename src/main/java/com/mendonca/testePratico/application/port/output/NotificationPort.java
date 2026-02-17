package com.mendonca.testePratico.application.port.output;

public interface NotificationPort {
    void notifyProgress(String fileId, int progress);
    void notifyCompletion(String fileId, String result);
    void notifyError(String fileId, String error);
	void notifyComplete(String fileId);
}