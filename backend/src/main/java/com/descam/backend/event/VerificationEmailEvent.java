package com.descam.backend.event;

public record VerificationEmailEvent(
  String recipient,
  String verificationUrl
) {
  
}
