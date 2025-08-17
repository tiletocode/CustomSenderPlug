package com.hdr.customsenderplug;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String formatDate(long timestamp, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(timestamp);
    }

    public static long toTimestamp(String date, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date lDate = formatter.parse(date);
        return lDate.getTime();
    }

    public static String replace(String str, String regex, Object replacement) {
        String result = str.replaceAll(regex, Matcher.quoteReplacement(replacement.toString()));
        return result;
    }

    /**
     * 문자열에서 소숫점을 포함한 숫자를 찾아서 소숫점 한 자리까지 반올림
     * 
     * @param text 처리할 문자열
     * @return 소숫점 숫자가 반올림된 문자열
     */
    public static String roundDecimalNumbers(String text) {
        Config config = Config.getConfig();
        int decimalPlaces = config.getInt("webhook.message.decimal.places", 1);
        return roundDecimalNumbers(text, decimalPlaces);
    }

    /**
     * 문자열에서 소숫점을 포함한 숫자를 찾아서 지정된 자리까지 반올림
     * 
     * @param text          처리할 문자열
     * @param decimalPlaces 소숫점 자리수 (0-9)
     * @return 소숫점 숫자가 반올림된 문자열
     */
    public static String roundDecimalNumbers(String text, int decimalPlaces) {
        if (text == null) {
            return null;
        }

        if (decimalPlaces < 0 || decimalPlaces > 9) {
            decimalPlaces = 1; // 기본값
        }

        // 소숫점을 포함한 숫자 패턴: 정수부.소수부
        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(text);

        StringBuffer result = new StringBuffer();

        // 동적으로 DecimalFormat 패턴 생성
        String formatPattern;
        if (decimalPlaces == 0) {
            formatPattern = "#"; // 정수로 표시
        } else {
            StringBuilder patternBuilder = new StringBuilder("#.");
            for (int i = 0; i < decimalPlaces; i++) {
                patternBuilder.append("#");
            }
            formatPattern = patternBuilder.toString();
        }
        DecimalFormat df = new DecimalFormat(formatPattern);

        while (matcher.find()) {
            double number = Double.parseDouble(matcher.group());
            String rounded = df.format(number);
            matcher.appendReplacement(result, rounded);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
