/**
 * 
 */
package com.quya.common.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.quya.common.utils.log.HollyinfoLog;
import com.quya.common.utils.log.HollyinfoLogger;


/**
 * @author fengbaoxp
 * csv工具类
 */
public class CSVUtil {
	private static final String SPECIAL_CHAR_A = "[^\",\\n 　]";
	private static final String SPECIAL_CHAR_B = "[^\",\\n]";
	private static final Pattern pattern;
	private static HollyinfoLog LOG = HollyinfoLogger.getLog(CSVUtil.class);
	/*初始化静态变量*/
	static {
		StringBuffer sbRegExp = new StringBuffer();
		sbRegExp.append("\"((" + SPECIAL_CHAR_A + "*[,\\n 　])*(").append(
				SPECIAL_CHAR_A);
		sbRegExp.append("*\"{2})*)*").append(SPECIAL_CHAR_A);
		sbRegExp.append("*\"[ 　]*,[ 　]*|").append(SPECIAL_CHAR_B);
		sbRegExp.append("*[ 　]*,[ 　]*|\"((").append(SPECIAL_CHAR_A);
		sbRegExp.append("*[,\\n 　])*(").append(SPECIAL_CHAR_A);
		sbRegExp.append("*\"{2})*)*").append(SPECIAL_CHAR_A);
		sbRegExp.append("*\"[ 　]*|").append(SPECIAL_CHAR_B).append("*[ 　]*");
		pattern = Pattern.compile(sbRegExp.toString());
	}

	/**
	 * 将csv字符串解析成列表
	 * @param pStrCsv
	 * @return
	 */
	public static List parseCSV(String pStrCsv) {
		Matcher matcher = pattern.matcher(pStrCsv);
		List listTemp = new ArrayList();
		while (matcher.find()) {
			String str = matcher.group();
			str = str.trim();
			if (str.endsWith(",")) {
				str = str.substring(0, str.length() - 1);
				str = str.trim();
			}
			if (str.startsWith("\"") && str.endsWith("\"")) {
				str = str.substring(1, str.length() - 1);
				str = str.replaceAll("\"\"", "\"");
			}
			listTemp.add(str);
		}
		if (!listTemp.isEmpty()) {
			listTemp.remove(listTemp.size() - 1);
		}
		return listTemp;
	}

	public static void main(String[] args) {
		String csvFilePath = "C:/autoimport/农产品/test.csv";
		String txtFile = "C:/autoimport/农产品/test.txt";
		String dbfEncoding = "GBK";
		String descrimFlag = ",";
		try {
			writeToTxt(csvFilePath, txtFile, dbfEncoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean writeToTxt(String csvFilePath, String txtFile,
			String encoding) throws IOException {
		BufferedReader csvBufferedReader = new BufferedReader(new FileReader(
				csvFilePath));
		File destTxtFile = new File(txtFile);
		if (!destTxtFile.exists()) {
			destTxtFile.createNewFile();
		}
		BufferedOutputStream outBufStream = new BufferedOutputStream(
				new FileOutputStream(txtFile));
		try {
			String csvRow = csvBufferedReader.readLine();
			while (csvRow != null) {
				csvRow +=IOUtils.LINE_SEPARATOR_WINDOWS;
				byte[] strBufBytes = csvRow.getBytes(encoding);
				outBufStream.write(strBufBytes);
				csvRow = csvBufferedReader.readLine();
			}
			csvBufferedReader.close();
			outBufStream.flush();
			// 操作结束，关闭文件
			outBufStream.close();
			return true;
		} catch (IOException ioe) {
			try {
				csvBufferedReader.close();
				outBufStream.close();
			} catch (IOException e) {
				LOG.error("关闭csv文件出错");
			}
		} finally {
			csvBufferedReader.close();
			outBufStream.close();
		}
		return false;
	}

}