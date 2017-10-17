package com.security.news.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

/**
 * 文本工具类;
 * @author Feng
 */
public class TextUtil {
	
	/**
	 * 将汉字转化为pinyin;
	 * @param text :需要转化成拼音的汉字;
	 * @return :转化后的拼音;
	 */
	public static String getPinYin(String text) {
		return PinyinHelper.convertToPinyinString(text,"",PinyinFormat.WITHOUT_TONE);
	}

	/**
	 * 读取输入流中的字符,并将其转化为可读的字符;
	 * @param in:输入流InputStream;
	 * @return :将输入流转化成的Text文本;
	 */
	public static String transInputStreamToText(InputStream in){
		String text="";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			text = out.toString();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * 生成随机汉字
	 * http://www.cnblogs.com/skyivben/archive/2012/10/20/2732484.html
	 * @return :返回随机生成的汉字;
	 */
	public static char generateRandomChar() {
		String str = "";
		int hightPos;
		int lowPos;
		Random random = new Random();
		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));
		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(hightPos)).byteValue();
		b[1] = (Integer.valueOf(lowPos)).byteValue();
		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str.charAt(0);
	}
}
