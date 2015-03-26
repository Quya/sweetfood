package com.quya.common.utils;

import java.util.Vector;

/**
 * 类 名 称：com.hollyinfo.hiiap.utils.StringSplit 创建日期: 2007-10-18
 * 文件名称：StringSplit.java 项目声明：CIIMSS 版权信息：和利时信息技术有限公司 工程部 编 写 人：Administrator
 */

public class StringSplit {
    public static String[] split(String str) throws Exception {
        // 初始化集合用来保存字符串
        Vector allArgs = new Vector();
        StringBuffer stringBuffer = new StringBuffer(str);
//      删除最后一个，
        if (stringBuffer.lastIndexOf(",") == stringBuffer.length() - 1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        // 对于字符串为空或为空字符串直接返回空数组
        if (stringBuffer == null || stringBuffer.length() == 0) {
            return new String[] {};
        }

        setVector(allArgs, stringBuffer);
        String[] strArgs = new String[allArgs.size()];
        {
            for (int i = 0; i < allArgs.size(); i++) {
                strArgs[i] = (String) allArgs.get(i);
            }
        }
        return strArgs;
    }

    private static void setVector(Vector allArgs, StringBuffer stringBuffer)
            throws Exception {
        if (stringBuffer.charAt(0) == '"') {
            dealWithYinHao(allArgs, stringBuffer);
        } else {
            dealWithNoYinHao(allArgs, stringBuffer);
        }
    }

    private static void dealWithNoYinHao(Vector allArgs,
            StringBuffer stringBuffer) throws Exception {
        // 处理,,这种情况
        if (stringBuffer.indexOf(",") == 0) {
            allArgs.add("");
            stringBuffer.delete(0, 1);
            if (stringBuffer.length() == 0) {
                allArgs.add("");
            } else {
                setVector(allArgs, stringBuffer);
            }
        } else {
            int lastArrayIndex = stringBuffer.indexOf(",", 1);
            // 整个内容都包含在“”内
            if (lastArrayIndex == -1) {
                allArgs.add(stringBuffer
                        .substring(0, stringBuffer.length()));
            } else {
                allArgs.add(stringBuffer.substring(0, lastArrayIndex));
                stringBuffer.delete(0, lastArrayIndex + 1);
                if (!(stringBuffer == null || stringBuffer.length() == 0)) {
                    setVector(allArgs, stringBuffer);
                } else {
                    allArgs.add("");
                }
            }
        }

    }

    private static void dealWithYinHao(Vector allArgs, StringBuffer stringBuffer)
            throws Exception {
        int lastArrayIndex = stringBuffer.indexOf("\",", 1);
        // 整个内容都包含在“”内
        if (lastArrayIndex == -1) {
            // 验证最后一个字符是否为",从而保证数据的完整性
            if (stringBuffer.lastIndexOf("\"") != stringBuffer.length() - 1) {
                throw new Exception("需要解析的字符串格式不正确");
            } else {
                // 将""中间的内容作为内容封装到集合中
                allArgs.add(stringBuffer
                        .substring(1, stringBuffer.length() - 1));
            }

        } else {
            allArgs.add(stringBuffer.substring(1, lastArrayIndex));
            stringBuffer.delete(0, lastArrayIndex + 2);
            if (!(stringBuffer == null || stringBuffer.length() == 0)) {
                setVector(allArgs, stringBuffer);
            }
        }
    }
    public static void main(String args[]) throws Exception {
        String str = ",,,,,";
        String ar[] = StringSplit.split(str);
        for (int i = 0; i < ar.length; i++) {
            System.out.println(i + ": " + ar[i]);
        }
    }
}
