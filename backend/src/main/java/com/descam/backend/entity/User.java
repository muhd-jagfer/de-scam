package com.descam.backend.entity;
import jakarta.persistence.Column; import jakarta.persistence.Entity; import jakarta.persistence.GeneratedValue; import jakarta.persistence.GenerationType; import jakarta.persistence.Id; import jakarta.persistence.PrePersist; import jakarta.persistence.PreUpdate; import jakarta.persistence.Table;
import java.time.LocalDateTime;
@Entity @Table(name = "users") public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, length = 100)
private String name;

@Column(nullable = false, unique = true, length = 254)
private String email;

@Column(name = "password_hash", nullable = false)
private String passwordHash;

@Column(name = "email_verified", nullable = false)
private boolean emailVerified = false;

@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;

@Column(name = "updated_at", nullable = false)
private LocalDateTime updatedAt;

protected User() {
}

public User(String name, String email, String passwordHash) {
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
}

@PrePersist
protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}

public Long getId() {
    return id;
}

public String getName() {
    return name;
}

public String getEmail() {
    return email;
}

public String getPasswordHash() {
    return passwordHash;
}

public boolean isEmailVerified() {
    return emailVerified;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public LocalDateTime getUpdatedAt() {
    return updatedAt;
}

public void setName(String name) {
    this.name = name;
}

public void setEmail(String email) {
    this.email = email;
}

public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
}

public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
}
}