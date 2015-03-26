package com.quya.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

public class MakePic {
    private static char mapTable[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9' };

    public static void getCertPic(int width, int height,String savePath) throws FileNotFoundException {
        //保存图片url
        File file = new File(savePath);
        
        System.out.println(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println(file.getAbsolutePath());
        FileOutputStream fos=new FileOutputStream(savePath+"\\"+"code.jpg");
        if (width <= 0)
            width = 60;
        if (height <= 0)
            height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        String strEnsure = "";
        for (int i = 0; i < 4; ++i) {
            strEnsure += mapTable[(int) (mapTable.length * Math.random())];
        }
        g.setColor(Color.black);
        g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
        String str = strEnsure.substring(0, 1);
        g.drawString(str, 8, 17);
        str = strEnsure.substring(1, 2);
        g.drawString(str, 20, 15);
        str = strEnsure.substring(2, 3);
        g.drawString(str, 35, 18);
        str = strEnsure.substring(3, 4);
        g.drawString(str, 45, 15);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            g.drawOval(x, y, 1, 1);
        }
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", fos);
            System.out.println("dingni");
        } catch (IOException e) {
        }
        System.out.println(strEnsure);
    }
    
    
    
    
    public static void getCertPicDirect(int width, int height,OutputStream os,HttpServletRequest request) throws FileNotFoundException {
        //保存图片url
        
        if (width <= 0)
            width = 60;
        if (height <= 0)
            height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        String strEnsure = "";
        for (int i = 0; i < 4; ++i) {
            strEnsure += mapTable[(int) (mapTable.length * Math.random())];
        }
        g.setColor(Color.black);
        g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
        String str = strEnsure.substring(0, 1);
        g.drawString(str, 8, 17);
        str = strEnsure.substring(1, 2);
        g.drawString(str, 20, 15);
        str = strEnsure.substring(2, 3);
        g.drawString(str, 35, 18);
        str = strEnsure.substring(3, 4);
        g.drawString(str, 45, 15);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            g.drawOval(x, y, 1, 1);
        }
        g.dispose();
        try {
            request.getSession().setAttribute("code", strEnsure);
            ImageIO.write(image, "JPEG", os);
        } catch (IOException e) {
        }
    }
}