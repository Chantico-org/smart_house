package org.example.smart.models;

import lombok.Builder;
import lombok.Data;

/**
 */
@Builder
@Data
public class SensorValue {
  private String device;
  private String value;
}
