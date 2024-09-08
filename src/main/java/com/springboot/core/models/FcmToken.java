package com.springboot.core.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "fcm_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createdDate", columnDefinition = "datetime(3) default current_timestamp(3)")
    private LocalDate createdDate;

    @Column(name = "createdBy")
    private Integer createdBy;

    @Column(name = "updatedDate", columnDefinition = "datetime(3)")
    private LocalDate updatedDate;

    @Column(name = "updatedBy")
    private Integer updatedBy;

    @Column(name = "deletedDate", columnDefinition = "datetime(3)")
    private LocalDate deletedDate;

    @Column(name = "fcmToken", nullable = false, length = 191)
    private String fcmToken;

    @Column(name = "deviceName", nullable = false, length = 191)
    private String deviceName;

    @Column(name = "deviceHash", nullable = false, length = 191)
    private String deviceHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}