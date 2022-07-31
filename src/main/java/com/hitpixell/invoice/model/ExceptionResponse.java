package com.hitpixell.invoice.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ExceptionResponse {
  private Date timestamp;
  private String message;
  private String details;
}