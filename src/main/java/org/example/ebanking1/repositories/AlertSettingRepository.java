package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.AlertSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertSettingRepository extends JpaRepository<AlertSetting, String> {
    Optional<AlertSetting> findByClient(String clientId);
}