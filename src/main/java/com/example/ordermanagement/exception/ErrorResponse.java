package com.example.ordermanagement.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response structure")
public class ErrorResponse {
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when error occurred", example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "HTTP status code", example = "400")
    private int status;
    
    @Schema(description = "Error type", example = "Validation Failed")
    private String error;
    
    @Schema(description = "Error message", example = "Request validation failed")
    private String message;
    
    @Schema(description = "Detailed error information")
    private Map<String, String> details;
    
    @Schema(description = "Request ID for tracing", example = "req_987654321")
    private String requestId;
    
    @Schema(description = "Documentation URL", example = "https://docs.example.com/errors")
    private String documentation;
}