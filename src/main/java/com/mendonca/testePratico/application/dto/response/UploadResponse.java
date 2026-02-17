package com.mendonca.testePratico.application.dto.response;

import com.mendonca.testePratico.domain.entity.FileProcessing;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UploadResponse(
 String fileId,
 String status,
 String message,
 String progressUrl
) {
 public static UploadResponse from(FileProcessing processing) {
     return new UploadResponse(
         processing.getId().value(),
         processing.getStatus().name(),
         "File uploaded successfully. Processing started.",
         "/api/progress/" + processing.getId().value()
     );
 }
 
 public static UploadResponse error(String message) {
     return new UploadResponse(null, "ERROR", message, null);
 }
}