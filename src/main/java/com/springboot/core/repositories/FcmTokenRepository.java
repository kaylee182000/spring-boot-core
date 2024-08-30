package com.springboot.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.core.models.FcmToken;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

}
