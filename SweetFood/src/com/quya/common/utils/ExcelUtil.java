/**
 * 
 */
package com.quya.common.utils;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;



/**
 * 
 * @author yanghongtao
 * @date 2009-12-30
 * 
 */
public final class ExcelUtil {
	private static final Logger LOG = Logger.getLogger(ExcelUtil.class);
	private static final String NULLVALUE = "NULL";

	/**
	 * 
	 * 创建一个workbook
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static HSSFWorkbook createWorkbook(InputStream in)
			throws IOException {
		POIFSFileSystem fs;
		HSSFWorkbook wb = null;
		try {
			fs = new POIFSFileSystem(in);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			LOG.error(e.getMessage());
			in.close();
		}
		return wb;
	}
	private static String parseCellValeByType(HSSFCell cell) {
		String strCell = "";
		try {
			if (cell != null) {
				int cellType = cell.getCellType();
				switch (cellType) {
					case HSSFCell.CELL_TYPE_NUMERIC://
						if ( HSSFDateUtil.isCellDateFormatted(cell) ) {
							   strCell = datetimeFormat(cell.getDateCellValue());
						} else if (isInteger(cell.getNumericCellValue())) {
							   strCell = String.valueOf((int)cell.getNumericCellValue());
						} else {
							   strCell = String.valueOf(roundDouble(cell.getNumericCellValue(),2));
						}
						break;
					case HSSFCell.CELL_TYPE_STRING:// 处理String数据类型
						HSSFRichTextString richTextString = cell
								.getRichStringCellValue();
						strCell = new String(richTextString.toString().getBytes(), System.getProperty("file.encoding"));
						try {
						    double val = Double.parseDouble(strCell);
						    strCell = String.valueOf(roundDouble(val,2));
						} catch (NumberFormatException nfEx) {
						}
						
						break;
				}
				return strCell.replace('?', ' ');
			  }
			} catch (Exception e) {
				e.printStackTrace();
			}
		return strCell;
	}
	public static boolean writeToTxt(String xlsFilePath, String destFile,
			 String decrimFlag) throws IOException {

		InputStream srcInputStream = new FileInputStream(xlsFilePath);
		FileOutputStream outFile = new FileOutputStream(destFile);
		BufferedOutputStream outBufStream = new BufferedOutputStream(outFile);
		try {
			HSSFWorkbook wb = createWorkbook(srcInputStream);
			if (wb != null) {
				// for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets();
				// sheetIndex++) {
				HSSFSheet sheet = wb.getSheetAt(0);
				if (null != sheet) {
					for (int rowNumOfSheet = 0; rowNumOfSheet < sheet
							.getLastRowNum(); rowNumOfSheet++) {
						StringBuffer strBuf = new StringBuffer();
						// 判断行的存在性
						HSSFRow row = sheet.getRow(rowNumOfSheet);
						// 获得一行
						for (short colNumOfSheet = 0; colNumOfSheet < row
								.getLastCellNum(); colNumOfSheet++) {
							// 判断列的存在性
							HSSFCell cell = row.getCell((short) colNumOfSheet);
							if (null != cell) {
								String value = parseCellValeByType(cell);

								// 如果是NULL， 需要转换成空字符串k
								if (NULLVALUE.equals(value)) {
									value = "";
								}
								strBuf.append(value);
								strBuf.append(decrimFlag);
							} else {
								strBuf.append(decrimFlag);
							}
						}
						String valStr = strBuf.substring(0, strBuf.lastIndexOf(decrimFlag));
						valStr +=IOUtils.LINE_SEPARATOR_WINDOWS;
						byte[] strBufBytes = valStr.getBytes(
								System.getProperty("file.encoding"));
						outBufStream.write(strBufBytes);
					}
				}
				outBufStream.flush();
			}
			srcInputStream.close();
			outBufStream.close();
			return true;
		} catch (IOException ioe) {
			try {
				srcInputStream.close();
				outBufStream.close();
			} catch (IOException e) {
				LOG.error("关闭csv文件出错");
			}
		} finally {
			srcInputStream.close();
			outBufStream.close();
		}
		return false;
	}
	 public static String datetimeFormat(java.util.Date date) {
		  
		  SimpleDateFormat pdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		  String dateStr = "";
		  try {
		   if (date != null) {
		    dateStr = pdf.format(date);
		   }
		  }catch (Exception e){
		   e.printStackTrace();
		  }
		  return dateStr;
		 }
		 

		
		 
		 /**
		  * judge the input val whether can be a integer val without value changing.
		  * <br>like 15.00 -> 15: true; 15.35 -> 15: false;
		  * @param val
		  * @return
		  */
		 public static boolean isInteger(double val){
		  double exp = 0.0000000000000001;
		  return (Math.abs(val - (int)val)) < exp;
		 }
		 public static Double roundDouble(double val, int precision) {  
	         Double ret = null;  
	         try {  
	             double factor = Math.pow(10, precision);  
	             ret = new Double(Math.floor(val * factor + 0.5) / factor);  
	         } catch (Exception e) {  
	             e.printStackTrace();  
	         }  
	   
	        return ret;  
	    } 
	public static void main(String[] args) {
		String csvFilePath = "C:/autoimport/农产品/test.xls";
		String txtFile = "C:/autoimport/农产品/test.txt";
		String dbfEncoding = "GBK";
		String descrimFlag = ",";
		try {
			writeToTxt(csvFilePath, txtFile, descrimFlag);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
