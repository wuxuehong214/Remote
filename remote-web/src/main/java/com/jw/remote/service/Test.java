package com.jw.remote.service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class Test {

	public static void main(String[] args) throws IOException {

//		BufferedImage bi = ImageIO.read(new File("DSC_0230.JPG"));
//		ByteArrayOutputStream out = new ByteArrayOutputStream();  
//       ImageIO.write(bi, "png", out);  
//        byte[] b = out.toByteArray(); 		
//        System.out.println("原始长度:"+b.length);
//        String str = Base64.encodeBase64String(b);
//        
////        System.out.println(str);
//        
//        //原始图保存
//        byte[] d = Base64.decodeBase64(str);
//        System.out.println(d.length);
//        ByteArrayInputStream in = new ByteArrayInputStream(d);
//        BufferedImage image = ImageIO.read(in);
//        ImageIO.write(image, "png", new File("15802540365.png"));
//        
//        //缩略图保存
//        Image src = image.getScaledInstance(48,48, Image.SCALE_DEFAULT);
//        BufferedImage dest = new BufferedImage(48, 48, BufferedImage.TYPE_INT_RGB);
//        Graphics g = dest.getGraphics();
//        g.drawImage(src, 0, 0, null);
//        g.dispose();
//		ImageIO.write(dest, "png", new File("15802540365_2.png"));
		
		String  s = "2015-6-6 22:30:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {
			d = sdf.parse(s);
			System.out.println(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
