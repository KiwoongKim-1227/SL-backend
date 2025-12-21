package com.gdg.slbackend.api.mileage.dto;

import com.gdg.slbackend.domain.mileageHistory.MileageHistory;
import com.gdg.slbackend.global.enums.MileageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MileageHistoryResponse {

    private Long id;
    private int amount;
    private int balanceAfter;
    private MileageType type;
    private String description;
    private LocalDateTime createdAt;

    public static MileageHistoryResponse from(MileageHistory history) {
        return new MileageHistoryResponse(
                history.getId(),
                history.getAmount(),
                history.getBalanceAfter(),
                history.getType(),
                history.getType().getDescription(),
                history.getCreatedAt()
        );
    }
}
