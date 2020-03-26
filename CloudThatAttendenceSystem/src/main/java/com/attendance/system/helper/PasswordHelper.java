package com.attendance.system.helper;
import org.apache.commons.codec.binary.Base64;

public class PasswordHelper {
	
	
	public static String passwordEncode(String password)
	{
		byte[] bytesEncoded = Base64.encodeBase64(password.getBytes());
		return new String(bytesEncoded);
	}

	public static String passwordDecode(String password)
	{
		byte[] valueDecoded = Base64.decodeBase64(password.getBytes());
		return new String(valueDecoded);
	}
	
	
}
