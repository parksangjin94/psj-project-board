package com.psj.projectboard.domain.type;

import lombok.Getter;

// 열거형, 이넘(enum)은 요소, 멤버라 불리는 명명된 값의 집합을 이루는 자료형이다. 열거자 이름들은 일반적으로 해당 언어의 상수 역할을 하는 식별자다. 일부 열거자 자료형은 언어에 기본 소속되어 있을 수 있다.
public enum SearchType {
    TITLE("제목"),
    CONTENT("본문"),
    ID("유저 ID"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    @Getter
    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
