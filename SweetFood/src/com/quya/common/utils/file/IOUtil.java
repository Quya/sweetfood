package com.quya.common.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.apache.tools.ant.filters.StringInputStream;
import org.springframework.util.StringUtils;


/**
 * <p>
 * 封装了IO常用的操作方法。
 * </p>
 * @author meconsea
 * @version v1.0Beta
 */
public final class IOUtil {
    public static final Logger LOG = Logger.getLogger(IOUtil.class);

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Writes a String to a file creating the file if it does not exist using
     * the default encoding for the VM.
     * @param pFile
     * @param pData
     * @return write successful return true otherwrise false
     */
    private static boolean writeStringToFile(File pFile, String pData) {
        boolean flag = false;
        try {
            FileUtils.writeStringToFile(pFile, pData);
            flag = true;
        } catch (Exception e) {
            LOG.error("将数据写入到" + pFile.getName() + "出现异常：", e);
        }
        return flag;
    }

    /**
     * Writes a String to a file creating the file by filePath.
     * @param pFilePath
     * @param pData
     * @param pEncoding
     * @return write successful return true otherwrise false
     */
    public static boolean writeStringToFile(String pFilePath, String pData) {
        boolean flag = false;
        try {
            File file = new File(pFilePath);
          // RandomAccessFile rf=new RandomAccessFile(pFilePath ,"rw");
//          if (rf.length() == 0) {
//             fw.write(content);
//         } else {
            //rf.writeBytes(pData);
            
            
          flag = writeStringToFile(file, pData);
        } catch (Exception e) {
            LOG.error("将数据写入到" + pFilePath + "出现异常：", e);
        }
        return flag;
    }
    
    /**
     * Writes byte[] to a file creating the file by filePath.
     * @param pFilePath
     * @param pData
     * @param pEncoding
     * @return return true
     */
    public static boolean writeByteToFile(String pFilePath, byte content[]) {
        FileOutputStream stream=null;
        try {
            File file = new File(pFilePath);
            stream = new FileOutputStream(file);
            stream.write(content);
            stream.close();            
        } catch (Exception e) {
            LOG.error("将数据写入到" + pFilePath + "出现异常：", e);
            return false;
        }
        return true;
    }

    /**
     * write bytes from an inputream to a file.
     * @param pIs
     * @param filePath
     * @param pAppend
     * @return write successful return true otherwrise false
     */
    public static boolean writeFileByBinary(InputStream pIs, File pFile,
            boolean pAppend) {
        boolean flag = false;
        try {
            FileOutputStream fos = new FileOutputStream(pFile, pAppend);
            IOUtils.copy(pIs, fos);
            fos.flush();
            fos.close();
            pIs.close();
            flag = true;
        } catch (Exception e) {
            LOG.error("将字节流写入到" + pFile.getName() + "出现异常：", e);
        }
        return flag;
    }
    /**
     * write bytes from an inputream to a file.
     * @param pIs
     * @param filePath
     * @param pAppend
     * @return write successful return true otherwrise false
     */
    public static boolean writeFileByEncoding(File pInFile, File pOutFile,
            String inEncoding, String outEncoding) {
        boolean flag = false;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(pInFile));
            FileUtil.delBomHeaderFromInputStream(in);
            out = new BufferedOutputStream(new FileOutputStream(pOutFile));
            byte[] pBuffer = new byte[DEFAULT_BUFFER_SIZE];
            for (;;) {
                int res = in.read(pBuffer);
                if (res == -1) {
                    break;
                }
                if (res > 0) {
                    if (out != null) {
                        String str = new   String(pBuffer, inEncoding);
                        pBuffer =  str.getBytes(outEncoding);
                        out.write(pBuffer, 0, res);
                    }
                }
            }
            if (out != null) {
                out.close();
                out = null;
            }
            in.close();
        } catch (Exception e) {
            LOG.error("将字节流写入到" + pInFile.getName() + "出现异常：", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Throwable t) {
                }
            }
        }
        return flag;
    }

    /**
     * write bytes from an inputstream to a file of chars.
     * @param pIs
     * @param pFile
     * @param pAppend
     * @return
     */
    private static boolean writeFileB2C(InputStream pIs, File pFile,
            boolean pAppend) {
        boolean flag = false;
        try {
            FileWriter fw = new FileWriter(pFile, pAppend);
            IOUtils.copy(pIs, fw);
            fw.flush();
            fw.close();
            pIs.close();
            flag = true;
        } catch (Exception e) {
            LOG.error("将字节流写入到" + pFile.getName() + "出现异常：", e);
        }
        return flag;
    }

    /**
     * write bytes from an inputstream to a file of chars.
     * @param pIs
     * @param pFilePath
     * @param pAppend
     * @return
     */
    public static boolean writeFileB2C(InputStream pIs, String pFilePath,
            boolean pAppend) {
        boolean flag = false;
        try {
            flag = writeFileB2C(pIs, new File(pFilePath), pAppend);
        } catch (Exception e) {
            LOG.error("将字节流写入到" + pFilePath + "出现异常：", e);
        }
        return flag;
    }

    public static String readStrData(String pFilePath){
        String res = "";
        FileReader fr = null;
        try{
            fr = new FileReader(pFilePath);
            res = IOUtils.toString(fr);
            fr.close();
        }catch(Exception e) {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            LOG.error("读取文件内容出现异常",e);
        }
        return res;
    }
    public static boolean appendStr2File(String pFilePath, String content, boolean pAppend) {
    	
    	return appendStr2File( pFilePath,  content,  pAppend, IOUtils.LINE_SEPARATOR_UNIX);
    }
    public static boolean appendStr2File(String pFilePath, String content, boolean pAppend, String separator) {
        OutputStream out = null;
        InputStream in = null;
        try {
            if (StringUtils.hasText(content)) {
                in = new StringInputStream(content + separator);
            } else {
                in = new StringInputStream(content);
            }
            out = new FileOutputStream(pFilePath, pAppend);
            byte[] pBuffer = new byte[DEFAULT_BUFFER_SIZE];
            long total = 0;
            for (;;) {
                int res = in.read(pBuffer);
                if (res == -1) {
                    break;
                }
                if (res > 0) {
                    total += res;
                    if (out != null) {
                        out.write(pBuffer, 0, res);
                    }
                }
            }
            if (out != null) {
                out.close();
                out = null;
            }
            in.close();
            in = null;
            return true;
        } catch (Exception e) {
            LOG.error("追加文件出错" + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) {
                    /* Ignore me */
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Throwable t) {
                    /* Ignore me */
                }
            }
        }
        return false;
    }
    /**
     * 在指定文件的指定行插入字符串
     * @param pFilePath
     * @param content
     * @param pAppend
     * @param line
     * @return
     */public static boolean appendStrToLineOfFile(String pFilePath, String content, boolean pAppend,long line) {
        try
        {
            //FileWriter fw = new FileWriter(pFilePath, pAppend);
            RandomAccessFile rf=new RandomAccessFile(pFilePath ,"rw");
            rf.seek(line);
            rf.writeBytes(content + IOUtils.LINE_SEPARATOR_UNIX);
//            if (rf.length() == 0) {
//               fw.write(content);
//           } else {
               //fw.write(content + IOUtils.LINE_SEPARATOR_UNIX);
           //}
         rf.close();
        // fw.close();
         return true;
        }
        catch(Exception e)
        {
            LOG.error("追加文件出错" + e.getMessage());
        }
        
        return false;
    }
     /**
      * 判断文件的换行符是line feed(unix)=false 还是 carrigage return(window)
      * @param datafilepath
      * @return 
      * @default  false
      * 默认lf
      */public static boolean isWindowLineFlag(String datafilepath) {
         try {
         File file = new File(datafilepath);
         InputStream is = new FileInputStream(file);
       
         int bytelen = Math.min((int)file.length(),128);
         byte[] bytes = new byte[bytelen];
         while (is.read(bytes,0, bytelen) > 0) {
             for (int i = 0; i < bytes.length; i++) {
                 if (bytes[i] == MultipartStream.CR) {
                     LOG.error(datafilepath + "---is window line seperator");
                     return true;
                 } else   if (bytes[i] == MultipartStream.LF) {
                     LOG.error(datafilepath + "---  is  UNIX line seperator");
                     return false;
                 }
             }
         }
         is.close();
     } catch (IOException ioe) {
         ioe.printStackTrace();
     }
     return false;
     } 
}
