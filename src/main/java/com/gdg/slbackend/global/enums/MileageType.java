package com.gdg.slbackend.global.enums;

import lombok.Getter;

@Getter
public enum MileageType {

//    QUESTION_ANSWER(10, "질문 답변 작성"),
    RESOURCE_UPLOAD_REWARD(+100, "자료 업로드 보상"),
    RESOURCE_DOWNLOAD(-100, "자료 다운로드 차감");

    private final int amount;
    private final String description;

    MileageType(int amount, String description) {
        this.amount = amount;
        this.description = description;
    }

}
