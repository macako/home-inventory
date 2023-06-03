package dev.macako.homeinventory.userservice.infrastructure.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorDetails {
  private LocalDateTime timestamp;
  private String message;
  private String details;
}
