package com.quya.common.utils.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import com.quya.ConstantDataManager;
import com.quya.common.utils.exception.BusinessException;
import com.quya.common.utils.log.HollyinfoLog;
import com.quya.common.utils.log.HollyinfoLogger;


public class FileUtil {
    private static HollyinfoLog log = HollyinfoLogger.getLog(FileUtil.class);
    /**
     * 遍历List,按行写入文件
     * @param list
     * @param lineMapper
     * @param fullFileName
     * @throws IOException
     */
    public static void writeFile(final List list, final LineMapper lineMapper,
            final String fullFileName) throws IOException {
        PrintWriter pw = null;
        try {
            int index = fullFileName.lastIndexOf(ConstantDataManager.FILE_SEPARATOR);
            String dir = fullFileName.substring(0, index);
            log.debug("创建临时文件夹：" + dir);
            createFolder(dir);
            //new OutputStream(new File(fullFileName , "UTF-8"));
            pw = new PrintWriter(new FileWriter(new File(fullFileName)));
            Iterator it = list.iterator();
            while (it.hasNext()) {
                pw.println(lineMapper.mapLine(it.next()));
            }
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * 新建文件夹
     * @param folderPath:目录路径
     * @return
     * @throws IOException
     */
    public static boolean createFolder(String folderPath) throws IOException {
        boolean result = false;
        File f = new File(folderPath);
        result = f.mkdirs();
        return result;
    }

    /**
     * 根据文件名获取文件的扩展名     * @param pFileNameFull
     * @return
     */
    public static String getFileNameExtension(final String pFileNameFull) {
        String extension = "";
        if ((pFileNameFull != null) && (pFileNameFull.length() > 0)) {
            int i = pFileNameFull.lastIndexOf('.');
            if ((i > -1) && (i < (pFileNameFull.length() - 1))) {
                return pFileNameFull.substring(i + 1);
            }
        }
        return extension;
    }
    /**
     * 新建文件
     * @param filepath
     * @return
     * @throws BusinessException
     */
    public static File createFile(String filepath)
            throws BusinessException {
        File file = new File(filepath);
        if (file.exists()) {
            file.delete();
        }
        File dirs = new File(filepath.substring(0, filepath.lastIndexOf("/")));
        dirs.mkdirs();
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("创建文件失败!");
        }
    }

    /**
     * 写字符串内容到文件
     * @param reform2
     * @param filename
     * @param file
     * @return
     */
    public static void writeContentToFile(String content, File file)
            throws BusinessException {
        if (content == null || content.length() == 0) {
            throw new BusinessException("要写入文件的内容为空");
        }
        char[] charbuf = content.toCharArray();
        FileOutputStream fileout = null;
        java.io.OutputStreamWriter outwriter = null;
        java.io.BufferedWriter bufwriter = null;
        try {
            fileout = new FileOutputStream(file);
            outwriter = new OutputStreamWriter(fileout,
                    "UTF-8");
            bufwriter = new java.io.BufferedWriter(outwriter);
            bufwriter.write(charbuf);
            bufwriter.flush();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("文件没有找到,请查看日志 ");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("无法支持文件的编码格式,请查看日志 ");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("写入文件内容出错,请查看日志 ");
        } finally {
            try {
                bufwriter.close();
                outwriter.close();
                fileout.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new BusinessException("写入文件出错,请查看日志 ");
            }
        }
    }
    /**
     * 从输入流里按照字符集编码,读出字符串内容
     * @param in
     * @param datain
     * @param file
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String getContentFromInputStream(InputStream in, String charset)
            throws BusinessException {
        String value = null;
        try {
            byte [] buf = new byte[in.available()];
            in.read(buf);
            value = new String(buf, charset);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException("获取文件内容失败");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return value;
    }
    /**
     * 删除文件
     * @param filepath
     * @throws BusinessException
     */
    public static void deleteFile(String filepath) throws BusinessException {
        File file = new File(filepath);
        if (file.exists()) {
            file.delete();
        }
    }
    /**
     * 从字节数组中按照字符集编码取出字符串内容
     * @param backbuf
     * @param charset
     * @return
     */
    public static String getContentFromBuffer(byte[] backbuf, String charset) {
        try {
            String value = new String(backbuf, charset);
            return value;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    public static String getContentFromUri(URI uri) {
        File file = new File(uri);
        FileInputStream in = null;
        InputStreamReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            in = new FileInputStream(file);
            reader = new InputStreamReader(in, "UTF-8");
            char [] buf = new char[Integer.parseInt("1024")];
            while (reader.read(buf) != -1) {
                content.append(buf);
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("文件没有找到");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("读取文件出错");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    /**
     * 向URI文件内容写入内容
     * @param uri
     * @param content
     */
    public static void writeContentToUriFile(URI uri, String content) {
        File file = new File(uri);
        FileOutputStream out = null;
        BufferedOutputStream outbuf = null;
        try {
           out = new FileOutputStream(file);
           outbuf = new BufferedOutputStream(out);
           byte [] strbuf = content.getBytes("UTF-8");
           outbuf.write(strbuf);
           outbuf.flush();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("从URI读取文件时,文件没有找到");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("从URI读取文件出错");
        } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (outbuf != null) {
                        outbuf.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
        }
    }
    
    /**
     * 批量删除文件或路径

     */
    public static void deleteDirectoryOrFile(List pDirs) {
        if (pDirs != null && pDirs.size() > 0) {
            for (int i = 0; i < pDirs.size(); i++) {
                File tFile = new File((String) pDirs.get(i));
                if (tFile.exists()) {
                    deleteDirectory(tFile);
                }
            }
        }
    }

    /**
     * 删除文件或目录

     * @param dirFile
     */
    private static void deleteDirectory(File dirFile) {
        if (dirFile.isDirectory()) {
            String dirPath = dirFile.getPath();
            String[] str = dirFile.list();
            for (int j = 0; j < str.length; j++) {
                deleteDirectory(new File(dirPath + ConstantDataManager.FILE_SEPARATOR + str[j]));
            }
            dirFile.delete();
        } else {
            dirFile.delete();
        }
    }
    
    /**
     * 删除某一目录下的全部文件(包括该目录)
     * @param f
     * @return
     */
    public static boolean deletedir(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory())
                    deletedir(files[i]);
                else
                    deleteFile(files[i].getPath());
            }
            f.delete();
        }
        return true;

    }
    /**
     * 去掉文件的BOM头
     * @param in
     * @return
     */
	public static InputStream delBomHeaderFromInputStream(InputStream in) {
		try {
			PushbackInputStream testin = new PushbackInputStream(in);
			int ch = testin.read();
			if (ch != 0xEF) {
				testin.unread(ch);
			} else if ((ch = testin.read()) != 0xBB) {
				testin.unread(ch);
				testin.unread(0xef);
			} else if ((ch = testin.read()) != 0xBF) {
				//
			}
			return testin;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new BusinessException("读取文件流出错，请查看日志.");
		}
	}
	 /**
	 * 删除文件
	 * @param file
	 * @return
	 */
    public static boolean delFile(File file){ 
		boolean result = false;    
	    int tryCount = 0;    
	    while(!result && tryCount++ <10){    
	     System.gc();    
	     result = file.delete();    
	     log.debug("删除文件："+result);
	     }
	    return result;    
   }
    
    /*public static void saveFileToDisk(Image im) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        File file = new File("wtf");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            fos = new FileOutputStream("\\"+"code.jpg");
            MakePic.getCertPic(width, height, os)
            fis = new FileInputStream(getImage());
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fos, fis);
        }
    }*/

    /*private static void close(FileOutputStream fos, FileInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
