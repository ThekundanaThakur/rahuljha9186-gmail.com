package com.attendance.system.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Test {
	static SimpleDateFormat format = new SimpleDateFormat("hh-mm-ss aa",Locale.ENGLISH);
	public static void main(String[] args) throws ParseException {
     String oo=format.format(new Date());
      System.out.println(oo);
	}
}