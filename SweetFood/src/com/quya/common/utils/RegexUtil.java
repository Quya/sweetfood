package com.quya.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类 名 称：com.hollyinfo.hiiap.utils.Regex
 * 用于处理正则表达式
 * 创建日期: 2007-11-16
 * 文件名称：Regex.java
 * 项目声明：CIIMSS
 * 版权信息：和利时信息技术有限公司 工程部
 * 编 写 人：liwei2840
 */

public class RegexUtil {
    /******************************************
     * 简单的正则表达式应用
     * 1 非负整数 ：^\d+$
     * 2 正整数 ：^[0-9]*[1-9][0-9]*$
     * 3 非正整数（负整数 + 0): ^((-\d+)|(0+))$
     * 4 负整数 : ^-[0-9]*[1-9][0-9]*$
     * 5 整数 : ^-?\d+$
     * 6 非负浮点数（正浮点数 + 0）: ^\d+(\.\d+)?$
     * 7 正浮点数 : ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$
     * 8 非正浮点数（负浮点数 + 0）: ^((-\d+(\.\d+)?)|(0+(\.0+)?))$
     * 9 负浮点数 : ^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$
     * 10 浮点数 : ^(-?\d+)(\.\d+)?$
     **************************************************************/
    /**
     * 验证字符串是否满足正则表达式
     * @param str 需要验证的字符串
     * @param regexStr 正则表达式 
     */
    public static boolean isAccordWithStr(String str , String regexStr) {
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
    public static void main(String[] args) {
        String str = "-1111";
        System.out.println(isAccordWithStr(str, "^(-?\\d+)(\\.\\d+)?$"));;
    }
}
