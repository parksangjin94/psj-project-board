package com.psj.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;


    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0); // 0 과 비교 후 더 큰 수를 사용한다.
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList();  // range(a, b) a,b 사이의 수를 차례대로 스트림에 방출하여 방출되는 Integer마다 실행되는 명령을 forEach로 정의할 수 있다.
    }

    public int currentBarLength(){
        return BAR_LENGTH;
    }
}
