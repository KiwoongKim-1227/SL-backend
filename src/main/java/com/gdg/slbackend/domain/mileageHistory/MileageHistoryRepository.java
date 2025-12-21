package com.gdg.slbackend.domain.mileageHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MileageHistoryRepository
        extends JpaRepository<MileageHistory, Long> {

    List<MileageHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}