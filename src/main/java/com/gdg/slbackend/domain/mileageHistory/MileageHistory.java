package com.gdg.slbackend.domain.mileageHistory;

import com.gdg.slbackend.global.enums.MileageType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageHistory {

    @Id @GeneratedValue
    private Long id;

    private Long userId;
    private int amount;
    private int balanceAfter;

    @Enumerated(EnumType.STRING)
    private MileageType type;

    private LocalDateTime createdAt;

    @Builder
    public static MileageHistory earn(
            Long userId,
            int amount,
            MileageType type,
            int balanceAfter
    ) {
        MileageHistory history = new MileageHistory();
        history.userId = userId;
        history.amount = amount;
        history.type = type;
        history.balanceAfter = balanceAfter;
        history.createdAt = LocalDateTime.now();
        return history;
    }

    public static MileageHistory use(
            Long userId,
            int amount,
            MileageType type,
            int balanceAfter
    ) {
        return create(userId, amount, type, balanceAfter);
    }

    public static MileageHistory create(
            Long userId,
            int amount,
            MileageType type,
            int balanceAfter
    ) {
        return earn(userId, amount, type, balanceAfter);
    }
}
