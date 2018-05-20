package com.hub.anyspot.utils;

import java.io.UnsupportedEncodingException;

public class EncodingUtil {

	public static String encoding(String orignText) {
		String encodeText = "";
		try {
			encodeText = new String(orignText.getBytes("8859_1"), "utf-8");
		} catch (UnsupportedEncodingException e1) {}
		
		return encodeText;
	}
}
