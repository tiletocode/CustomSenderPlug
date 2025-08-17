package com.hdr.customsenderplug;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest {

    @Test
    public void testRoundDecimalNumbers() {
        // 테스트 케이스 1: 소숫점 숫자가 포함된 문자열 (기본 설정)
        String input1 = "CPU 사용률이 85.678%입니다.";
        String expected1 = "CPU 사용률이 85.7%입니다.";
        assertEquals(expected1, StringUtils.roundDecimalNumbers(input1));

        // 테스트 케이스 2: 여러 소숫점 숫자가 포함된 문자열 (기본 설정)
        String input2 = "메모리: 12.345GB, 디스크: 67.89%";
        String expected2 = "메모리: 12.3GB, 디스크: 67.9%";
        assertEquals(expected2, StringUtils.roundDecimalNumbers(input2));

        // 테스트 케이스 3: 소숫점 숫자가 없는 문자열
        String input3 = "정수만 있습니다: 100, 200";
        String expected3 = "정수만 있습니다: 100, 200";
        assertEquals(expected3, StringUtils.roundDecimalNumbers(input3));

        // 테스트 케이스 4: 소숫점 이하가 한 자리인 경우
        String input4 = "값: 12.3";
        String expected4 = "값: 12.3";
        assertEquals(expected4, StringUtils.roundDecimalNumbers(input4));

        // 테스트 케이스 5: null 입력
        assertNull(StringUtils.roundDecimalNumbers(null));

        // 테스트 케이스 6: 반올림이 필요한 경우
        String input6 = "온도: 23.56도, 습도: 45.78%";
        String expected6 = "온도: 23.6도, 습도: 45.8%";
        assertEquals(expected6, StringUtils.roundDecimalNumbers(input6));
    }

    @Test
    public void testRoundDecimalNumbersWithSpecificPlaces() {
        // 소숫점 2자리 테스트
        String input = "값: 12.3456, 온도: 98.7654도";
        String expected2 = "값: 12.35, 온도: 98.77도";
        assertEquals(expected2, StringUtils.roundDecimalNumbers(input, 2));

        // 소숫점 0자리 (정수로 반올림) 테스트
        String expected0 = "값: 12, 온도: 99도";
        assertEquals(expected0, StringUtils.roundDecimalNumbers(input, 0));

        // 소숫점 3자리 테스트
        String expected3 = "값: 12.346, 온도: 98.765도";
        assertEquals(expected3, StringUtils.roundDecimalNumbers(input, 3));

        // 잘못된 자리수 입력시 기본값(1자리) 사용
        String expected1 = "값: 12.3, 온도: 98.8도";
        assertEquals(expected1, StringUtils.roundDecimalNumbers(input, -1));
        assertEquals(expected1, StringUtils.roundDecimalNumbers(input, 10));
    }
}
