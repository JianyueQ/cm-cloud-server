package com.cm.utils;

import cn.hutool.core.lang.UUID;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * @author 31373
 */
public class UUIDUtils {

    /**
     * 使用UUID生成指定长度的数字工号
     * @param length 工号长度
     * @return 指定长度的数字工号
     */
    public static String generateNumericJobNumber(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 将UUID转换为数字字符
        StringBuilder numericString = new StringBuilder();
        for (int i = 0; i < uuid.length() && numericString.length() < length; i++) {
            char c = uuid.charAt(i);
            if (Character.isDigit(c)) {
                numericString.append(c);
            }
        }

        // 如果数字不够，用UUID的哈希值补充
        while (numericString.length() < length) {
            int hash = uuid.hashCode();
            String hashStr = String.valueOf(Math.abs(hash));
            for (char c : hashStr.toCharArray()) {
                if (numericString.length() < length) {
                    numericString.append(c);
                }
            }
        }

        return numericString.substring(0, length);
    }

    /**
     * 使用UUID生成带日期前缀的工号
     * @return 工号
     */
    public static String generateJobNumberWithDateAndUUID() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 生成UUID并取前3位作为序列号
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String sequence = uuid.substring(0, 3).toUpperCase();

        // 组合：日期+序列号
        return dateStr + sequence;
    }

}
