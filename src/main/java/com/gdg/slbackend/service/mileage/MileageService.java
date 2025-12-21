package com.gdg.slbackend.service.mileage;

import com.gdg.slbackend.domain.mileageHistory.MileageHistory;
import com.gdg.slbackend.domain.mileageHistory.MileageHistoryRepository;
import com.gdg.slbackend.global.enums.MileageType;
import com.gdg.slbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 댓글 작성 시 마일리지 적립 등 User 연관 처리를 담당함.
 */
@Service
@RequiredArgsConstructor
public class MileageService {

    private final UserService userService;
    private final MileageHistoryRepository mileageHistoryRepository;

    @Transactional
    public void earn(MileageType type, Long userId) {
        int amount = type.getAmount();

        int balanceAfter = userService.increaseMileage(userId, amount).getMileage();

        mileageHistoryRepository.save(
                MileageHistory.earn(userId, amount, type, balanceAfter)
        );
    }

    @Transactional
    public void use(MileageType type, Long userId) {
        int amount = type.getAmount(); // 음수

        int balanceAfter = userService.changeMileage(userId, amount);

        mileageHistoryRepository.save(
                MileageHistory.use(userId, amount, type, balanceAfter)
        );
    }

    @Transactional
    public void change(Long userId, MileageType type) {
        int delta = type.getAmount();

        int balanceAfter = userService.changeMileage(userId, delta);

        mileageHistoryRepository.save(
                MileageHistory.create(userId, delta, type, balanceAfter)
        );
    }
}
