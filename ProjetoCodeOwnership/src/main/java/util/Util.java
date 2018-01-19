package util;

import java.util.Arrays;

public class Util {
	public static final String LS = System.lineSeparator();
	
	public static boolean isJavaClass(String string) {
		String[] splitted = string.split("\\.");

		if (splitted.length == 2) {
			return splitted[1].equals("java");
		} else {
			return false;
		}
	}

	public static String getNamesFromTxt(String txtPath) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
